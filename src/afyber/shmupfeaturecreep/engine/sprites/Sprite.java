package afyber.shmupfeaturecreep.engine.sprites;

public class Sprite {

	private final SpriteSheetRegion[] frames;

	public Sprite(int frames) {
		this.frames = new SpriteSheetRegion[frames];
	}

	// PACKAGE-PRIVATE
	void setFrame(SpriteSheetRegion region, int frame) {
		frames[frame] = region;
	}

	public SpriteSheetRegion getFrame(int index) {
		if (index >= frames.length) {
			index = frames.length - 1;
		}
		return frames[index];
	}

	public int numFrames() {
		return frames.length;
	}
}
