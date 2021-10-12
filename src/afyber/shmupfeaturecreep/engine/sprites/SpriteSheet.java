package afyber.shmupfeaturecreep.engine.sprites;

import ar.com.hjg.pngj.PngReaderByte;
import jdk.jfr.Unsigned;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;

/**
 * This class does stuff.
 *
 * @author afyber
 */
public class SpriteSheet {

	@Unsigned
	private byte[][] imageData;

	private HashMap<String, SpriteInformation> allSprites;

	public SpriteSheet(String imageName) {
		PngReaderByte imageReader = new PngReaderByte(new File(imageName + ".png"));
		try {
			setupSpriteSheet(imageReader);
			imageReader.end();
		}
		catch (Exception e) {
			imageReader.close();
			e.printStackTrace();
		}

		try (InputStream infoStream = new FileInputStream(imageName + "_info.txt")) {
			setupSpriteInfo(infoStream);
		}
		catch (IOException e) {
			System.out.println("Attempting to load sprite-sheet caused IOException");
			e.printStackTrace();
		}
	}

	private void setupSpriteSheet(PngReaderByte reader) {
		imageData = new byte[reader.imgInfo.rows][reader.imgInfo.cols * 4];

		for (int i = 0; i < imageData.length; i++) {
				imageData[i] = Arrays.copyOf(reader.readRowByte().getScanline(), imageData[0].length);
		}
	}

	private void setupSpriteInfo(InputStream stream) throws IOException {
		allSprites = new HashMap<>();

		byte[] allInfoBytes = stream.readAllBytes();
		stream.close();

		String name;
		int x;
		int y;
		int width;
		int height;
		int originX;
		int originY;

		StringBuilder allInfoBuilder = new StringBuilder();
		for (byte charByte: allInfoBytes) {
			allInfoBuilder.append(Character.toString(charByte));
		}
		String allInfo = allInfoBuilder.toString();

		String[] allInfoSplit = allInfo.split("\r\n");


		for (String section: allInfoSplit) {
			String[] splitMore = section.split(":");
			name = splitMore[0];

			String[] splitEvenMore = splitMore[1].split(",");
			x = Integer.parseInt(splitEvenMore[0]);
			y = Integer.parseInt(splitEvenMore[1]);
			width = Integer.parseInt(splitEvenMore[2]);
			height = Integer.parseInt(splitEvenMore[3]);
			// TODO
			originX = Integer.parseInt(splitEvenMore[4]);
			originY = Integer.parseInt(splitEvenMore[5]);

			SpriteInformation info = new SpriteInformation(x, y, width, height, originX, originY);
			allSprites.put(name, info);
		}
	}

	public boolean hasSprite(String name) {
		return allSprites.containsKey(name);
	}

	public SpriteSheetRegion getSprite(String name) {
		SpriteInformation info = allSprites.get(name);

		byte[][] newBytes = new byte[info.dataHeight()][info.dataWidth() * 4];
		for (int y = 0; y < info.dataHeight(); y++) {
			System.arraycopy(imageData[y + info.dataBeginY()], info.dataBeginX() * 4, newBytes[y], 0, info.dataWidth() * 4);
		}

		return new SpriteSheetRegion(newBytes, info.dataWidth(), info.dataHeight(), info.originX(), info.originY());
	}

	public static void testPngLoading() {
		PngReaderByte reader = new PngReaderByte(new File("test4.png"));
		System.out.println(reader.imgInfo.cols);
		while (reader.hasMoreRows()) {
			System.out.println(Arrays.toString(reader.readRowByte().getScanline()));
		}
		reader.end();
	}

	record SpriteInformation(int dataBeginX, int dataBeginY, int dataWidth, int dataHeight, int originX, int originY) {}
}