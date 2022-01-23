package afyber.shmupfeaturecreep.game;

public record Wave(EnemyWaveSlot[] slots, Stage stage, int framesToNext) {

	public enum Stage {
		BW,
		CRT,
		NES,
		SNES,
		DOOM
	}
}
