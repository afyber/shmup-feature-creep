package afyber.shmupfeaturecreep.engine.world;

public class SpriteCollision implements Collision {

	private final String spriteName;

	public SpriteCollision(String spriteName) {
		this.spriteName = spriteName;
	}

	@Override
	public CollisionType getType() {
		return CollisionType.SPRITE;
	}

	public String getSpriteName() {
		return spriteName;
	}
}
