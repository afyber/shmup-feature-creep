package afyber.shmupfeaturecreep.engine.screen;

record LineDrawRequest(int x1, int y1, int x2, int y2, int width, int rgbColor, int depth) implements DrawRequest {

	@Override
	public DrawRequestType getType() {
		return DrawRequestType.LINE;
	}
}
