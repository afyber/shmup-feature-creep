package afyber.shmupfeaturecreep.game;

public record WaveProperties(String name, Stage stage, int framesToNext, boolean repeatable, int batch, double difficulty) {

	public enum Stage {
		BW,
		CRT,
		NES,
		SNES,
		DOOM
	}
}
