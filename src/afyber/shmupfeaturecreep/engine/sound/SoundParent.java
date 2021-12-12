package afyber.shmupfeaturecreep.engine.sound;

public abstract class SoundParent {

	protected int bytePos;

	protected boolean loop;
	protected int loopPos;

	protected boolean playing;

	protected double volume;

	protected SoundParent(boolean loop, int loopPos) {
		this.bytePos = 0;
		this.loop = loop;
		this.loopPos = loopPos;
		this.playing = false;
		this.volume = 1.0;
	}

	public abstract int[] readFrame();

	public abstract void skipFrames(int frames);

	public abstract int getChannels();

	public void play() {
		bytePos = 0;
		playing = true;
	}

	public void loop() {
		bytePos = 0;
		loop = true;
		playing = true;
	}

	public void setGain(double gain) {
		this.volume = gain;
	}

	public void setLooping(boolean loop) {
		this.loop = loop;
	}

	public boolean isPlaying() {
		return playing;
	}

	public int getBytePos() {
		return bytePos;
	}
}
