package afyber.shmupfeaturecreep.engine.audio;

import java.util.concurrent.atomic.AtomicBoolean;

public abstract class AudioParent {

	protected int bytePos;

	protected AtomicBoolean playing;

	protected double volume;
	protected double panning;

	protected AudioParent() {
		bytePos = 0;
		playing = new AtomicBoolean(false);
		volume = 1.0;
		panning = 0.0;
	}

	public abstract int[] readFrame();

	public abstract void skipFrames(int frames);

	public abstract int getChannels();

	protected int[] readFrameFromArray(byte[][] data, int channels) {
		int[] frame = null;
		if (channels == 1) {
			frame = new int[1];
			frame[0] = data[0][bytePos + 1] << 8 | data[0][bytePos] & 0xFF;
		}
		else if (channels == 2) {
			frame = new int[2];
			frame[0] = data[0][bytePos + 1] << 8 | data[0][bytePos] & 0xFF;
			frame[1] = data[1][bytePos + 1] << 8 | data[1][bytePos] & 0xFF;
		}

		return frame;
	}

	public void play() {
		bytePos = 0;
		playing.set(true);
	}

	public void stop() {
		bytePos = 0;
		playing.set(false);
	}

	public void pause() {
		playing.set(false);
	}

	public void resume() {
		playing.set(true);
	}

	public void setGain(double gain) {
		this.volume = gain;
	}

	public double getGain() {
		return volume;
	}

	public void setPanning(double panning) {
		this.panning = panning;
	}

	public double getPanning() {
		return panning;
	}

	public boolean isPlaying() {
		return playing.get();
	}
}
