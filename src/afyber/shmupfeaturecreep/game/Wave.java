package afyber.shmupfeaturecreep.game;

import java.util.Arrays;
import java.util.Objects;

public record Wave(EnemyWaveSlot[] slots, WaveProperties properties) {
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Wave wave = (Wave)o;
		return Arrays.equals(slots, wave.slots) && properties.equals(wave.properties);
	}

	@Override
	public int hashCode() {
		int result = Objects.hash(properties);
		result = 31 * result + Arrays.hashCode(slots);
		return result;
	}

	@Override
	public String toString() {
		return "Wave{" +
				"slots=" + Arrays.toString(slots) +
				", properties=" + properties +
				'}';
	}
}
