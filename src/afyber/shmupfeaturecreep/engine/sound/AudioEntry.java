package afyber.shmupfeaturecreep.engine.sound;

public interface AudioEntry {

	int getBufferToPlay();

	int getSourceToPlay();

	// 0: BasicAudioEntry
	// 1: IntroLoopAudioEntry
	int getEntryType();

	void clearStatus();

	void setBufferIndex(int bufferIndex);

	void setSourceIndex(int sourceIndex);
}
