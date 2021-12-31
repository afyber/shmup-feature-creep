package afyber.shmupfeaturecreep.engine.screen;

record SpriteDrawRequest(String spriteName, int spriteIndex, int x, int y, double xScale, double yScale, int depth, double alpha) implements DrawRequest {

	@Override
	public DrawRequestType getType() {
		return DrawRequestType.SPRITE;
	}
}