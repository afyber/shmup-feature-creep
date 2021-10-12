package afyber.shmupfeaturecreep.engine.rooms;

import afyber.shmupfeaturecreep.engine.Screen;

import java.util.Arrays;

/**
 * The similarities to the-game-creation-software-that-shall-not-be-named are growing stronger.
 * Much stronger.
 * Like really, if you've done anything in *that* software you should recognize this.
 *
 * @author afyber
 */
public class DynamicObject {

	protected final int instanceID;

	protected float x;
	protected float y;

	protected int depth;
	protected boolean visible = true;

	protected float imageXScale = 1;
	protected float imageYScale = 1;

	protected String spriteIndex = "sprite_1";
	protected String collisionIndex = "sprite_1";

	protected int[] alarm = new int[10];

	public DynamicObject(float x, float y, int depth, int instanceID) {
		this.x = x;
		this.y = y;
		this.depth = depth;
		this.instanceID = instanceID;
		Arrays.fill(alarm, -1);
	}

	public void create() {
		// to override
	}

	public void postCreate() {
		// to override
	}

	public void preUpdate() {
		// to override
	}

	public void update() {
		// to override
	}

	public void postUpdate() {
		// to override
	}

	public void preDraw() {
		// to override
	}

	public void draw() {
		drawSelf();
	}

	public void postDraw() {
		// to override
	}

	public void preDestroy() {
		// to override
	}

	public void destroy() {
		// to override
	}

	public void alarm1() {
		// to override
	}

	public void alarm2() {
		// to override
	}

	public void alarm3() {
		// to override
	}

	public void alarm4() {
		// to override
	}

	public void alarm5() {
		// to override
	}

	public void alarm6() {
		// to override
	}

	public void alarm7() {
		// to override
	}

	public void alarm8() {
		// to override
	}

	public void alarm9() {
		// to override
	}

	public void alarm10() {
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

	// THE METHODS BELOW ARE TO BE USED (mostly) BY WORLD TO SET/GET IMPORTANT INFORMATION

	public final void setX(float x) {
		this.x = x;
	}

	public final void setY(float y) {
		this.y = y;
	}

	public final void setDepth(int depth) {
		this.depth = depth;
	}

	public final void setVisible(boolean visible) {
		this.visible = visible;
	}

	public final void setSpriteIndex(String spriteIndex) {
		this.spriteIndex = spriteIndex;
	}

	public final void setAlarm(int alarm, int value) {
		this.alarm[alarm] = value;
	}

	public final float getX() {
		return x;
	}

	public final float getY() {
		return y;
	}

	public final int getDepth() {
		return depth;
	}

	public final boolean isVisible() {
		return visible;
	}

	public final String getSpriteIndex() {
		return spriteIndex;
	}

	public final int getAlarms(int alarm) {
		return this.alarm[alarm];
	}

	public final int getInstanceID() {
		return instanceID;
	}
}
