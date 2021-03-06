package afyber.shmupfeaturecreep.engine.rooms;

import afyber.shmupfeaturecreep.engine.screen.Screen;
import afyber.shmupfeaturecreep.engine.world.Collision;
import afyber.shmupfeaturecreep.engine.world.WorldMiddleman;

import java.awt.*;
import java.util.Arrays;

/**
 * The similarities to the-game-creation-software-that-shall-not-be-named are growing stronger.
 * Much stronger.
 * Like really, if you've done anything in *that* software you should recognize this.
 *
 * @author afyber
 */
public class DynamicObject {

	public boolean pauseable = true;

	// NOTE: Accessors are not used BECAUSE it makes the code look more like GML
	// That might sound silly, but this is basically a GameMaker clone anyways
	public String objectName = "";

	public final int instanceID;

	public double x;
	public double y;

	public int depth;
	public boolean visible = true;

	public double imageXScale;
	public double imageYScale;

	public String sprite = "";
	public double spriteIndex = 0;
	public double imageSpeed = 0;

	public Collision collision = null;

	public float xSpeed = 0;
	public float ySpeed = 0;

	public int[] alarm = new int[11];

	public DynamicObject(double x, double y, int depth, int instanceID) {
		this(x, y, depth, 1, 1, instanceID);
	}
	public DynamicObject(double x, double y, int depth, double xScale, double yScale, int instanceID) {
		this.x = x;
		this.y = y;
		this.depth = depth;
		this.imageXScale = xScale;
		this.imageYScale = yScale;
		this.instanceID = instanceID;
		Arrays.fill(alarm, 0);
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

	public void alarm10(WorldMiddleman world) {
		// to override
	}

	// THE METHODS BELOW ARE TO BE USED BY CHILD CLASSES IN THEIR VERSIONS OF THE ABOVE METHODS

	protected final void drawSelf() {
		if (visible) {
			draw(sprite, spriteIndex, x, y, imageXScale, imageYScale);
		}
	}

	protected final void drawSimple(String sprite, double spriteIndex, double x, double y) {
		if (visible) {
			Screen.draw(sprite, spriteIndex, x, y, depth);
		}
	}

	protected final void draw(String sprite, double spriteIndex, double x, double y, double xScale, double yScale) {
		if (visible) {
			Screen.draw(sprite, spriteIndex, x, y, xScale, yScale, depth);
		}
	}

	protected final void drawExtended(String sprite, double spriteIndex, double x, double y, double xScale, double yScale, double alpha) {
		if (visible) {
			Screen.draw(sprite, spriteIndex, x, y, xScale, yScale, depth, alpha);
		}
	}

	protected final void drawText(String message, double x, double y) {
		if (visible) {
			Screen.drawText(message, x, y, depth);
		}
	}

	protected final void drawText(String message, double x, double y, double wrapWidth) {
		if (visible) {
			Screen.drawText(message, x, y, wrapWidth, depth);
		}
	}

	protected final void drawTextExt(String message, double x, double y, double xScale, double yScale, double wrapWidth, double alpha) {
		if (visible) {
			Screen.drawText(message, x, y, xScale, yScale, wrapWidth, depth, alpha);
		}
	}

	protected final void drawTextExtCentered(String message, double x, double y, double xScale, double yScale, double wrapWidth, double alpha) {
		if (visible) {
			Screen.drawText(message, x - Screen.getTextWidth(message, xScale, wrapWidth) / 2, y, xScale, yScale, wrapWidth, depth, alpha);
		}
	}

	protected final void drawRect(double x1, double y1, double x2, double y2, Color color) {
		if (visible) {
			drawRect(x1, y1, x2, y2, color.getRGB());
		}
	}

	protected final void drawRect(double x1, double y1, double x2, double y2, int rgbColor) {
		if (visible) {
			Screen.drawRect(x1, y1, x2, y2, rgbColor, depth);
		}
	}

	protected final void drawRectExt(double x1, double y1, double x2, double y2, int rgbColor, double alpha) {
		if (visible) {
			Screen.drawRect(x1, y1, x2, y2, rgbColor, depth, alpha);
		}
	}
}
