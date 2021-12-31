package afyber.shmupfeaturecreep.engine.audio.music;

import afyber.shmupfeaturecreep.engine.audio.AudioParent;

import java.util.concurrent.atomic.AtomicBoolean;

public abstract class MusicParent extends AudioParent {

	protected AtomicBoolean loop;

	protected MusicParent() {
		super();
		loop = new AtomicBoolean(false);
	}

	public void setLoop(boolean loop) {
		this.loop.set(loop);
	}

	public boolean getLoop() {
		return this.loop.get();
	}
}
