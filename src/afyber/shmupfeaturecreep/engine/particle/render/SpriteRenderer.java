package afyber.shmupfeaturecreep.engine.particle.render;

import afyber.shmupfeaturecreep.engine.particle.Particle;
import afyber.shmupfeaturecreep.engine.screen.Screen;

public class SpriteRenderer implements RenderModel {

	private String sprite;
	private int spriteIndex;
	private double xScale;
	private double yScale;

	public SpriteRenderer(String spriteName, int spriteIndex, double xScale, double yScale) {
		this.sprite = spriteName;
		this.spriteIndex = spriteIndex;
		this.xScale = xScale;
		this.yScale = yScale;
	}

	@Override
	public void draw(Particle particle) {
		Screen.draw(sprite, spriteIndex, particle.x, particle.y, xScale, yScale, particle.depth, particle.alpha);
	}

	@Override
	public RenderModel copy() {
		return new SpriteRenderer(sprite, spriteIndex, xScale, yScale);
	}
}
