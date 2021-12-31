/*
 * Copyright (c) 2012, Finn Kuusisto
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *     Redistributions of source code must retain the above copyright notice,
 *     this list of conditions and the following disclaimer.
 *
 *     Redistributions in binary form must reproduce the above copyright notice,
 *     this list of conditions and the following disclaimer in the documentation
 *     and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package afyber.shmupfeaturecreep.engine.audio;

import afyber.shmupfeaturecreep.MainClass;
import afyber.shmupfeaturecreep.engine.GeneralUtil;
import afyber.shmupfeaturecreep.engine.audio.music.*;
import afyber.shmupfeaturecreep.engine.audio.sounds.SoundDataReference;
import afyber.shmupfeaturecreep.engine.audio.sounds.SoundInstance;
import afyber.shmupfeaturecreep.engine.errors.SoundsNotDefinedError;
import afyber.shmupfeaturecreep.engine.output.LoggingLevel;

import javax.sound.sampled.*;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;

/**
 * Sounds, Roxanne. I was afraid of sounds.
 *
 * @author afyber
 */
public class Sound {
	private Sound() {}

	private static boolean ready = false;

	static HashMap<String, SoundDataReference> allSounds = new HashMap<>();
	static HashMap<String, MusicParent> allMusic = new HashMap<>();
	static HashMap<String, LoopParent> allLoops = new HashMap<>();

