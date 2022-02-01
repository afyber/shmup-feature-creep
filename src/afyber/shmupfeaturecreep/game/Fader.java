package afyber.shmupfeaturecreep.game;

import afyber.shmupfeaturecreep.Game;
import afyber.shmupfeaturecreep.engine.rooms.DynamicObject;
import afyber.shmupfeaturecreep.engine.world.WorldMiddleman;

public class Fader extends DynamicObject {

	public int rgbColor = 0xffffff;
	public int time = 120;
	private int timer = 0;

	public Fader(double x, double y, int depth, int instanceID) {
		super(x, y, depth, instanceID);
		objectName = "fader";
	}

	@Override
	public void draw(WorldMiddleman world) {
		drawRectExt(0, 0, Game.WINDOW_WIDTH - 1, Game.WINDOW_HEIGHT - 1, rgbColor, (double)timer / time);
		timer++;
	}
}
