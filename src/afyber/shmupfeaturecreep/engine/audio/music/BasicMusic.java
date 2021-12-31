package afyber.shmupfeaturecreep.engine.audio.music;

public class BasicMusic extends MusicParent {

	private final byte[][] data;
	private final int channels;

	public BasicMusic(byte[][] data, int channels) {
		super();
		this.data = data;
		this.channels = channels;
	}

	@Override
	public int[] readFrame() {
		int[] retur = readFrameFromArray(data, channels);

		bytePos += 2;

		if (bytePos >= data[0].length) {
			if (loop.get()) {
				bytePos = 0;
			}
			else {
				bytePos = 0;
				playing.set(false);
			}
		}

		return retur;
	}

	@Override
	public void skipFrames(int frames) {
		bytePos += frames * 2;

		if (bytePos > data[0].length) {
			if (loop.get()) {
				do {
					bytePos -= data[0].length;
				} while(bytePos > data[0].length);
			}
			else {
				bytePos = 0;
				playing.set(false);
			}
		}
	}

	@Override
	public int getChannels() {
		return channels;
	}
}
