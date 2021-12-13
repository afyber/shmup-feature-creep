package afyber.shmupfeaturecreep.engine.sound;

public class BasicLoop extends LoopParent {

	private final byte[][] dataIntro;
	private final byte[][] dataLoop;

	private final int introChannels;
	private final int loopChannels;

	public BasicLoop(byte[] dataIntro, byte[] dataLoop) {
		this.dataIntro = new byte[][]{ dataIntro, null };
		this.dataLoop = new byte[][]{ dataLoop, null };
		this.introChannels = 1;
		this.loopChannels = 1;
	}
	public BasicLoop(byte[][] dataIntro, byte[] dataLoop) {
		this.dataIntro = dataIntro;
		this.dataLoop = new byte[][]{ dataLoop, null };
		this.introChannels = 2;
		this.loopChannels = 1;
	}
	public BasicLoop(byte[] dataIntro, byte[][] dataLoop) {
		this.dataIntro = new byte[][]{ dataIntro, null };
		this.dataLoop = dataLoop;
		this.introChannels = 1;
		this.loopChannels = 2;
	}
	public BasicLoop(byte[][] dataIntro, byte[][] dataLoop) {
		this.dataIntro = dataIntro;
		this.dataLoop = dataLoop;
		this.introChannels = 2;
		this.loopChannels = 2;
	}

	@Override
	public void play() {
		playingState.set(0);
		bytePos = 0;
		playing.set(true);
	}

	@Override
	public void stop() {
		playingState.set(0);
		bytePos = 0;
		playing.set(false);
	}

	@Override
	public int[] readFrame() {
		int[] retur = null;
		if (playingState.get() == 0) {
			if (introChannels == 1) {
				retur = new int[1];
				retur[0] = dataIntro[0][bytePos + 1] << 8 | dataIntro[0][bytePos] & 0xFF;
			} else if (introChannels == 2) {
				retur = new int[2];
				retur[0] = dataIntro[0][bytePos + 1] << 8 | dataIntro[0][bytePos] & 0xFF;
				retur[1] = dataIntro[1][bytePos + 1] << 8 | dataIntro[1][bytePos] & 0xFF;
			}

			bytePos += 2;

			if (bytePos >= dataIntro[0].length) {
				bytePos = 0;
				playingState.set(1);
			}
		} else if (playingState.get() == 1) {
			if (loopChannels == 1) {
				retur = new int[1];
				retur[0] = dataLoop[0][bytePos + 1] << 8 | dataLoop[0][bytePos] & 0xFF;
			} else if (loopChannels == 2) {
				retur = new int[2];
				retur[0] = dataLoop[0][bytePos + 1] << 8 | dataLoop[0][bytePos] & 0xFF;
				retur[1] = dataLoop[1][bytePos + 1] << 8 | dataLoop[1][bytePos] & 0xFF;
			}

			bytePos += 2;

			if (bytePos >= dataLoop[0].length) {
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
