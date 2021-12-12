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
