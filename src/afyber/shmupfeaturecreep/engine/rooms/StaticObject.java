package afyber.shmupfeaturecreep.engine.rooms;

import afyber.shmupfeaturecreep.engine.screen.Screen;

/**
 * Once again we have here a class that does things, but this time while standing still not doing things
 *
 * @author afyber
 */
public record StaticObject(String spriteIndex, int spriteFrame, int x, int y) {
	public void draw() {
		Screen.draw(spriteIndex(), spriteFrame(), x(), y(), -1000);
	}
}
