package afyber.shmupfeaturecreep.engine.world;

public class SpriteCollision implements Collision {

	private final String spriteName;

	public SpriteCollision(String spriteName) {
		this.spriteName = spriteName;
	}

	@Override
	public Type getType() {
		return Type.SPRITE;
	}

	public String getSpriteName() {
		return spriteName;
	}
}
