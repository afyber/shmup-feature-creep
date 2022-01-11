package afyber.shmupfeaturecreep.engine.audio.music;

/**
 * "Basic" is going a little far
 *
 * @author afyber
 */
public class BasicLoop extends LoopParent {

	private final byte[][] dataIntro;
	private final byte[][] dataLoop;

	private final int introChannels;
	private final int loopChannels;

	public BasicLoop(byte[][] dataIntro, byte[][] dataLoop, int introChannels, int loopChannels) {
		this.dataIntro = dataIntro;
		this.dataLoop = dataLoop;
		this.introChannels = introChannels;
		this.loopChannels = loopChannels;
	}

	@Override
	public int[] readFrame() {
		int[] retur = null;
		if (playingState.get() == 0) {
			retur = readFrameFromArray(dataIntro, introChannels);

			framePos += pitch;

			if (getBytePos() >= dataIntro[0].length) {
				framePos = 0;
				playingState.set(1);
			}
		} else if (playingState.get() == 1) {
			retur = readFrameFromArray(dataLoop, loopChannels);

			framePos += pitch;

			if (getBytePos() >= dataLoop[0].length) {
				framePos = 0;
			}
		}

		return retur;
	}

	@Override
	public void skipFrames(int frames) {
		framePos += frames;

		if (playingState.get() == 0) {
			if (getBytePos() >= dataIntro[0].length) {
				framePos -= dataIntro[0].length / 2.0;
				playingState.set(1);
			}
		}
		if (playingState.get() == 1) {
			while (getBytePos() >= dataLoop[0].length) {
				framePos -= dataLoop[0].length / 2.0;
			}
		}

		if (framePos < 0) {
			framePos = 0;
		}
	}

	@Override
	public int getChannels() {
		return playingState.get() == 0 ? introChannels : loopChannels;
	}
}
