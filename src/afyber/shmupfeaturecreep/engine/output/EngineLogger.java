package afyber.shmupfeaturecreep.engine.output;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public class EngineLogger {

	private final String filename;
	private LoggingLevel lowestAllowedLevel;

	public EngineLogger(String filename) {
		this.filename = filename;
		this.lowestAllowedLevel = LoggingLevel.WARNING;
		try {
			Files.createFile(Path.of(filename));
		}
		catch (IOException e) {
			System.out.println("Either the log file already exists or the program doesn't have the ability to create a file");
			e.printStackTrace();
		}
	}

	public void log(LoggingLevel level, String msg) {
		try {
			if (level.getValue() >= lowestAllowedLevel.getValue()) {
				Files.writeString(Path.of(filename), "[" + level.name() + "] " + msg + "\n", StandardOpenOption.APPEND);
			}
			System.out.print("[" + level.name() + "] " + msg + "\n");
		}
		catch (IOException e) {
			System.out.println("IOException when attempting to log");
			e.printStackTrace();
		}
	}

	public void log(LoggingLevel level, String msg, Throwable e) {
		try {
			if (level.getValue() >= lowestAllowedLevel.getValue()) {
				Files.writeString(Path.of(filename), "[" + level.name() + "] " + msg + "\n" + e.getMessage() + "\n", StandardOpenOption.APPEND);
			}
			System.out.print("[" + level.name() + "] " + msg +"\n" + e.getMessage() + "\n");
		}
		catch (IOException ex) {
			System.out.println("IOError when attempting to log");
			ex.printStackTrace();
		}
	}

	public void setLoggingLevel(LoggingLevel level) {
		this.lowestAllowedLevel = level;
	}

	public LoggingLevel getLoggingLevel() {
		return this.lowestAllowedLevel;
	}
}