package afyber.shmupfeaturecreep.engine.sound;

public class BasicAudioEntry implements AudioEntry {

	private int bufferIndex;
	private int sourceIndex;

	public BasicAudioEntry() {}
	public BasicAudioEntry(int bufferIndex, int sourceIndex) {
		this.bufferIndex = bufferIndex;
		this.sourceIndex = sourceIndex;
	}

	@Override
	public int getBufferToPlay() {
		return bufferIndex;
	}

	@Override
	public int getSourceToPlay() {
		return sourceIndex;
	}

	@Override
	public int getEntryType() {
		return 0;
	}

	@Override
	public void clearStatus() {
		// nothing to clear
	}

	@Override
	public void setBufferIndex(int bufferIndex) {
		this.bufferIndex = bufferIndex;
	}

	@Override
	public void setSourceIndex(int sourceIndex) {
		this.sourceIndex = sourceIndex;
	}
}
