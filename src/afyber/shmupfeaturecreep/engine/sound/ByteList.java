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
package afyber.shmupfeaturecreep.engine.sound;

import java.util.Arrays;

public class ByteList {

	private int numBytes;
	private byte[] data;

	public ByteList() {
		data = new byte[20];
		numBytes = 0;
	}

	public void add(byte b) {
		if (numBytes >= data.length) {
			long tmp = data.length * 2L;
			int newSize = tmp > Integer.MAX_VALUE ? Integer.MAX_VALUE : (int)tmp;
			data = Arrays.copyOf(data, newSize);
		}
		data[numBytes] = b;
		numBytes++;
	}

	public byte get(int i) {
		if (i < 0 || i > numBytes) {
			throw new ArrayIndexOutOfBoundsException();
		}
		return data[i];
	}

	public int size() {
		return numBytes;
	}

	public byte[] toArray() {
		return Arrays.copyOf(data, numBytes);
	}
}
