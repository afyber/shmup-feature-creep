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

			bytePos += 2;

			if (bytePos >= dataIntro[0].length) {
				bytePos = 0;
				playingState.set(1);
			}
		} else if (playingState.get() == 1) {
			retur = readFrameFromArray(dataLoop, loopChannels);

			bytePos += 2;

			if (bytePos >= dataLoop[0].length) {
				bytePos = 0;
			}
		}

		return retur;
	}

	@Override
	public void skipFrames(int frames) {
		bytePos += frames * 2;

		if (playingState.get() == 0) {
			if (bytePos >= dataIntro[0].length) {
				bytePos -= dataIntro[0].length;
				playingState.set(1);
			}
		}
		if (playingState.get() == 1) {
			while (bytePos >= dataLoop[0].length) {
				bytePos -= dataLoop[0].length;
			}
		}
	}

	@Override
	public int getChannels() {
		return playingState.get() == 0 ? introChannels : loopChannels;
	}
}
