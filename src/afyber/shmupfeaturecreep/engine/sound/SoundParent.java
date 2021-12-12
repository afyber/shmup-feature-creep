package afyber.shmupfeaturecreep.engine.sound;

import java.util.concurrent.atomic.AtomicBoolean;

public abstract class SoundParent {

	protected int bytePos;

	protected AtomicBoolean loop;
	protected int loopPos;

	protected AtomicBoolean playing;

	protected double volume;

	protected SoundParent(boolean loop, int loopPos) {
		this.bytePos = 0;
		this.loop = new AtomicBoolean();
		this.loop.set(loop);
		this.loopPos = loopPos;
		this.playing = new AtomicBoolean();
		this.volume = 1.0;
	}

	public abstract int[] readFrame();

	public abstract void skipFrames(int frames);

	public abstract int getChannels();

	public void play() {
		bytePos = 0;
		playing.set(true);
	}

	public void pause() {
		playing.set(false);
	}

	public void resume() {
		playing.set(true);
	}

	public void loop() {
		bytePos = 0;
		loop.set(true);
		playing.set(true);
	}

	public void setGain(double gain) {
		this.volume = gain;
	}

	public void setLooping(boolean loop) {
		this.loop.set(loop);
	}

	public boolean isPlaying() {
		return playing.get();
	}

	public int getBytePos() {
		return bytePos;
	}
}
