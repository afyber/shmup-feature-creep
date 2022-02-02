package afyber.shmupfeaturecreep.engine.audio.music;

import afyber.shmupfeaturecreep.engine.audio.AudioParent;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Loops are different from other music because:
 * 1. They have multiple audio data in them
 * 2. They automatically loop, no need to call loopMusic()
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

	@Override
	public void play() {
		playing.set(true);
		playingState.set(0);
		framePos = 0;
	}

	@Override
	public void stop() {
		playing.set(false);
		playingState.set(0);
		framePos = 0;
	}
}
