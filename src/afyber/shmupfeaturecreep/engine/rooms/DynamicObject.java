package afyber.shmupfeaturecreep.engine.rooms;

import afyber.shmupfeaturecreep.engine.Screen;
import afyber.shmupfeaturecreep.engine.world.WorldMiddleman;

import java.util.Arrays;

/**
 * The similarities to the-game-creation-software-that-shall-not-be-named are growing stronger.
 * Much stronger.
 * Like really, if you've done anything in *that* software you should recognize this.
 *
 * @author afyber
 */
public class DynamicObject {

	// NOTE: Accessors are not used BECAUSE it makes the code look more like GML
	// That might sound silly, but this is basically a GameMaker clone anyways
	public String objectName = "";

	public final int instanceID;

	public float x;
	public float y;

	public int depth;
	public boolean visible = true;

	public float imageXScale = 1;
	public float imageYScale = 1;

	public String spriteIndex = "";
	public String collisionIndex = "";

	public float xSpeed = 0;
	public float ySpeed = 0;

	public int[] alarm = new int[10];

	public DynamicObject(float x, float y, int depth, int instanceID) {
		this(x, y, depth, 1, 1, instanceID);
	}
	public DynamicObject(float x, float y, int depth, float xScale, float yScale, int instanceID) {
		this.x = x;
		this.y = y;
		this.depth = depth;
		this.imageXScale = xScale;
		this.imageYScale = yScale;
		this.instanceID = instanceID;
		Arrays.fill(alarm, -1);
	}

	public void create(WorldMiddleman world) {
		// to override
	}

	public void postCreate(WorldMiddleman world) {
		// to override
	}

	public void preUpdate(WorldMiddleman world) {
		// to override
	}

	public void update(WorldMiddleman world) {
		// to override
	}

	public void postUpdate(WorldMiddleman world) {
		// to override
	}

	public void preDraw(WorldMiddleman world) {
		// to override
	}

	public void draw(WorldMiddleman world) {
		drawSelf();
	}

	public void postDraw(WorldMiddleman world) {
		// to override
	}

	public void destroy(WorldMiddleman world) {
		// to override
	}

	public void alarm0(WorldMiddleman world) {
		// to override
	}

	public void alarm1(WorldMiddleman world) {
		// to override
	}

	public void alarm2(WorldMiddleman world) {
		// to override
	}

	public void alarm3(WorldMiddleman world) {
		// to override
	}

	public void alarm4(WorldMiddleman world) {
		// to override
	}

	public void alarm5(WorldMiddleman world) {
		// to override
	}

	public void alarm6(WorldMiddleman world) {
		// to override
	}

	public void alarm7(WorldMiddleman world) {
		// to override
	}

	public void alarm8(WorldMiddleman world) {
		// to override
	}

	public void alarm9(WorldMiddleman world) {
		// to override
	}

	// THE METHODS BELOW ARE TO BE USED BY CHILD CLASSES IN THEIR VERSIONS OF THE ABOVE METHODS

	protected final void drawSelf() {
		if (visible) {
			draw(spriteIndex, x, y, imageXScale, imageYScale);
		}
	}

	protected final void drawSimple(String spriteIndex, float x, float y) {
		if (visible) {
			Screen.draw(spriteIndex, x, y, depth);
		}
	}

	protected final void draw(String spriteIndex, float x, float y, float xScale, float yScale) {
		if (visible) {
			Screen.draw(spriteIndex, x, y, xScale, yScale, depth);
		}
	}

	protected final void drawExtended(String spriteIndex, float x, float y, float xScale, float yScale, float alpha) {
		if (visible) {
			Screen.draw(spriteIndex, x, y, xScale, yScale, depth, alpha);
		}
	}
}
