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

		framePos += pitch;

		if (getBytePos() >= data[0].length) {
			framePos = 0;
			if (!loop.get()) {
				playing.set(false);
			}
		}

		return retur;
	}

	@Override
	public void skipFrames(int frames) {
		framePos += frames;

		if (getBytePos() > data[0].length) {
			if (loop.get()) {
				do {
					framePos -= data[0].length / 2.0;
				} while(getBytePos() > data[0].length);

				if (framePos < 0) {
					framePos = 0;
				}
			}
			else {
				framePos = 0;
				playing.set(false);
			}
		}
	}

	@Override
	public int getChannels() {
		return channels;
	}
}
