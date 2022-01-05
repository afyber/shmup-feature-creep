package afyber.shmupfeaturecreep.engine.particle.physics;

import afyber.shmupfeaturecreep.engine.particle.Particle;

public class SimpleVelocity implements PhysicsModel {

	private final double xVel;
	private final double yVel;

	public SimpleVelocity(double xVel, double yVel) {
		this.xVel = xVel;
		this.yVel = yVel;
	}
	public SimpleVelocity(int angle, double velocity) {
		this.xVel = Math.cos(Math.toRadians(angle)) * velocity;
		this.yVel = Math.sin(Math.toRadians(angle)) * velocity;
	}

	@Override
	public void update(Particle particle) {
		particle.x += xVel;
		particle.y += yVel;
	}
}
