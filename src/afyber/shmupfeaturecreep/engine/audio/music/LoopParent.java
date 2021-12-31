package afyber.shmupfeaturecreep.engine.audio.music;

import afyber.shmupfeaturecreep.engine.audio.AudioParent;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Loops are different from other sounds because:
 * 1. They have multiple audio data in them
 * 2. They can handle both mono and stereo, for convenience
 *
 * @author afyber
 */
public abstract class LoopParent extends AudioParent {

	protected AtomicInteger playingState;

	protected LoopParent() {
		super();
		this.playingState = new AtomicInteger(0);
		this.volume = 1.0;
	}

	public void play() {
		playingState.set(0);
		bytePos = 0;
		playing.set(true);
	}

	public void stop() {
		playingState.set(0);
		bytePos = 0;
		playing.set(false);
	}
}
