package afyber.shmupfeaturecreep;

import afyber.shmupfeaturecreep.engine.GeneralUtil;
import afyber.shmupfeaturecreep.engine.Registry;
import afyber.shmupfeaturecreep.engine.Timing;
import afyber.shmupfeaturecreep.engine.audio.Sound;
import afyber.shmupfeaturecreep.engine.errors.RoomNotDefinedError;
import afyber.shmupfeaturecreep.engine.input.Keyboard;
import afyber.shmupfeaturecreep.engine.output.EngineLogger;
import afyber.shmupfeaturecreep.engine.screen.Screen;
import afyber.shmupfeaturecreep.engine.world.World;

import java.awt.*;

public class MainClass {

	// TODO: REMEMBER TO CHANGE THIS TO                                                                              true
	public static final EngineLogger LOGGER = new EngineLogger(System.currentTimeMillis() + ".txt", false);

	public static void main(String[] args) {
		if (Game.DEBUG) {
			LOGGER.setLoggingLevel(EngineLogger.Level.DEBUG);
		}
		LOGGER.log(EngineLogger.Level.DEBUG, "Program start");

		boolean splashScreen = MainClass.class.getResource("/splashscreen.png") != null;
		if (splashScreen) {
			Screen.setupScreen(Game.GAME_NAME_NICE, Game.WINDOW_WIDTH, Game.WINDOW_HEIGHT, true);
		}

		Sound.init();

		Game.registerObjects();

		Game.registerRooms();

		Game.gameStart();

		Timing.setIdealFrameTimeMillis((long)Math.ceil(1000.0/Game.IDEAL_FPS));

		// NOTE: This fixes an audio bug where audio that plays on game start skips a few thousand frames DO NOT REMOVE IT
		GeneralUtil.sleepHandlingInterrupt(200);

		if (!splashScreen) {
			Screen.setupScreen(Game.GAME_NAME_NICE, Game.WINDOW_WIDTH, Game.WINDOW_HEIGHT, false);
		}

		Keyboard.clearKeys();

		World world;
		if (Registry.hasRoom("roomStart")) {
			 world = new World(Registry.getRoom("roomStart"));
		}
		else {
			LOGGER.log(EngineLogger.Level.ERROR, "The room registry does not contain \"roomStart\"");
			throw new RoomNotDefinedError();
		}

		LOGGER.log(EngineLogger.Level.DEBUG, "Main loop start");

		// ======== MAIN LOOP ========
		while (!Screen.isWindowClosed()) {
			Timing.mainLoopBodyStarted();

			if (world.getRoomChangeRequested()) {
				if (Registry.hasRoom(world.getRoomChangeRequestName())) {
					world = new World(Registry.getRoom(world.getRoomChangeRequestName()));
				}
				else {
					LOGGER.log(EngineLogger.Level.ERROR, "Couldn't load room \"" + world.getRoomChangeRequestName() + "\" after call to changeRoom");
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

			// perform physics (and other calculations)
			world.physicsUpdateAll();

			// alarm code
			world.alarmAll();

			// tell Keyboard that a frame has passed
			Keyboard.frameDone();

			Timing.mainLoopBodyEnded();
			// wait until the next frame
			Timing.calculateTimeAndWaitThread();
		}

		LOGGER.log(EngineLogger.Level.DEBUG, "Main loop end");

		Sound.shutdown();

		LOGGER.log(EngineLogger.Level.DEBUG, "Program end");
	}
}
