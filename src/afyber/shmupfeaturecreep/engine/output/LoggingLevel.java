package afyber.shmupfeaturecreep.engine.output;

public enum LoggingLevel {
	DEBUG(0),
	INFO(1),
	WARNING(2),
	ERROR(3);

	private final int value;

	LoggingLevel(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}
}
