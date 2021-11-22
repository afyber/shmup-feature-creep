package afyber.shmupfeaturecreep;

import afyber.shmupfeaturecreep.engine.Screen;
import afyber.shmupfeaturecreep.engine.Timing;
import afyber.shmupfeaturecreep.engine.input.Keyboard;
import afyber.shmupfeaturecreep.engine.world.World;

import java.awt.*;

public class MainClass {

	public static final String GAME_NAME = "shmupfeaturecreep";
	public static final String GAME_NAME_NICE = "Shmup: Feature Creep";
	public static final int WINDOW_WIDTH = 640;
	public static final int WINDOW_HEIGHT = 640;

	private static final int IDEAL_FPS = 60;

	public static final boolean DEBUG = true;

	public static void main(String[] args) {
		// TODO: use a logger
		if (DEBUG)
			System.out.println("Program start");

		Screen.setupScreen(GAME_NAME_NICE, WINDOW_WIDTH, WINDOW_HEIGHT);

		Timing.setIdealFrameTimeMillis((long)Math.ceil(1000f/IDEAL_FPS));

		Keyboard.clearKeys();

		World world = new World();

		if (DEBUG)
			System.out.println("Main Loop Start");

		while (!Screen.isWindowClosed()) {
			Timing.mainLoopBodyStarted();

			Keyboard.applyKeyQueue();

			// deal with objects that were added and removed last frame
			world.destroyAll();

			world.moveAllNewlyAdded();

			// NOTE: setting alarm[0] = 1 in create code will apply on the first frame of execution
			world.alarmAll();

			// update all the stuff
			world.updateAll();

			// draw everything
			Screen.clearAllPixelsToColor(Color.BLACK);

			world.drawAll();

			Screen.applyDrawRequestsAndPaint();

			// tell Keyboard that a frame has passed
			Keyboard.frameDone();

			Timing.mainLoopBodyEnded();
			try {
				Timing.calculateTimeAndWaitThread();
			}
			catch (InterruptedException e) {
				System.out.println("For some reason an InterruptedException happened while waiting the main thread.");
				e.printStackTrace();
				Thread.currentThread().interrupt();
			}
		}

		if (DEBUG)
			System.out.println("Main loop end");

		// save important information and whatnot here

		if (DEBUG)
			System.out.println("Program end");
	}
}
