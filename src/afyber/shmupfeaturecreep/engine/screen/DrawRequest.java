package afyber.shmupfeaturecreep.engine.screen;

public interface DrawRequest {

	int depth();

	Type getType();

	enum Type {
		SPRITE,
		RECT,
		LINE,
		TEXT
	}
}
