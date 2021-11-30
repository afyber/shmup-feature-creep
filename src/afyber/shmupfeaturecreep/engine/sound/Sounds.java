package afyber.shmupfeaturecreep.engine.sound;

import afyber.shmupfeaturecreep.MainClass;
import afyber.shmupfeaturecreep.engine.GeneralUtil;
import afyber.shmupfeaturecreep.engine.errors.JavaOpenALError;
import afyber.shmupfeaturecreep.engine.errors.SoundNotDefinedError;
import afyber.shmupfeaturecreep.engine.errors.SoundsNotDefinedError;
import afyber.shmupfeaturecreep.engine.output.LoggingLevel;
import com.jogamp.openal.AL;
import com.jogamp.openal.ALFactory;
import com.jogamp.openal.util.ALut;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

public class Sounds {
	private Sounds() {}

	private static final AL al = ALFactory.getAL();

	// the integer here is sort of a pointer, as it is the index of the sound in the source and buffer arrays
	private static final HashMap<String, Integer> allSounds = new HashMap<>();

	private static int[] buffer;
	private static int[] source;

	public static void playSound(String soundName) {
		if (allSounds.containsKey(soundName)) {
			soundSetLoop(soundName, false);
			al.alSourcePlay(source[allSounds.get(soundName)]);
		}
		else {
			throw new SoundNotDefinedError();
		}
	}

	public static void loopSound(String soundName) {
		if (allSounds.containsKey(soundName)) {
			soundSetLoop(soundName, true);
			al.alSourcePlay(source[allSounds.get(soundName)]);
		}
		else {
			throw new SoundNotDefinedError();
		}
	}

	public static void pauseSound(String soundName) {
		if (allSounds.containsKey(soundName)) {
			al.alSourcePause(source[allSounds.get(soundName)]);
		}
		else {
			throw new SoundNotDefinedError();
		}
	}

	public static void stopSound(String soundName) {
		if (allSounds.containsKey(soundName)) {
			al.alSourceStop(source[allSounds.get(soundName)]);
		}
		else {
			throw new SoundNotDefinedError();
		}
	}

	public static void soundSetGain(String soundName, float gain) {
		if (allSounds.containsKey(soundName)) {
			al.alSourcef(source[allSounds.get(soundName)], AL.AL_GAIN, gain);
		}
		else {
			throw new SoundNotDefinedError();
		}
	}

	public static void soundSetPitch(String soundName, float pitch) {
		if (allSounds.containsKey(soundName)) {
			al.alSourcef(source[allSounds.get(soundName)], AL.AL_PITCH, pitch);
		}
		else {
			throw new SoundNotDefinedError();
		}
	}

	public static void soundSetPanning(String soundName, float panning) {
		if (allSounds.containsKey(soundName)) {
			if (panning < -1) {
				panning = -1;
			}
			else if (panning > 1) {
				panning = 1;
			}
			// TODO: use circular instead of triangular
			al.alSourcefv(source[allSounds.get(soundName)], AL.AL_POSITION, new float[]{ panning, 0.0f, 1.0f - Math.abs(panning) }, 0);
		}
		else {
			throw new SoundNotDefinedError();
		}
	}

	private static void soundSetLoop(String soundName, boolean val) {
		if (allSounds.containsKey(soundName)) {
			al.alSourcei(source[allSounds.get(soundName)], AL.AL_LOOPING, val ? AL.AL_TRUE : AL.AL_FALSE);
		}
		else {
			throw new SoundNotDefinedError();
		}
	}

	public static void setupSound() {
		ALut.alutInit();
		al.alGetError();

		setListenerValues();
	}

	public static void closeSound() {
		ALut.alutExit();
	}

