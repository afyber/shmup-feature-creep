package afyber.shmupfeaturecreep.engine.rooms;

import afyber.shmupfeaturecreep.engine.Screen;

/**
 * Once again we have here a class that does things, but this time while standing still not doing things
 *
 * @author afyber
 */
public record StaticObject(String spriteIndex, int x, int y) {
	public void draw() {
		Screen.draw(spriteIndex(), x(), y(), -1000);
	}
}
