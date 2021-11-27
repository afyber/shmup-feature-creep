package afyber.shmupfeaturecreep.engine.sprites;

import afyber.shmupfeaturecreep.MainClass;
import afyber.shmupfeaturecreep.engine.errors.SpriteSheetNotDefinedError;
import afyber.shmupfeaturecreep.engine.output.LoggingLevel;
import ar.com.hjg.pngj.PngReaderInt;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

/**
 * This class serves as a buffer between the actual image data and Screen which loads the sprite information and the sprites themselves.
 *
 * @author afyber
 */
public class SpriteSheet {
	private SpriteSheet() {}

	private static int[][] imageData;

	public static void loadSpriteSheet(String imageName, Map<String, SpriteSheetRegion> out) {
		PngReaderInt imageReader;
		try {
			imageReader = new PngReaderInt(new File(imageName + ".png"));
		}
		catch (Exception e) {
			MainClass.LOGGER.log(LoggingLevel.ERROR, "Couldn't load sprite-sheet image:", e);
			throw new SpriteSheetNotDefinedError();
		}

		try {
			setupSpriteSheetData(imageReader);
			imageReader.end();
		}
		catch (Exception e) {
			imageReader.close();
			MainClass.LOGGER.log(LoggingLevel.ERROR, "Couldn't load sprite-sheet image:", e);
			throw new SpriteSheetNotDefinedError();
		}

		try (InputStream infoStream = new FileInputStream(imageName + "_info.txt")) {
			setupSprites(infoStream, out);
		}
		catch (IOException e) {
			MainClass.LOGGER.log(LoggingLevel.ERROR, "Attempting to load sprite-sheet caused IOException", e);
			throw new SpriteSheetNotDefinedError();
		}
	}

	private static void setupSpriteSheetData(PngReaderInt reader) {
		imageData = new int[reader.imgInfo.rows][reader.imgInfo.cols];

		for (int i = 0; i < imageData.length; i++) {
			int[] scanline = reader.readRowInt().getScanline();
			int[] aRGBScanline = new int[reader.imgInfo.cols];
			for (int n = 0; n < reader.imgInfo.cols; n++) {
				aRGBScanline[n] = scanline[n * 4 + 3] << 24 | scanline[n * 4] << 16 | scanline[n * 4 + 1] << 8 | scanline[n * 4 + 2];
			}
			imageData[i] = aRGBScanline;
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

			int[][] newData = new int[height][width];
			for (int i = 0; i < height; i++) {
				System.arraycopy(imageData[i + y], x, newData[i], 0, width);
			}
			SpriteSheetRegion region = new SpriteSheetRegion(newData, width, height, originX, originY);
			map.put(name, region);
		}
	}
}