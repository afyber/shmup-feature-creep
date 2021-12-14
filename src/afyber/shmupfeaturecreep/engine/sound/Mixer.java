package afyber.shmupfeaturecreep.engine.sound;

public class Mixer {

	private double globalVolume;

	public Mixer() {
		globalVolume = 1.0;
	}

	public synchronized void setGlobalVolume(double volume) {
		this.globalVolume = volume;
	}

	public synchronized int read(byte[] data, int length) {

		boolean readBytes = true;

		int numBytesRead = 0;

		for (int i = 0; i < length && readBytes; i += 4) {
			readBytes = false;

			double leftVal = 0.0;
			double rightVal = 0.0;

			for (SoundParent sound: Sound.allSounds.values()) {
				if (sound.isPlaying()) {
					double volume = globalVolume * sound.getGain();

					if (sound.getChannels() == 1) {
						int[] val = sound.readFrame();
						double currVal = val[0] * volume;

						leftVal += currVal;
						rightVal += currVal;

						readBytes = true;
					} else if (sound.getChannels() == 2) {
						int[] vals = sound.readFrame();
						double currLeft = vals[0] * volume;
						double currRight = vals[1] * volume;

						leftVal += currLeft;
						rightVal += currRight;

						readBytes = true;
					}
				}
			}
			for (LoopParent loop: Sound.allLoops.values()) {
				if (loop.isPlaying()) {
					double volume = globalVolume * loop.getGain();

					if (loop.getChannels() == 1) {
						int[] val = loop.readFrame();
						double currVal = val[0] * volume;

						leftVal += currVal;
						rightVal += currVal;

						readBytes = true;
					} else if (loop.getChannels() == 2) {
						int[] vals = loop.readFrame();
						double currLeft = vals[0] * volume;
						double currRight = vals[1] * volume;

						leftVal += currLeft;
						rightVal += currRight;

						readBytes = true;
					}
				}
			}

			if (readBytes) {
				int finalLeftVal = (int)leftVal;
				int finalRightVal = (int)rightVal;
				finalLeftVal = Math.max(Math.min(finalLeftVal, Short.MAX_VALUE), Short.MIN_VALUE);
				finalRightVal = Math.max(Math.min(finalRightVal, Short.MAX_VALUE), Short.MIN_VALUE);

				data[i + 1] = (byte)((finalLeftVal >> 8) & 0xFF);
				data[i] = (byte)(finalLeftVal & 0xFF);

				data[i + 3] = (byte)((finalRightVal >> 8) & 0xFF);
				data[i + 2] = (byte)(finalRightVal & 0xFF);

				numBytesRead += 4;
			}
		}

		return numBytesRead;
	}

	public synchronized void skipFrames(int frames) {
		for (SoundParent sound: Sound.allSounds.values()) {
			if (sound.isPlaying()) {
				sound.skipFrames(frames);
			}
		}
	}
}
