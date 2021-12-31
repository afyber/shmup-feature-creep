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

import afyber.shmupfeaturecreep.engine.audio.music.LoopParent;
import afyber.shmupfeaturecreep.engine.audio.music.MusicParent;
import afyber.shmupfeaturecreep.engine.audio.sounds.SoundInstance;

import java.util.ArrayList;

public class Mixer {

	private double globalVolume;

	final ArrayList<SoundInstance> playingSounds;

	public Mixer() {
		globalVolume = 1.0;
		playingSounds = new ArrayList<>();
	}

	public synchronized void setGlobalVolume(double volume) {
		this.globalVolume = volume;
	}

	public synchronized int read(byte[] data, int length) {

		boolean readBytes = true;

		int numBytesRead = 0;

		for (int i = 0; i < length && readBytes; i += 4) {
			readBytes = false;

			double leftVal = 0.0;
			double rightVal = 0.0;

			for (SoundInstance sound: playingSounds) {
				if (sound.isPlaying()) {
					double volume = globalVolume * sound.getGain();
					if (sound.getChannels() == 1) {
						int[] val = sound.readFrame();
						double currVal = val[0] * volume;

						leftVal += currVal;
						rightVal += currVal;

						readBytes = true;
					} else if (sound.getChannels() == 2) {
						int[] vals = sound.readFrame();
						double currLeft = vals[0] * volume;
						double currRight = vals[1] * volume;

						leftVal += currLeft;
						rightVal += currRight;

						readBytes = true;
					}
				}
			}

			// remove sounds that are no longer playing
			playingSounds.removeIf(sound -> !sound.isPlaying());

			for (MusicParent music: Sound.allMusic.values()) {
				if (music.isPlaying()) {
					double volume = globalVolume * music.getGain();

					if (music.getChannels() == 1) {
						int[] val = music.readFrame();
						double currVal = val[0] * volume;

						leftVal += currVal;
						rightVal += currVal;

						readBytes = true;
					} else if (music.getChannels() == 2) {
						int[] vals = music.readFrame();
						double currLeft = vals[0] * volume;
						double currRight = vals[1] * volume;

						leftVal += currLeft;
						rightVal += currRight;

						readBytes = true;
					}
				}
			}
			for (LoopParent loop: Sound.allLoops.values()) {
				if (loop.isPlaying()) {
					double volume = globalVolume * loop.getGain();

					if (loop.getChannels() == 1) {
						int[] val = loop.readFrame();
						double currVal = val[0] * volume;

						leftVal += currVal;
						rightVal += currVal;

						readBytes = true;
					} else if (loop.getChannels() == 2) {
						int[] vals = loop.readFrame();
						double currLeft = vals[0] * volume;
						double currRight = vals[1] * volume;

						leftVal += currLeft;
						rightVal += currRight;

						readBytes = true;
					}
				}
			}

			if (readBytes) {
				int finalLeftVal = (int)leftVal;
				int finalRightVal = (int)rightVal;
				finalLeftVal = Math.max(Math.min(finalLeftVal, Short.MAX_VALUE), Short.MIN_VALUE);
				finalRightVal = Math.max(Math.min(finalRightVal, Short.MAX_VALUE), Short.MIN_VALUE);

				data[i + 1] = (byte)((finalLeftVal >> 8) & 0xFF);
				data[i] = (byte)(finalLeftVal & 0xFF);

				data[i + 3] = (byte)((finalRightVal >> 8) & 0xFF);
				data[i + 2] = (byte)(finalRightVal & 0xFF);

				numBytesRead += 4;
			}
		}

		return numBytesRead;
	}

	public synchronized void skipFrames(int frames) {
		for (SoundInstance sound: playingSounds) {
			sound.skipFrames(frames);
		}
		for (MusicParent music: Sound.allMusic.values()) {
			music.skipFrames(frames);
		}
		for (LoopParent loop: Sound.allLoops.values()) {
			loop.skipFrames(frames);
		}
	}

	public synchronized void addSound(SoundInstance sound) {
		playingSounds.add(sound);
	}

	public synchronized void removeSound(SoundInstance sound) {
		playingSounds.remove(sound);
	}
}
