package afyber.shmupfeaturecreep.engine.world;

public interface Collision {

	Type getType();

	enum Type {
		SPRITE,
		RECT
	}
}
