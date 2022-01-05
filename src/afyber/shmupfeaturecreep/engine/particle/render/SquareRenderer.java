package afyber.shmupfeaturecreep.engine.particle.render;

import afyber.shmupfeaturecreep.engine.particle.Particle;
import afyber.shmupfeaturecreep.engine.screen.Screen;

public class SquareRenderer implements RenderModel {

	private final int width;
	private final int color;

	public SquareRenderer(int width, int color) {
		this.width = width;
		this.color = color;
	}

	@Override
	public void draw(Particle particle) {
		Screen.drawRect(particle.x, particle.y, particle.x + width, particle.y + width, color, particle.depth, particle.alpha);
	}
}
