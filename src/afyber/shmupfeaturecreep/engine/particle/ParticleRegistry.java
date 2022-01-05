package afyber.shmupfeaturecreep.engine.particle;

import java.util.HashMap;

public class ParticleRegistry {
	private ParticleRegistry() {}

	private static final HashMap<String, Particle> particleDefinitions = new HashMap<>();

	public static void registerParticle(String name, Particle particle) {
		particleDefinitions.put(name, particle);
	}

	public static Particle getNewParticle(String name) {
		return particleDefinitions.get(name).copy();
	}

	public static boolean hasParticle(String name) {
		return particleDefinitions.containsKey(name);
	}
}
