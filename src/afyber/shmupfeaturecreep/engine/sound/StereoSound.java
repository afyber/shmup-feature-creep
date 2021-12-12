package afyber.shmupfeaturecreep.engine.sound;

import java.util.Arrays;

public class StereoSound extends SoundParent {

	private final byte[][] data;

	public StereoSound(byte[][] data) {
		this(data, false, 0);
	}
	public StereoSound(byte[][] data, boolean loop, int loopPos) {
		super(loop, loopPos);
		this.data = new byte[2][];
		for (int channel = 0; channel < 2; channel++) {
			this.data[channel] = Arrays.copyOf(data[channel], data[channel].length);
		}
	}

	@Override
	public int[] readFrame() {
		int[] retur = new int[2];
		retur[0] = data[0][bytePos + 1] << 8 | data[0][bytePos] & 0xFF;
		retur[1] = data[1][bytePos + 1] << 8 | data[1][bytePos] & 0xFF;

		bytePos += 2;

		if (bytePos >= data[0].length) {
			if (loop) {
				bytePos = loopPos;
			}
			else {
				playing = false;
			}
		}

		return retur;
	}

	@Override
	public void skipFrames(int frames) {
		bytePos += frames * 2;
		if (bytePos >= data.length) {
			if (loop) {
				bytePos -= data.length;
			}
			else {
				playing = false;
			}
		}
	}

	@Override
	public int getChannels() {
		return 2;
	}
}
