/*
 * Copyright (c) 2012, Finn Kuusisto
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *     Redistributions of source code must retain the above copyright notice,
 *     this list of conditions and the following disclaimer.
 *
 *     Redistributions in binary form must reproduce the above copyright notice,
 *     this list of conditions and the following disclaimer in the documentation
 *     and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package afyber.shmupfeaturecreep.engine.audio;

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

		int maxFramesPerUpdate = (int)((Sound.FORMAT.getFrameRate() / 1000) * 25);
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
