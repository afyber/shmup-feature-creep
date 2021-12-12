package afyber.shmupfeaturecreep.engine.sound;

import afyber.shmupfeaturecreep.MainClass;
import afyber.shmupfeaturecreep.engine.GeneralUtil;
import afyber.shmupfeaturecreep.engine.errors.SoundsNotDefinedError;
import afyber.shmupfeaturecreep.engine.output.LoggingLevel;

import javax.sound.sampled.*;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;


public class Sound {
	private Sound() {}

	private static boolean ready = false;

	static HashMap<String, SoundParent> allSounds;

	public static final AudioFormat FORMAT = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, 44100, 16, 2, 4, 44100, false);

	public static final AudioFormat MONO_FORMAT = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, 44100, 16, 1, 2, 44100, false);

	private static SourceDataLine soundLine;
	private static Mixer mixer;

	private static SoundUpdateRunner updater;

	public static void init() {
		allSounds = new HashMap<>();

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
				String[] split = line.split(":");
				Sound.registerSound("/sounds/" + split[0], split[1]);
			}
		}
	}

	public static void registerSound(String fileName, String soundName) {
		SoundParent sound = loadAudio(fileName);

		if (sound == null) {
			MainClass.LOGGER.log(LoggingLevel.ERROR, "Registering audio with null sound reference");
			return;
		}

		allSounds.put(soundName, sound);
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
