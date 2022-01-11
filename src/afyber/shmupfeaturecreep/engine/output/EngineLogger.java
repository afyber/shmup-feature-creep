package afyber.shmupfeaturecreep.engine.output;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public class EngineLogger {

	private final String filename;
	private Level lowestAllowedLevel;
	private boolean writeToFile;

	public EngineLogger(String filename, boolean writeToFile) {
		this.filename = filename;
		this.lowestAllowedLevel = Level.WARNING;
		setWriteToFile(writeToFile);
	}

	public void log(Level level, String msg) {
		try {
			String message = "[" + level.name() + "] " + msg + "\n";
			System.out.print(message);
			if (writeToFile && level.getValue() >= lowestAllowedLevel.getValue()) {
				Files.writeString(Path.of(filename), message, StandardOpenOption.APPEND);
			}
		}
		catch (IOException e) {
			System.out.println("IOException when attempting to log");
			e.printStackTrace();
		}
	}

	public void log(Level level, String msg, Throwable e) {
		try {
			String message = "[" + level.name() + "] " + msg + "\n" + e.toString() + "\n";
			System.out.print(message);
			if (writeToFile && level.getValue() >= lowestAllowedLevel.getValue()) {
				Files.writeString(Path.of(filename), message, StandardOpenOption.APPEND);
			}
		}
		catch (IOException ex) {
			System.out.println("IOError when attempting to log");
			ex.printStackTrace();
		}
	}

	public void setLoggingLevel(Level level) {
		this.lowestAllowedLevel = level;
	}

	public Level getLoggingLevel() {
		return this.lowestAllowedLevel;
	}

	public void setWriteToFile(boolean val) {
		writeToFile = val;
		if (writeToFile) {
			try {
				Files.createFile(Path.of(filename));
			} catch (IOException e) {
				System.out.println("Either the log file already exists or the program doesn't have the ability to create a file");
				e.printStackTrace();
			}
		}
	}

	public enum Level {
		DEBUG(0),
		INFO(1),
		WARNING(2),
		ERROR(3);

		private final int value;

		Level(int value) {
			this.value = value;
		}

		public int getValue() {
			return value;
		}
	}
}
