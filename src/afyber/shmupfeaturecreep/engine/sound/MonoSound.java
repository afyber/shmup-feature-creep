package afyber.shmupfeaturecreep.engine.sound;

import java.util.Arrays;

/**
 * ...
 *
 * @author afyber
 */
public class MonoSound extends SoundParent {

	private final byte[] data;

	public MonoSound(byte[] data) {
		this(data, false, 0);
	}
	public MonoSound(byte[] data, boolean loop, int loopPos) {
		super(loop, loopPos);
		this.data = Arrays.copyOf(data, data.length);
	}

	@Override
	public int[] readFrame() {
		int[] retur = new int[1];
		retur[0] = data[bytePos + 1] << 8 | data[bytePos] & 0xFF;
		bytePos += 2;

		if (bytePos >= data.length) {
			if (loop.get()) {
				bytePos = loopPos;
			}
			else {
				playing.set(false);
			}
		}

		return retur;
	}

	@Override
	public void skipFrames(int frames) {
		bytePos += frames * 2;
		if (bytePos >= data.length) {
			if (loop.get()) {
				while (bytePos >= data.length) {
					bytePos -= data.length;
				}
			}
			else {
				playing.set(false);
			}
		}
	}

	@Override
	public int getChannels() {
		return 1;
	}
}