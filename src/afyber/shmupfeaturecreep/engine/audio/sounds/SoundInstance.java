package afyber.shmupfeaturecreep.engine.audio.sounds;

import afyber.shmupfeaturecreep.engine.audio.AudioParent;

public class SoundInstance extends AudioParent {

	private final SoundDataReference dataRef;

	public SoundInstance(SoundDataReference dataRef) {
		super();
		playing.set(true);
		this.dataRef = dataRef;
	}

	@Override
	public int[] readFrame() {
		int[] frame = null;
		if (dataRef.getChannels() == 1) {
			frame = new int[1];
			frame[0] = dataRef.readByte(0, bytePos + 1) << 8 | dataRef.readByte(0, bytePos) & 0xFF;
		}
		else if (dataRef.getChannels() == 2) {
			frame = new int[2];
			frame[0] = dataRef.readByte(0, bytePos + 1) << 8 | dataRef.readByte(0, bytePos) & 0xFF;
			frame[1] = dataRef.readByte(1, bytePos + 1) << 8 | dataRef.readByte(1, bytePos) & 0xFF;
		}

		bytePos += 2;

		if (bytePos >= dataRef.getDataLength()) {
			playing.set(false);
		}

		return frame;
	}

	@Override
	public void skipFrames(int frames) {
		bytePos += frames * 2;

		if (bytePos >= dataRef.getDataLength()) {
			playing.set(false);
		}
	}

	@Override
	public int getChannels() {
		return dataRef.getChannels();
	}

	public String getSoundRefName() {
		return dataRef.getName();
	}
}
