package afyber.shmupfeaturecreep.game;

import afyber.shmupfeaturecreep.MainClass;
import afyber.shmupfeaturecreep.engine.rooms.DynamicObject;
import afyber.shmupfeaturecreep.engine.world.WorldMiddleman;

public class Scorecard1 extends DynamicObject {

	public Scorecard1(float x, float y, int depth, int instanceID) {
		super(x, y, depth, instanceID);
		objectName = "scorecard";
	}

	@Override
	public void draw(WorldMiddleman world) {
		draw("vertical_border", MainClass.WINDOW_WIDTH * 3f/4f, 0, 1, MainClass.WINDOW_HEIGHT);
	}
}
