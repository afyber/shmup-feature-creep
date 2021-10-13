package afyber.shmupfeaturecreep;

import afyber.shmupfeaturecreep.engine.Screen;
import afyber.shmupfeaturecreep.engine.Timing;
import afyber.shmupfeaturecreep.engine.World;
import afyber.shmupfeaturecreep.engine.input.Keyboard;

import java.awt.*;

public class MainClass {

	public static final String GAME_NAME = "shmupfeaturecreep";
	public static final String GAME_NAME_NICE = "Shmup: Feature Creep";
	public static final int WINDOW_WIDTH = 640;
	public static final int WINDOW_HEIGHT = 400;

	private static final int IDEAL_FPS = 60;

	public static final boolean DEBUG = true;

	public static void main(String[] args) {
		if (DEBUG)
			System.out.println("Program start");

		Screen.setupScreen(GAME_NAME_NICE, WINDOW_WIDTH, WINDOW_HEIGHT);

		Timing.setIdealFrameTimeMillis(1000L/IDEAL_FPS);

		Keyboard.clearKeys();

		World world = new World();

		if (DEBUG)
			System.out.println("Main Loop Start");

		while (!Screen.isWindowClosed()) {
			Timing.mainLoopBodyStarted();

			Keyboard.applyKeyQueue();

			// BUG: alarms from create code set at room start will apply one frame early
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
