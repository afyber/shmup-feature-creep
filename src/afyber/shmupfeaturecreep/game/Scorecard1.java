package afyber.shmupfeaturecreep.game;

import afyber.shmupfeaturecreep.MainClass;
import afyber.shmupfeaturecreep.engine.rooms.DynamicObject;
import afyber.shmupfeaturecreep.engine.screen.Screen;
import afyber.shmupfeaturecreep.engine.world.WorldMiddleman;

public class Scorecard1 extends DynamicObject {

	public Scorecard1(double x, double y, int depth, int instanceID) {
		super(x, y, depth, instanceID);
		objectName = "scorecard";
	}

	@Override
	public void draw(WorldMiddleman world) {
		Screen.drawLine(MainClass.WINDOW_WIDTH * 3.0/4.0, 0, MainClass.WINDOW_WIDTH * 3.0/4.0, MainClass.WINDOW_HEIGHT, 3, 0xFFFFFF, 0);
		Screen.drawText("ABCA A B C CCC BBB AAA EEEEEEEEEDECABBDBCBDBABDE", 50, 50, 200, 0);
	}
}
