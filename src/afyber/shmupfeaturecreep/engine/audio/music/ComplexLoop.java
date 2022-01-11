package afyber.shmupfeaturecreep.engine.audio.music;

/**
 * This class lives up to its stinking name
 *
 * @author afyber
 */
public class ComplexLoop extends LoopParent {

	private final byte[][] dataIntro;
	private final byte[][] dataIntroLoop;
	private final byte[][] dataLoopLoop;

	private final int introChannels;
	private final int introLoopChannels;
	private final int loopLoopChannels;

	public ComplexLoop(byte[][] dataIntro, byte[][] dataIntroLoop, byte[][] dataLoopLoop, int introChannels, int introLoopChannels, int loopLoopChannels) {
		this.dataIntro = dataIntro;
		this.dataIntroLoop = dataIntroLoop;
		this.dataLoopLoop = dataLoopLoop;
		this.introChannels = introChannels;
		this.introLoopChannels = introLoopChannels;
		this.loopLoopChannels = loopLoopChannels;
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
		}
		else if (playingState.get() == 1) {
			retur = readFrameFromArray(dataIntroLoop, introLoopChannels);

			framePos += pitch;

			if (getBytePos() >= dataIntroLoop[0].length) {
				framePos = 0;
				playingState.set(2);
			}
		}
		else if (playingState.get() == 2) {
			retur = readFrameFromArray(dataLoopLoop, loopLoopChannels);

			framePos += pitch;

			if (getBytePos() >= dataLoopLoop[0].length) {
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
			if (getBytePos() >= dataIntroLoop[0].length) {
				framePos -= dataIntroLoop[0].length / 2.0;
				playingState.set(2);
			}
		}
		if (playingState.get() == 2) {
			while (getBytePos() >= dataLoopLoop[0].length) {
				framePos -= dataLoopLoop[0].length / 2.0;
			}
		}

		if (framePos < 0) {
			framePos = 0;
		}
	}

	@Override
	public int getChannels() {
		return playingState.get() == 0 ? introChannels : playingState.get() == 1 ? introLoopChannels : loopLoopChannels;
	}
}
