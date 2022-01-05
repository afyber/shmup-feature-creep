package afyber.shmupfeaturecreep.engine.particle;

import afyber.shmupfeaturecreep.engine.particle.physics.PhysicsModel;
import afyber.shmupfeaturecreep.engine.particle.render.RenderModel;

public class Particle {

	private final PhysicsModel physics;
	private final RenderModel renderer;

	public double alpha;
	private final FadeRule fadeRule;

	public final int lifetime;
	public int lived;

	public int depth;
	public double x;
	public double y;

	public Particle(double x, double y, int lifetime, PhysicsModel physics, RenderModel renderer, FadeRule fadeRule, int depth) {
		this.x = x;
		this.y = y;
		this.lifetime = lifetime;
		this.physics = physics;
		this.renderer = renderer;
		this.fadeRule = fadeRule;
		this.depth = depth;
		this.alpha = 1.0;
	}

	public void update() {
		physics.update(this);
		switch (this.fadeRule) {
			case LINEAR -> {
				alpha = 1.0 - ((double)lived / lifetime);
			}
			case SQUARE -> {
				alpha = 1.0 - (Math.pow(lived, 2) / Math.pow(lifetime, 2));
			}
			case CUBE -> {
				alpha = 1.0 - (Math.pow(lived, 3) / Math.pow(lifetime, 3));
			}
			case FIFTH -> {
				alpha = 1.0 - (Math.pow(lived, 5) / Math.pow(lifetime, 5));
			}
			case TENTH -> {
				alpha = 1.0 - (Math.pow(lived, 10) / Math.pow(lifetime, 10));
			}
		}
	}

	public void draw() {
		renderer.draw(this);
	}

	public Particle copy() {
		return new Particle(x, y, lifetime, physics, renderer, fadeRule, depth);
	}

	enum FadeRule {
		LINEAR,
		SQUARE,
		CUBE,
		FIFTH,
		TENTH
	}
}
