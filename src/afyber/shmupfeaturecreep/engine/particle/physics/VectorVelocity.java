package afyber.shmupfeaturecreep.engine.particle.physics;

import afyber.shmupfeaturecreep.engine.particle.Particle;

public class VectorVelocity implements PhysicsModel {

	private final double angle;
	private final double velocity;

	public VectorVelocity(double angle, double velocity) {
		this.angle = angle;
		this.velocity = velocity;
	}

	@Override
	public void update(Particle particle) {
		particle.x += Math.cos(Math.toRadians(angle)) * velocity;
		particle.y += Math.sin(Math.toRadians(angle)) * velocity;
	}

	@Override
	public PhysicsModel copy() {
		return new VectorVelocity(angle, velocity);
	}
}
