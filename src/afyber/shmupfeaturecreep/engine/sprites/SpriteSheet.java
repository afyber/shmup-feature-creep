package afyber.shmupfeaturecreep.engine.sprites;

import ar.com.hjg.pngj.PngReaderByte;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Map;

/**
 * This class does stuff.
 *
 * @author afyber
 */
public class SpriteSheet {
	private SpriteSheet() {}

	private static byte[][] imageData;

	public static void loadSpriteSheet(String imageName, Map<String, SpriteSheetRegion> out) {
		PngReaderByte imageReader = new PngReaderByte(new File(imageName + ".png"));
		try {
			setupSpriteSheetData(imageReader);
			imageReader.end();
		}
		catch (Exception e) {
			imageReader.close();
			e.printStackTrace();
		}

		try (InputStream infoStream = new FileInputStream(imageName + "_info.txt")) {
			setupSprites(infoStream, out);
		}
		catch (IOException e) {
			System.out.println("Attempting to load sprite-sheet caused IOException");
			e.printStackTrace();
		}
	}

	private static void setupSpriteSheetData(PngReaderByte reader) {
		imageData = new byte[reader.imgInfo.rows][reader.imgInfo.cols * 4];

		for (int i = 0; i < imageData.length; i++) {
				imageData[i] = Arrays.copyOf(reader.readRowByte().getScanline(), imageData[0].length);
		}
	}

	private static void setupSprites(InputStream stream, Map<String, SpriteSheetRegion> map) throws IOException {
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
			originX = Integer.parseInt(splitEvenMore[4]);
			originY = Integer.parseInt(splitEvenMore[5]);

			byte[][] newBytes = new byte[height][width * 4];
			for (int i = 0; i < height; i++) {
				System.arraycopy(imageData[i + y], x * 4, newBytes[i], 0, width * 4);
			}
			SpriteSheetRegion region = new SpriteSheetRegion(newBytes, width * 4, height, originX, originY);
			map.put(name, region);
		}
	}
}