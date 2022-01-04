package afyber.shmupfeaturecreep.engine.world;

public class RectangleCollision implements Collision {

	private final int[] coords;

	public RectangleCollision(int marginLeft, int marginTop, int marginRight, int marginBottom) {
		coords = new int[4];
		coords[0] = marginLeft;
		coords[1] = marginTop;
		coords[2] = marginRight;
		coords[3] = marginBottom;
	}

	@Override
	public CollisionType getType() {
		return CollisionType.RECT;
	}

	public int getMargin(int index) {
		return coords[index];
	}
}