	public static final AudioFormat FORMAT = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, 44100, 16, 2, 4, 44100, false);

	public static final AudioFormat MONO_FORMAT = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, 44100, 16, 1, 2, 44100, false);

	private static SourceDataLine soundLine;
	private static Mixer mixer;

	private static SoundUpdateRunner updater;

	public static void init() {
		DataLine.Info info = new DataLine.Info(SourceDataLine.class, FORMAT);

		if (!AudioSystem.isLineSupported(info)) {
			MainClass.LOGGER.log(LoggingLevel.ERROR, "Unsupported audio output, no audio will be available!");
			return;
		}

		soundLine = tryGetLine();
		if (soundLine == null) {
			MainClass.LOGGER.log(LoggingLevel.ERROR, "Couldn't get audio data line, no audio will be available!");
			return;
		}

		soundLine.start();

		loadSounds();

		mixer = new Mixer();

		updater = new SoundUpdateRunner(mixer, soundLine);

		Thread updaterThread = new Thread(updater);
		try {
			updaterThread.setName("EngineAudioUpdater");
			updaterThread.setDaemon(true);
			updaterThread.setPriority(Thread.MAX_PRIORITY);
		}
		catch (Exception ignored) {}

		updaterThread.start();

		ready = true;
	}

	public static void shutdown() {
		if (updater != null) {
			updater.stop();
			updater = null;
		}
		if (soundLine != null) {
			soundLine.stop();
			soundLine.flush();
		}
		mixer = null;
	}

	public static void loadSounds() {
		String file;
		try {
			file = GeneralUtil.readResourceToString("/sounds.txt");
		}
		catch (IOException e) {
			MainClass.LOGGER.log(LoggingLevel.ERROR, "Couldn't load sounds.txt");
			throw new SoundsNotDefinedError();
		}

		String[] lines = file.split("\r\n");

		for (String line: lines) {
			if (!line.startsWith("//")) {
				if (line.startsWith("loop:")) {
					String[] split = line.substring(5).split(":");
					if (split.length == 3) {
						registerBasicLoop("/sounds/" + split[0], "/sounds/" + split[1], split[2]);
					}
					else if (split.length == 4) {
						registerComplexLoop("/sounds/" + split[0], "/sounds/" + split[1], "/sounds/" + split[2], split[3]);
					}
				}
				else if (line.startsWith("music:")) {
					String[] split = line.substring(6).split(":");
					registerMusic("/sounds/" + split[0], split[1]);
				}
				else {
					String[] split = line.split(":");
					Sound.registerSound("/sounds/" + split[0], split[1]);
				}
			}
		}
	}

	public static void registerSound(String fileName, String soundName) {
		SoundDataReference sound = loadSound(fileName, soundName);

		if (sound == null) {
			MainClass.LOGGER.log(LoggingLevel.ERROR, "Registering sound with null sound reference");
			return;
		}

		allSounds.put(soundName, sound);
	}

	public static void registerMusic(String fileName, String soundName) {
		MusicParent music = loadMusic(fileName);

		if (music == null) {
			MainClass.LOGGER.log(LoggingLevel.ERROR, "Registering music with null music reference");
			return;
		}

		allMusic.put(soundName, music);
	}

	public static void registerBasicLoop(String introFileName, String loopFileName, String soundName) {
		LoopParent loop = loadBasicLoop(introFileName, loopFileName);

		if (loop == null) {
			MainClass.LOGGER.log(LoggingLevel.ERROR, "Registering loop with null loop reference");
			return;
		}

		allLoops.put(soundName, loop);
	}

	public static void registerComplexLoop(String introFileName, String introLoopFileName, String loopLoopFileName, String soundName) {
		LoopParent loop = loadComplexLoop(introFileName, introLoopFileName, loopLoopFileName);

		if (loop == null) {
			MainClass.LOGGER.log(LoggingLevel.ERROR, "Registering loop with null loop reference");
			return;
		}

		allLoops.put(soundName, loop);
	}

	public static void setGlobalGain(double gain) {
		if (!ready) {
			return;
		}

		if (gain < 0) {
			gain = 0;
		}
		else if (gain > 1.0) {
			gain = 1.0;
		}

		mixer.setGlobalVolume(gain);
	}

	public static void playMusic(String soundName) {
		if (!ready) {
			return;
		}

		if (allMusic.containsKey(soundName)) {
			allMusic.get(soundName).play();
		}
		if (allLoops.containsKey(soundName)) {
			allLoops.get(soundName).play();
		}
	}

	public static void stopMusic(String soundName) {
		if (!ready) {
			return;
		}

		if (allMusic.containsKey(soundName)) {
			allMusic.get(soundName).stop();
		}
		if (allLoops.containsKey(soundName)) {
			allLoops.get(soundName).stop();
		}
	}

	public static void pauseMusic(String soundName) {
		if (!ready) {
			return;
		}

		if (allMusic.containsKey(soundName)) {
			allMusic.get(soundName).pause();
		}
		if (allLoops.containsKey(soundName)) {
			allLoops.get(soundName).pause();
		}
	}

	public static void resumeMusic(String soundName) {
		if (!ready) {
			return;
		}

		if (allMusic.containsKey(soundName)) {
			allMusic.get(soundName).resume();
		}
		if (allLoops.containsKey(soundName)) {
			allLoops.get(soundName).resume();
		}
	}

	public static void loopMusic(String soundName) {
		if (!ready) {
			return;
		}

		if (allMusic.containsKey(soundName)) {
			allMusic.get(soundName).setLoop(true);
			allMusic.get(soundName).play();
		}
		if (allLoops.containsKey(soundName)) {
			allLoops.get(soundName).play();
		}
	}

	public static void setMusicGain(String soundName, double gain) {
		if (!ready) {
			return;
		}

		if (gain < 0) {
			gain = 0;
		} else if (gain > 1.0) {
			gain = 1.0;
		}
		if (allMusic.containsKey(soundName)) {
			allMusic.get(soundName).setGain(gain);
		}
		if (allLoops.containsKey(soundName)) {
			allLoops.get(soundName).setGain(gain);
		}
	}

	public static void setMusicPanning(String soundName, double pan) {
		if (!ready) {
			return;
		}

		if (pan < -1.0) {
			pan = -1.0;
		} else if (pan > 1.0) {
			pan = 1.0;
		}
		if (allMusic.containsKey(soundName)) {
			allMusic.get(soundName).setPanning(pan);
		}
		if (allLoops.containsKey(soundName)) {
			allLoops.get(soundName).setPanning(pan);
		}
	}

	public static void playSound(String soundName) {
		if (!ready) {
			return;
		}

		if (allSounds.containsKey(soundName)) {
			mixer.addSound(new SoundInstance(allSounds.get(soundName)));
		}
	}

	public static void stopSound(String soundName) {
		if (!ready) {
			return;
		}

		for (SoundInstance sound: mixer.playingSounds) {
			if (sound.getSoundRefName().equals(soundName)) {
				mixer.removeSound(sound);
			}
		}
	}

	public static void setSoundGain(String soundName, double gain) {
		if (!ready) {
			return;
		}

		if (allSounds.containsKey(soundName)) {
			if (gain < 0) {
				gain = 0;
			} else if (gain > 1.0) {
				gain = 1.0;
			}

			for (SoundInstance sound: mixer.playingSounds) {
				if (sound.getSoundRefName().equals(soundName)) {
					sound.setGain(gain);
				}
			}
		}
	}

	public static void setSoundPanning(String soundName, double pan) {
		if (!ready) {
			return;
		}

		if (allSounds.containsKey(soundName)) {
			if (pan < -1.0) {
				pan = -1.0;
			} else if (pan > 1.0) {
				pan = 1.0;
			}

			for (SoundInstance sound: mixer.playingSounds) {
				if (sound.getSoundRefName().equals(soundName)) {
					sound.setPanning(pan);
				}
			}
		}
	}

	private static SoundDataReference loadSound(String fileName, String soundName) {
		URL url = MainClass.class.getResource(fileName);

		if (url == null) {
			MainClass.LOGGER.log(LoggingLevel.ERROR, "URL was null when loading sound");
			return null;
		}

		return loadSound(url, soundName);
	}

	private static SoundDataReference loadSound(URL url, String soundName) {
		AudioInputStream stream = getValidAudioInputStream(url);

		if (stream == null) {
			MainClass.LOGGER.log(LoggingLevel.ERROR, "Couldn't get correctly formatted audio stream when loading sound");
			return null;
		}

		int numChannels = stream.getFormat().getChannels();

		byte[][] data = null;
		if (numChannels == 1) {
			data = new byte[][]{ readAllBytesMono(stream), null };
		}
		else if (numChannels == 2) {
			data = readAllBytesStereo(stream);
		}

		if (data == null) {
			MainClass.LOGGER.log(LoggingLevel.ERROR, "Couldn't read sound data from stream");
			return null;
		}

		return new SoundDataReference(data, numChannels, soundName);
	}

	private static MusicParent loadMusic(String fileName) {
		URL url = MainClass.class.getResource(fileName);

		if (url == null) {
			MainClass.LOGGER.log(LoggingLevel.ERROR, "URL was null when loading music");
			return null;
		}

		return loadMusic(url);
	}

	private static MusicParent loadMusic(URL url) {
		AudioInputStream stream = getValidAudioInputStream(url);

		if (stream == null) {
			MainClass.LOGGER.log(LoggingLevel.ERROR, "Couldn't get correctly formatted audio stream when loading music");
			return null;
		}

		int numChannels = stream.getFormat().getChannels();

		byte[][] data = null;
		if (numChannels == 1) {
			data = new byte[][]{ readAllBytesMono(stream), null };
		}
		else if (numChannels == 2) {
			data = readAllBytesStereo(stream);
		}

		if (data == null) {
			MainClass.LOGGER.log(LoggingLevel.ERROR, "Couldn't read music data from stream");
			return null;
		}

		return new BasicMusic(data, numChannels);
	}

	private static LoopParent loadBasicLoop(String introFileName, String loopFileName) {
		URL url1 = MainClass.class.getResource(introFileName);
		URL url2 = MainClass.class.getResource(loopFileName);

		if (url1 == null || url2 == null) {
			MainClass.LOGGER.log(LoggingLevel.ERROR, "Attempting to load basic loop with null file reference");
			return null;
		}

		return loadBasicLoop(url1, url2);
	}

	private static LoopParent loadBasicLoop(URL introFile, URL loopFile) {
		AudioInputStream stream1 = getValidAudioInputStream(introFile);
		AudioInputStream stream2 = getValidAudioInputStream(loopFile);

		if (stream1 == null || stream2 == null) {
			MainClass.LOGGER.log(LoggingLevel.ERROR, "Attempting to load basic loop with null audio stream");
			return null;
		}

		int numChannels1 = stream1.getFormat().getChannels();
		int numChannels2 = stream2.getFormat().getChannels();

		byte[][] data1 = null;
		byte[][] data2 = null;

		if (numChannels1 == 1) {
			data1 = new byte[][]{ readAllBytesMono(stream1), null };
		}
		else if (numChannels1 == 2) {
			data1 = readAllBytesStereo(stream1);
		}
		if (numChannels2 == 1) {
			data2 = new byte[][]{ readAllBytesMono(stream2), null };
		}
		else if (numChannels2 == 2) {
			data2 = readAllBytesStereo(stream2);
		}

		if (data1 == null || data2 == null) {
			MainClass.LOGGER.log(LoggingLevel.ERROR, "An oopsie whoopsie happened when loading the audio data for a basic loop (too many channels?)");
			return null;
		}

		return new BasicLoop(data1, data2, numChannels1, numChannels2);
	}

	private static LoopParent loadComplexLoop(String introFileName, String introLoopFileName, String loopLoopFileName) {
		URL url1 = MainClass.class.getResource(introFileName);
		URL url2 = MainClass.class.getResource(introLoopFileName);
		URL url3 = MainClass.class.getResource(loopLoopFileName);

		if (url1 == null || url2 == null || url3 == null) {
			MainClass.LOGGER.log(LoggingLevel.ERROR, "Attempting to load complex loop with null file reference");
			return null;
		}

		return loadComplexLoop(url1, url2, url3);
	}

	private static LoopParent loadComplexLoop(URL introFile, URL introLoopFile, URL loopLoopFile) {
		AudioInputStream introStream = getValidAudioInputStream(introFile);
		AudioInputStream introLoopStream = getValidAudioInputStream(introLoopFile);
		AudioInputStream loopLoopStream = getValidAudioInputStream(loopLoopFile);

		if (introStream == null || introLoopStream == null || loopLoopStream == null) {
			MainClass.LOGGER.log(LoggingLevel.ERROR, "Attempting to load complex loop with null audio stream");
			return null;
		}

		int channels1 = introStream.getFormat().getChannels();
		int channels2 = introLoopStream.getFormat().getChannels();
		int channels3 = loopLoopStream.getFormat().getChannels();

		byte[][] data1 = null;
		byte[][] data2 = null;
		byte[][] data3 = null;

		if (channels1 == 1) {
			data1 = new byte[][]{ readAllBytesMono(introStream), null };
		}
		else if (channels1 == 2) {
			data1 = readAllBytesStereo(introStream);
		}
		if (channels2 == 1) {
			data2 = new byte[][]{ readAllBytesMono(introLoopStream), null };
		}
		else if (channels2 == 2) {
			data2 = readAllBytesStereo(introLoopStream);
		}
		if (channels3 == 1) {
			data3 = new byte[][]{ readAllBytesMono(loopLoopStream), null };
		}
		else if (channels3 == 2) {
			data3 = readAllBytesStereo(loopLoopStream);
		}

		if (data1 == null || data2 == null || data3 == null) {
			MainClass.LOGGER.log(LoggingLevel.ERROR, "Null audio data when loading a complex loop (too many channels?)");
			return null;
		}

		return new ComplexLoop(data1, data2, data3, channels1, channels2, channels3);
	}

	private static byte[] readAllBytesMono(AudioInputStream stream) {
		byte[] data = null;
		try (stream) {
			data = getBytes(stream);
		} catch (IOException e) {
			MainClass.LOGGER.log(LoggingLevel.ERROR, "IOException reading bytes from audio stream", e);
		}

		return data;
	}

	private static byte[][] readAllBytesStereo(AudioInputStream stream) {
		byte[][] data = null;
		try (stream) {
			byte[] tmpData = getBytes(stream);

			data = new byte[2][];
			byte[] left = new byte[tmpData.length / 2];
			byte[] right = new byte[tmpData.length / 2];

			for (int i = 0; i < tmpData.length; i += 4) {
				left[i / 2] = tmpData[i];
				left[i / 2 + 1] = tmpData[i + 1];
				right[i / 2] = tmpData[i + 2];
				right[i / 2 + 1] = tmpData[i + 3];
			}

			data[0] = left;
			data[1] = right;
		} catch (IOException e) {
			MainClass.LOGGER.log(LoggingLevel.ERROR, "IOException reading bytes from audio stream", e);
		}

		return data;
	}

	private static byte[] getBytes(AudioInputStream stream) throws IOException {
		int bufferSize = (int)FORMAT.getSampleRate() * FORMAT.getFrameSize();
		byte[] buffer = new byte[bufferSize];
		ByteList list = new ByteList();
		int numRead;
		while ((numRead = stream.read(buffer)) > -1) {
			for (int i = 0; i < numRead; i++) {
				list.add(buffer[i]);
			}
		}
		return list.toArray();
	}

	private static AudioInputStream getValidAudioInputStream(URL url) {
		AudioInputStream stream;
		try {
			stream = AudioSystem.getAudioInputStream(url);
			AudioFormat streamFormat = stream.getFormat();

			if (streamFormat.getChannels() == 1) {
				if (streamFormat.matches(MONO_FORMAT)) {
					return stream;
				}
				else if (AudioSystem.isConversionSupported(MONO_FORMAT, streamFormat)) {
					stream = AudioSystem.getAudioInputStream(MONO_FORMAT, stream);
				}
			}
			else if (streamFormat.getChannels() == 2) {
				if (streamFormat.matches(FORMAT)) {
					return stream;
				}
				else if (AudioSystem.isConversionSupported(FORMAT, streamFormat)) {
					stream = AudioSystem.getAudioInputStream(FORMAT, stream);
				}
			}
		}
		catch (UnsupportedAudioFileException e) {
			MainClass.LOGGER.log(LoggingLevel.ERROR, "Unsupported audio file-type");
			return null;
		}
		catch (IOException e) {
			MainClass.LOGGER.log(LoggingLevel.ERROR, "IOException when getting audio stream");
			return null;
		}

		return stream;
	}

	private static SourceDataLine tryGetLine() {

		DataLine.Info lineInfo = new DataLine.Info(SourceDataLine.class, FORMAT);
		javax.sound.sampled.Mixer.Info[] infos = AudioSystem.getMixerInfo();

		for (javax.sound.sampled.Mixer.Info info: infos) {
			javax.sound.sampled.Mixer mixer = null;
			try {
				mixer = AudioSystem.getMixer(info);
			}
			catch (SecurityException | IllegalArgumentException ignored) {}

			if (mixer == null || !mixer.isLineSupported(lineInfo)) {
				continue;
			}

			SourceDataLine line = null;
			try {
				line = (SourceDataLine)mixer.getLine(lineInfo);

				if (!line.isOpen()) {
					line.open(FORMAT);
				}
			}
			catch (LineUnavailableException | SecurityException ignored) {}

			if (line != null && line.isOpen()) {
				return line;
			}
		}

		return null;
	}
}
