package afyber.shmupfeaturecreep.engine.sound;

import javax.sound.sampled.SourceDataLine;
import java.util.concurrent.atomic.AtomicBoolean;

public class SoundUpdateRunner implements Runnable {

	private AtomicBoolean running;
	private SourceDataLine outLine;
	private Mixer mixer;

	public SoundUpdateRunner(Mixer mixer, SourceDataLine outLine) {
		running = new AtomicBoolean();
		this.outLine = outLine;
		this.mixer = mixer;
	}

	public void stop() {
		running.set(false);
	}

	@Override
	public void run() {
		running.set(true);

		int bufferSize = (int)Sound.FORMAT.getFrameRate() * Sound.FORMAT.getFrameSize();
		byte[] audioBuffer = new byte[bufferSize];

		// 20 milliseconds worth
		int maxFramesPerUpdate = (int)((Sound.FORMAT.getFrameRate() / 1000) * 20);
		int numBytesRead = 0;
		double framesAccrued = 0;
		long lastUpdate = System.nanoTime();

		while (this.running.get()) {

			long currTime = System.nanoTime();

			double delta = currTime - lastUpdate;
			double secDelta = (delta / 1000000000L);
			framesAccrued += secDelta * Sound.FORMAT.getFrameRate();

			int framesToRead = (int)framesAccrued;
			int framesToSkip = 0;

			if (framesToRead > maxFramesPerUpdate) {
				framesToSkip = framesToRead - maxFramesPerUpdate;
				framesToRead = maxFramesPerUpdate;
			}

			if (framesToSkip > 0) {
				mixer.skipFrames(framesToSkip);
			}

			if (framesToRead > 0) {
				int bytesToRead = framesToRead * Sound.FORMAT.getFrameSize();
				int tmpBytesRead = mixer.read(audioBuffer, bytesToRead);
				numBytesRead += tmpBytesRead;

				int remaining = bytesToRead - tmpBytesRead;
				for (int i = 0; i < remaining; i++) {
					audioBuffer[numBytesRead + i] = 0;
				}
				numBytesRead += remaining;
			}

			framesAccrued -= (framesToRead + framesToSkip);

			if (numBytesRead > 0) {
				outLine.write(audioBuffer, 0, numBytesRead);
				numBytesRead = 0;
			}

			lastUpdate = currTime;

			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {}
		}
	}
}
