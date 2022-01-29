package afyber.shmupfeaturecreep.game;

public record WaveProperties(Stage stage, int framesToNext, boolean repeatable) {

	public enum Stage {
		BW,
		CRT,
		NES,
		SNES,
		DOOM
	}
}
