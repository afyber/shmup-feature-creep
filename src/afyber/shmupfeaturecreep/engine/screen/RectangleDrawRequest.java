package afyber.shmupfeaturecreep.engine.screen;

record RectangleDrawRequest(int x1, int y1, int x2, int y2, int rgbColor, int depth) implements DrawRequest {

	@Override
	public DrawRequestType getType() {
		return DrawRequestType.RECT;
	}
}
