package afyber.shmupfeaturecreep.game;

import afyber.shmupfeaturecreep.Game;
import afyber.shmupfeaturecreep.engine.audio.Sound;
import afyber.shmupfeaturecreep.engine.rooms.DynamicObject;
import afyber.shmupfeaturecreep.engine.world.WorldMiddleman;

public class ItsOverLol extends DynamicObject {

	public ItsOverLol(double x, double y, int depth, int instanceID) {
		super(x, y, depth, instanceID);
		objectName = "its_over_lol";
	}

	@Override
	public void create(WorldMiddleman world) {
		Sound.playMusic("its_over_lol");
	}

	@Override
	public void draw(WorldMiddleman world) {
		drawTextExtCentered("THAT'S", Game.WINDOW_WIDTH / 2.0, 100, 3, 3, -1, 1);
		drawTextExtCentered("THE END", Game.WINDOW_WIDTH / 2.0, 150, 9, 9, -1, 1);
		drawTextExtCentered("OF THE DEMO", Game.WINDOW_WIDTH / 2.0, 290, 4, 4, -1, 1);

		drawTextExtCentered("PROGRAMMING", 200, 400, 2, 2, -1, 1);
		drawTextExtCentered("AND", 200, 430, 1, 1, -1, 1);
		drawTextExtCentered("GAME DESIGN", 200, 450, 2, 2, -1, 1);
		drawTextExtCentered("BY", 200, 480, 1, 1, -1, 1);
		drawTextExtCentered("AFYBER", 200, 500, 3, 3, -1, 1);

		drawTextExtCentered("MUSIC", 460, 400, 2, 2, -1, 1);
		drawTextExtCentered("BY", 460, 430, 1, 1, -1, 1);
		drawTextExtCentered("SOMEONE", 460, 450, 3, 3, -1, 1);
		drawTextExtCentered("AND", 460, 480, 1, 1, -1, 1);
		drawTextExtCentered("AFYBER", 460, 500, 2, 2, -1, 1);
	}
}
