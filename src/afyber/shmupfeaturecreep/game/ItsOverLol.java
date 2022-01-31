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

		drawTextExtCentered("PROGRAMMING AND GAME DESIGN", 240, 400, 2, 2, -1, 1);
		drawTextExtCentered("BY", 240, 430, 1, 1, -1, 1);
		drawTextExtCentered("AFYBER", 240, 450, 3, 3, -1, 1);

		drawTextExtCentered("MUSIC", 440, 490, 2, 2, -1, 1);
		drawTextExtCentered("BY", 440, 520, 1, 1, -1, 1);
		drawTextExtCentered("SOMEONE", 440, 540, 3, 3, -1, 1);
		drawTextExtCentered("AND", 440, 580, 1, 1, -1, 1);
		drawTextExtCentered("AFYBER", 440, 600, 3, 3, -1, 1);
	}
}
