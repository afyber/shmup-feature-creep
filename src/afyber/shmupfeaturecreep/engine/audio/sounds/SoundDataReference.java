package afyber.shmupfeaturecreep.engine.audio.sounds;

public class SoundDataReference {

	// The duplication of this information pains my performance gains
	private final String name;

	private final byte[][] data;

	private final int channels;

	public SoundDataReference(byte[][] data, int channels, String name) {
		this.data = data;
		this.channels = channels;
		this.name = name;
	}

	public byte readByte(int channel, int index) {
		return data[channel][index];
	}

	public int getChannels() {
		return channels;
	}

	public int getDataLength() {
		return data[0].length;
	}

	public String getName() {
		return this.name;
	}
}
