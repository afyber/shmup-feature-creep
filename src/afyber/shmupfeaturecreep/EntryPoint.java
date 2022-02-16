package afyber.shmupfeaturecreep;

import afyber.shmupfeaturecreep.engine.output.EngineLogger;
import afyber.shmupfeaturecreep.engine.screen.Screen;

public class EntryPoint {

	public static void main(String[] args) {
		// this is just to ensure that errors are logged
		try {
			Main.mainLoop();
		} catch (Exception e) {
			Main.LOGGER.log(EngineLogger.Level.ERROR, "Unhandled exception occurred in mainLoop:", e);
		} finally {
			if (!Screen.isWindowClosed()) {
				Main.LOGGER.log(EngineLogger.Level.INFO, "Emergency window dispose called");
				Screen.closeWindow();
			}
		}
	}
}
