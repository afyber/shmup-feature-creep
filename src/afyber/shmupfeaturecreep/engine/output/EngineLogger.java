package afyber.shmupfeaturecreep.engine.output;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public class EngineLogger {

	private static final String LOGS_DIR = "./logs/";

	private final String filename;
	private Level lowestAllowedLevel;
	private boolean writeToFile;

	public EngineLogger(String filename, boolean writeToFile) {
		this.filename = LOGS_DIR + filename;
		this.lowestAllowedLevel = Level.INFO;
		this.writeToFile = writeToFile;
	}

	public void log(Level level, String msg) {
		try {
			if (level.getValue() >= lowestAllowedLevel.getValue()) {
				String message = "[" + level.name() + "] " + msg + "\n";
				System.out.print(message);
				if (writeToFile) {
					ensureFilePresence();
					Files.writeString(Path.of(filename), message, StandardOpenOption.APPEND);
				}
			}
		}
		catch (IOException e) {
			// do not attempt to write to file again
			writeToFile = false;
			System.out.println("IOException when attempting to log");
			e.printStackTrace();
		}
	}

	public void log(Level level, String msg, Throwable e) {
		try {
			if (level.getValue() >= lowestAllowedLevel.getValue()) {
				StringWriter writer = new StringWriter();
				e.printStackTrace(new PrintWriter(writer));
				String message = "[" + level.name() + "] " + msg + "\n" + writer;
				System.out.print(message);
				if (writeToFile) {
					ensureFilePresence();
					Files.writeString(Path.of(filename), message, StandardOpenOption.APPEND);
				}
			}
		}
		catch (IOException ex) {
			writeToFile = false;
			System.out.println("IOError when attempting to log");
			ex.printStackTrace();
		}
	}

	public void setLoggingLevel(Level level) {
		this.lowestAllowedLevel = level;
	}

	private void ensureFilePresence() {
		if (writeToFile) {
			try {
				Files.createDirectories(Path.of(LOGS_DIR));
				Files.createFile(Path.of(filename));
			} catch (IOException e) {
				// the file already exists or cannot be accessed
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
