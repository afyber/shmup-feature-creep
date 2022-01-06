package afyber.shmupfeaturecreep.engine.particle.render;

import afyber.shmupfeaturecreep.engine.particle.Particle;
import afyber.shmupfeaturecreep.engine.screen.Screen;

public class SquareRenderer implements RenderModel {

	private final double width;
	private final int color;

	public SquareRenderer(double width, int color) {
		this.width = width;
		this.color = color;
	}

	@Override
	public void draw(Particle particle) {
		Screen.drawRect(particle.x - width / 2, particle.y - width / 2, particle.x + width / 2, particle.y + width / 2, color, particle.depth, particle.alpha);
	}
}
