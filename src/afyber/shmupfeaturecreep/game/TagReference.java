package afyber.shmupfeaturecreep.game;

public record TagReference(EnemyTag tag, TagState state) {

	public enum TagState {
		REQUIRED,
		RECOMMENDED,
		DISALLOWED
	}
}
