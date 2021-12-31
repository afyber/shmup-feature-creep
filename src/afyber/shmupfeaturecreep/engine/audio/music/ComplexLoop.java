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

			bytePos += 2;

			if (bytePos >= dataIntro[0].length) {
				bytePos = 0;
				playingState.set(1);
			}
		}
		else if (playingState.get() == 1) {
			retur = readFrameFromArray(dataIntroLoop, introLoopChannels);

			bytePos += 2;

			if (bytePos >= dataIntroLoop[0].length) {
				bytePos = 0;
				playingState.set(2);
			}
		}
		else if (playingState.get() == 2) {
			retur = readFrameFromArray(dataLoopLoop, loopLoopChannels);

			bytePos += 2;

			if (bytePos >= dataLoopLoop[0].length) {
				bytePos = 0;
			}
		}

		return retur;
	}

	@Override
	public void skipFrames(int frames) {
		bytePos += frames;

		if (playingState.get() == 0) {
			if (bytePos >= dataIntro[0].length) {
				bytePos -= dataIntro[0].length;
				playingState.set(1);
			}
		}
		if (playingState.get() == 1) {
			if (bytePos >= dataIntroLoop[0].length) {
				bytePos -= dataIntroLoop[0].length;
				playingState.set(2);
			}
		}
		if (playingState.get() == 2) {
			while (bytePos >= dataLoopLoop[0].length) {
				bytePos -= dataLoopLoop[0].length;
			}
		}
	}

	@Override
	public int getChannels() {
		return playingState.get() == 0 ? introChannels : playingState.get() == 1 ? introLoopChannels : loopLoopChannels;
	}
}
