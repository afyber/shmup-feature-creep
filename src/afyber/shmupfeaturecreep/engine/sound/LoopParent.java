package afyber.shmupfeaturecreep.engine.sound;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Loops are different from other sounds because:
 * 1. They have multiple audio data in them
 * 2. They can handle both mono and stereo, for convenience
 */
public abstract class LoopParent {

	protected int bytePos;

	protected AtomicInteger playingState;
	protected AtomicBoolean playing;

	protected double volume;

	protected LoopParent() {
		this.bytePos = 0;
		this.playing = new AtomicBoolean();
		this.playingState = new AtomicInteger(0);
		this.volume = 1.0;
	}

	public abstract int[] readFrame();

	public abstract void skipFrames(int frames);

	public abstract int getChannels();

	public abstract void play();

	public abstract void stop();

	public void pause() {
		playing.set(false);
	}

	public void resume() {
		playing.set(true);
	}

	public void setGain(double gain) {
		this.volume = gain;
	}

	public double getGain() {
		return volume;
	}

	public boolean isPlaying() {
		return playing.get();
	}

	public int getBytePos() {
		return bytePos;
	}
}