	public static void loadSounds() {
		String[] soundsData;
		try  {
			soundsData = GeneralUtil.readResourceToString("/sounds.txt").split("\r\n");
		}
		catch (IOException e) {
			MainClass.LOGGER.log(LoggingLevel.ERROR, "Could not load sounds.txt:", e);
			throw new SoundsNotDefinedError();
		}

		int i = 0;

		for (String sound: soundsData) {
			allSounds.put(sound, i);
			i++;
		}

		buffer = new int[i];

		al.alGenBuffers(i, buffer, 0);
		if (al.alGetError() != AL.AL_NO_ERROR) {
			MainClass.LOGGER.log(LoggingLevel.ERROR, "Could not generate JOAL audio buffers");
			throw new JavaOpenALError();
		}

		source = new int[i];

		al.alGenSources(i, source, 0);
		if (al.alGetError() != AL.AL_NO_ERROR) {
			MainClass.LOGGER.log(LoggingLevel.ERROR, "Could not generate JOAL audio sources");
			throw new JavaOpenALError();
		}

		for (Map.Entry<String, Integer> entry: allSounds.entrySet()) {
			makeLinkedBufferAndSource("/sounds/" + entry.getKey(), entry.getValue());
		}
	}

	private static void makeLinkedBufferAndSource(String fileName, int num) {
		try (InputStream stream = Sounds.class.getResourceAsStream(fileName)) {
			int[] format = new int[1];
			int[] size = new int[1];
			ByteBuffer[] data = new ByteBuffer[1];
			int[] freq = new int[1];
			int[] loop = new int[1];

			ALut.alutLoadWAVFile(stream, format, data, size, freq, loop);

			al.alBufferData(buffer[num], format[0], data[0], size[0], freq[0]);

			al.alSourcei(source[num], AL.AL_BUFFER, buffer[num]);
			al.alSourcef(source[num], AL.AL_PITCH, 1.0f);
			al.alSourcef(source[num], AL.AL_GAIN, 1.0f);
			al.alSourcefv(source[num], AL.AL_POSITION, new float[]{ 0.0f, 0.0f, 1.0f }, 0);
			al.alSourcefv(source[num], AL.AL_VELOCITY, new float[]{ 0.0f, 0.0f, 0.0f }, 0);
			al.alSourcei(source[num], AL.AL_LOOPING, AL.AL_FALSE);
		}
		catch (IOException e) {
			MainClass.LOGGER.log(LoggingLevel.ERROR, "Could not load audio file \"" + fileName + "\"");
			throw new SoundNotDefinedError();
		}
	}

	private static void makeLinkedIntroAndLoopBuffersAndSource(String fileName1, String fileName2, int numOfFirst) {
		try (InputStream stream1 = Sounds.class.getResourceAsStream(fileName1)) {
			try (InputStream stream2 = Sounds.class.getResourceAsStream(fileName2)) {
				int[] format = new int[1];
				int[] size = new int[1];
				ByteBuffer[] data = new ByteBuffer[1];
				int[] freq = new int[1];
				int[] loop = new int[1];

				ALut.alutLoadWAVFile(stream1, format, data, size, freq, loop);

				al.alBufferData(buffer[numOfFirst], format[0], data[0], size[0], freq[0]);

				format = new int[1];
				size = new int[1];
				data = new ByteBuffer[1];
				freq = new int[1];
				loop = new int[1];

				ALut.alutLoadWAVFile(stream2, format, data, size, freq, loop);

				al.alBufferData(buffer[numOfFirst + 1], format[0], data[0], size[0], freq[0]);

			}
			catch (IOException e) {
				MainClass.LOGGER.log(LoggingLevel.ERROR, "Could not load audio file \"" + fileName2 + "\"");
				throw new SoundNotDefinedError();
			}
		}
		catch (IOException e) {
			MainClass.LOGGER.log(LoggingLevel.ERROR, "Could not load audio file \"" + fileName1 + "\"");
			throw new SoundNotDefinedError();
		}
	}

	private static void setListenerValues() {
		al.alListenerfv(AL.AL_POSITION, new float[]{ 0.0f, 0.0f, 0.0f }, 0);
		al.alListenerfv(AL.AL_VELOCITY, new float[]{ 0.0f, 0.0f, 0.0f }, 0);
		al.alListenerfv(AL.AL_ORIENTATION, new float[]{ 0.0f, 0.0f, -1.0f, 0.0f, 1.0f, 0.0f }, 0);
	}
}
