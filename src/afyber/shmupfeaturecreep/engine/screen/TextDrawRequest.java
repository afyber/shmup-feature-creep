package afyber.shmupfeaturecreep.engine.screen;

public record TextDrawRequest(String message, String font, int x, int y, double xScale, double yScale, int wrapWidth, int depth, double alpha) implements DrawRequest {

	@Override
	public Type getType() {
		return Type.TEXT;
	}
}
