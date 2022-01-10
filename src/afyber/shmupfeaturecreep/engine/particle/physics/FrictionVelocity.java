package afyber.shmupfeaturecreep.engine.particle.physics;

import afyber.shmupfeaturecreep.engine.MathUtil;
import afyber.shmupfeaturecreep.engine.particle.Particle;

public class FrictionVelocity implements PhysicsModel {

	private double xFriction;
	private double yFriction;

	private double xVel;
	private double yVel;

	public FrictionVelocity(double xVel, double yVel, double xFriction, double yFriction) {
		this.xVel = xVel;
		this.yVel = yVel;
		this.xFriction = xFriction;
		this.yFriction = yFriction;
	}
	public FrictionVelocity(double angle, double velocity, double friction) {
		this.xVel = Math.cos(Math.toRadians(angle)) * velocity;
		this.yVel = Math.sin(Math.toRadians(angle)) * velocity;
		this.xFriction = Math.abs(Math.cos(Math.toRadians(angle)) * friction);
		this.yFriction = Math.abs(Math.sin(Math.toRadians(angle)) * friction);
	}

	@Override
	public void update(Particle particle) {
		particle.x += xVel;
		particle.y += yVel;
		xVel = MathUtil.interpolateExp(xVel, 0, 2, xFriction);
		yVel = MathUtil.interpolateExp(yVel, 0, 2, yFriction);
	}

	@Override
	public PhysicsModel copy() {
		return new FrictionVelocity(xVel, yVel, xFriction, yFriction);
	}
}
