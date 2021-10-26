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

	private HashMap<String, SpriteSheetRegion> allSprites;

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
			originX = Integer.parseInt(splitEvenMore[4]);
			originY = Integer.parseInt(splitEvenMore[5]);

			byte[][] newBytes = new byte[height][width * 4];
			for (int i = 0; i < height; i++) {
				System.arraycopy(imageData[i + y], x * 4, newBytes[i], 0, width * 4);
			}
			SpriteSheetRegion region = new SpriteSheetRegion(newBytes, width * 4, height, originX, originY);
			allSprites.put(name, region);
		}
	}

	public boolean hasSprite(String name) {
		return allSprites.containsKey(name);
	}

	public SpriteSheetRegion getSprite(String name) {
		return allSprites.get(name);
	}

	public SpriteInformation getSpriteInformation(String spriteName) {
		SpriteSheetRegion region = allSprites.get(spriteName);
		return new SpriteInformation(region.dataWidth(), region.dataHeight(), region.originX(), region.originY());
	}
}