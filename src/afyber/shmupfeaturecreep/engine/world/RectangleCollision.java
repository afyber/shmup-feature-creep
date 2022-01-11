package afyber.shmupfeaturecreep.engine.world;

public class RectangleCollision implements Collision {

	private final int[] margins;

	public RectangleCollision(int marginLeft, int marginTop, int marginRight, int marginBottom) {
		margins = new int[4];
		margins[0] = marginLeft;
		margins[1] = marginTop;
		margins[2] = marginRight;
		margins[3] = marginBottom;
	}

	@Override
	public Type getType() {
		return Type.RECT;
	}

	public int getMargin(int index) {
		return margins[index];
	}
}
