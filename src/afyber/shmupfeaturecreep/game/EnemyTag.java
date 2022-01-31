package afyber.shmupfeaturecreep.game;

public enum EnemyTag {
	// Special info
	INVINCIBLE,

	// How does it move?
	TOP_TO_BOTTOM,
	TOP_TO_SIDE,

	// Does it have bullets?
	NO_BULLETS,
	BULLETS,

	// How many bullets at once?
	ONE_BULLET,
	TWO_BULLET,
	THREE_BULLET,
	FOUR_BULLET,
	MANY_BULLETS,

	// How many times do bullets fire?
	ONE_TIME_BULLETS,
	TWO_TIME_BULLETS,
	THREE_TIME_BULLETS,
	CONSTANT_BULLETS,
}
