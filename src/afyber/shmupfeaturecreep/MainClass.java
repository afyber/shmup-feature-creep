package afyber.shmupfeaturecreep;

import afyber.shmupfeaturecreep.engine.Registry;
import afyber.shmupfeaturecreep.engine.Screen;
import afyber.shmupfeaturecreep.engine.Timing;
import afyber.shmupfeaturecreep.engine.errors.RoomNotDefinedError;
import afyber.shmupfeaturecreep.engine.input.Keyboard;
import afyber.shmupfeaturecreep.engine.output.EngineLogger;
import afyber.shmupfeaturecreep.engine.output.LoggingLevel;
import afyber.shmupfeaturecreep.engine.sound.Sounds;
import afyber.shmupfeaturecreep.engine.world.World;

import java.awt.*;

public class MainClass {

	public static final String GAME_NAME = "shmupfeaturecreep";
	public static final String GAME_NAME_NICE = "Shmup: Feature Creep";
	public static final int WINDOW_WIDTH = 640;
	public static final int WINDOW_HEIGHT = 640;

	private static final int IDEAL_FPS = 60;

	public static final boolean DEBUG = true;

	public static final EngineLogger LOGGER = new EngineLogger(System.currentTimeMillis() + ".txt", false);

	public static void main(String[] args) {
		if (DEBUG) {
			LOGGER.setLoggingLevel(LoggingLevel.DEBUG);
		}
		LOGGER.log(LoggingLevel.DEBUG, "Program start");

		Sounds.setupSound();

		Sounds.loadSounds();

		Game.registerObjects();

		Game.registerRooms();

		Screen.setupScreen(GAME_NAME_NICE, WINDOW_WIDTH, WINDOW_HEIGHT);

		Timing.setIdealFrameTimeMillis((long)Math.ceil(1000f/IDEAL_FPS));

		Keyboard.clearKeys();

		World world;
		if (Registry.hasRoom("roomStart")) {
			 world = new World(Registry.getRoom("roomStart"));
		}
		else {
			LOGGER.log(LoggingLevel.ERROR, "The room registry does not contain \"roomStart\"");
			throw new RoomNotDefinedError();
		}

		LOGGER.log(LoggingLevel.DEBUG, "Main loop start");

		while (!Screen.isWindowClosed()) {
			Timing.mainLoopBodyStarted();

			if (world.getIsRoomChange()) {
				if (Registry.hasRoom(world.getRoomChangeRoomName())) {
					world = new World(Registry.getRoom(world.getRoomChangeRoomName()));
				}
				else {
					LOGGER.log(LoggingLevel.ERROR, "Couldn't load room \"" + world.getRoomChangeRoomName() + "\" after call to changeRoom");
					throw new RoomNotDefinedError();
				}
			}

			Keyboard.applyKeyQueue();

			// deal with objects that were added and removed last frame
			world.destroyAll();

			world.moveAllNewlyAdded();

			// update all the stuff
			world.updateAll();

			// draw everything
			Screen.clearAllPixelsToColor(Color.BLACK);

			world.drawAll();

			Screen.applyDrawRequestsAndPaint();

			// perform physics
			world.physicsUpdateAll();

			// alarm code
			world.alarmAll();

			// tell Keyboard that a frame has passed
			Keyboard.frameDone();

			Timing.mainLoopBodyEnded();
			try {
				Timing.calculateTimeAndWaitThread();
			}
			catch (InterruptedException e) {
				LOGGER.log(LoggingLevel.ERROR, "For some reason an InterruptedException happened while waiting the main thread:", e);
				Thread.currentThread().interrupt();
			}
		}

		LOGGER.log(LoggingLevel.DEBUG, "Main loop end");

		Sounds.closeSound();

		LOGGER.log(LoggingLevel.DEBUG, "Program end");
	}
}
