package afyber.shmupfeaturecreep;

import afyber.shmupfeaturecreep.engine.output.EngineLogger;

public class EntryPoint {

	public static void main(String[] args) {
		// this is just to ensure that errors are logged
		try {
			Main.mainLoop();
		} catch (Error e) {
			Main.LOGGER.log(EngineLogger.Level.ERROR, "Unhandled error occurred in mainLoop:", e);
		}
	}
}
