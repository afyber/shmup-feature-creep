package afyber.shmupfeaturecreep.engine.sound;

import afyber.shmupfeaturecreep.MainClass;
import afyber.shmupfeaturecreep.engine.GeneralUtil;
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

	static HashMap<String, SoundParent> allSounds = new HashMap<>();
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
			updaterThread.setDaemon(true);
			updaterThread.setPriority(Thread.MAX_PRIORITY);
		}
		catch (Exception e) {}

		updaterThread.start();

		ready = true;
	}

	public static void shutdown() {
		updater.stop();
		updater = null;
		soundLine.stop();
		soundLine.flush();
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
				else {
					String[] split = line.split(":");
					Sound.registerSound("/sounds/" + split[0], split[1]);
				}
			}
		}
	}

	public static void registerSound(String fileName, String soundName) {
		SoundParent sound = loadAudio(fileName);

		if (sound == null) {
			MainClass.LOGGER.log(LoggingLevel.ERROR, "Registering sound with null sound reference");
			return;
		}

		allSounds.put(soundName, sound);
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
		if (gain < 0) {
			gain = 0;
		}
		else if (gain > 1.0) {
			gain = 1.0;
		}

		mixer.setGlobalVolume(gain);
	}

	public static void playSound(String soundName) {
		if (allSounds.containsKey(soundName)) {
			allSounds.get(soundName).play();
		}
		if (allLoops.containsKey(soundName)) {
			allLoops.get(soundName).play();
		}
	}

	public static void stopSound(String soundName) {
		if (allSounds.containsKey(soundName)) {
			allSounds.get(soundName).stop();
		}
		if (allLoops.containsKey(soundName)) {
			allLoops.get(soundName).stop();
		}
	}

	public static void pauseSound(String soundName) {
		if (allSounds.containsKey(soundName)) {
			allSounds.get(soundName).pause();
		}
		if (allLoops.containsKey(soundName)) {
			allLoops.get(soundName).pause();
		}
	}

	public static void resumeSound(String soundName) {
		if (allSounds.containsKey(soundName)) {
			allSounds.get(soundName).resume();
		}
		if (allLoops.containsKey(soundName)) {
			allLoops.get(soundName).resume();
		}
	}

	public static void loopSound(String soundName) {
		if (allSounds.containsKey(soundName)) {
			allSounds.get(soundName).loop();
		}
	}

	public static void setSoundGain(String soundName, double gain) {
		if (allSounds.containsKey(soundName)) {
			if (gain < 0) {
				gain = 0;
			} else if (gain > 1.0) {
				gain = 1.0;
			}

			allSounds.get(soundName).setGain(gain);
		}
	}

	public static SoundParent loadAudio(String fileName) {
		URL url = MainClass.class.getResource(fileName);

		if (url == null) {
			MainClass.LOGGER.log(LoggingLevel.ERROR, "URL was null when loading audio");
			return null;
		}

		return loadAudio(url);
	}

	public static SoundParent loadAudio(URL url) {
		AudioInputStream stream = getValidAudioInputStream(url);

		if (stream == null) {
			MainClass.LOGGER.log(LoggingLevel.ERROR, "Couldn't get correctly formatted audio stream when loading audio");
			return null;
		}

		int numChannels = stream.getFormat().getChannels();

		if (numChannels == 1) {
			byte[] data = readAllBytesMono(stream);

			return new MonoSound(data);
		}
		else if (numChannels == 2) {
			byte[][] data = readAllBytesStereo(stream);

			return new StereoSound(data);
		}

		return null;
	}

	public static LoopParent loadBasicLoop(String introFileName, String loopFileName) {
		URL url1 = MainClass.class.getResource(introFileName);
		URL url2 = MainClass.class.getResource(loopFileName);

		if (url1 == null || url2 == null) {
			MainClass.LOGGER.log(LoggingLevel.ERROR, "Attempting to load loop with null file reference");
			return null;
		}

		return loadBasicLoop(url1, url2);
	}

	public static LoopParent loadBasicLoop(URL introFile, URL loopFile) {
		AudioInputStream stream1 = getValidAudioInputStream(introFile);
		AudioInputStream stream2 = getValidAudioInputStream(loopFile);

		if (stream1 == null || stream2 == null) {
			MainClass.LOGGER.log(LoggingLevel.ERROR, "Attempting to load loop with null audio stream");
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

	public static LoopParent loadComplexLoop(String introFileName, String introLoopFileName, String loopLoopFileName) {
		URL url1 = MainClass.class.getResource(introFileName);
		URL url2 = MainClass.class.getResource(introLoopFileName);
		URL url3 = MainClass.class.getResource(loopLoopFileName);

		if (url1 == null || url2 == null || url3 == null) {
			MainClass.LOGGER.log(LoggingLevel.ERROR, "Attempting to load loop with null file reference");
			return null;
		}

		return loadComplexLoop(url1, url2, url3);
	}

	public static LoopParent loadComplexLoop(URL introFile, URL introLoopFile, URL loopLoopFile) {
		AudioInputStream introStream = getValidAudioInputStream(introFile);
		AudioInputStream introLoopStream = getValidAudioInputStream(introLoopFile);
		AudioInputStream loopLoopStream = getValidAudioInputStream(loopLoopFile);

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
		try {
			data = getBytes(stream);
		}
		catch (IOException e) {
			MainClass.LOGGER.log(LoggingLevel.ERROR, "IOException reading bytes from audio stream", e);
		}
		finally {
			try {
				stream.close();
			}
			catch (IOException e) {}
		}

		return data;
	}

	private static byte[][] readAllBytesStereo(AudioInputStream stream) {
		byte[][] data = null;
		try {
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
		}
		catch (IOException e) {
			MainClass.LOGGER.log(LoggingLevel.ERROR, "IOException reading bytes from audio stream", e);
		}
		finally {
			try {
				stream.close();
			}
			catch (IOException e) {}
		}

		return data;
	}

	private static byte[] getBytes(AudioInputStream stream) throws IOException {
		int bufferSize = (int)FORMAT.getSampleRate() * FORMAT.getFrameSize();
		byte[] buffer = new byte[bufferSize];
		ByteList list = new ByteList();
		int numRead = 0;
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
			catch (SecurityException e) {
				// just copying from TinySound
			}
			catch (IllegalArgumentException e) {
				// same
			}

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
			catch (LineUnavailableException e) {
				// things happen
			}
			catch (SecurityException e) {
				// they also happen
			}

			if (line != null && line.isOpen()) {
				return line;
			}
		}

		return null;
	}
}