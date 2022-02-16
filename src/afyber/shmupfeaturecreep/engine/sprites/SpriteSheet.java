package afyber.shmupfeaturecreep.engine.sprites;

import afyber.shmupfeaturecreep.Main;
import afyber.shmupfeaturecreep.engine.GeneralUtil;
import afyber.shmupfeaturecreep.engine.errors.SpriteSheetsNotDefinedError;
import afyber.shmupfeaturecreep.engine.output.EngineLogger;
import ar.com.hjg.pngj.PngReaderInt;

import java.io.IOException;
import java.util.Map;

/**
 * This class serves as a buffer between the actual image data and Screen which loads the sprite information and the sprites themselves.
 *
 * @author afyber
 */
public class SpriteSheet {
	private SpriteSheet() {}

	private static int[][] imageData;

	public static void loadSpriteSheet(String imageName, Map<String, Sprite> out) {
		PngReaderInt imageReader;
		try {
			imageReader = new PngReaderInt(Main.class.getResourceAsStream(imageName + ".png"));
		}
		catch (Exception e) {
			Main.LOGGER.log(EngineLogger.Level.ERROR, "Couldn't load sprite-sheet image:", e);
			throw new SpriteSheetsNotDefinedError();
		}

		try {
			setupSpriteSheetData(imageReader);
			imageReader.end();
		}
		catch (Exception e) {
			imageReader.close();
			Main.LOGGER.log(EngineLogger.Level.ERROR, "Couldn't load sprite-sheet image:", e);
			throw new SpriteSheetsNotDefinedError();
		}

		try {
			setupSprites(GeneralUtil.readResourceAsLineArray(imageName + "_info.txt"), out);
		}
		catch (IOException e) {
			Main.LOGGER.log(EngineLogger.Level.ERROR, "Attempting to load sprite-sheet caused IOException", e);
			throw new SpriteSheetsNotDefinedError();
		}
	}

	public static SpriteSheetRegion loadSingleImage(String imageName) {
		PngReaderInt imageReader = new PngReaderInt(Main.class.getResourceAsStream(imageName));
		imageData = new int[imageReader.imgInfo.rows][imageReader.imgInfo.cols];

		for (int i = 0; i < imageData.length; i++) {
			int[] scanline = imageReader.readRowInt().getScanline();
			int[] aRGBScanline = new int[imageReader.imgInfo.cols];
			for (int n = 0; n < imageReader.imgInfo.cols; n++) {
				aRGBScanline[n] = scanline[n * 4 + 3] << 24 | scanline[n * 4] << 16 | scanline[n * 4 + 1] << 8 | scanline[n * 4 + 2];
			}
			imageData[i] = aRGBScanline;
		}

		return new SpriteSheetRegion(imageData, imageReader.imgInfo.cols, imageReader.imgInfo.rows, 0, 0);
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

	private static void setupSprites(String[] lines, Map<String, Sprite> map) {
		int frame;
		int x;
		int y;
		int width;
		int height;
		int originX;
		int originY;

		int line = 0;
		Sprite currentSprite = null;
		String currentName = null;

		while (line < lines.length) {
			String currentLine = lines[line];

			if (Character.isAlphabetic(currentLine.charAt(0))) {
				if (currentSprite != null) {
					if (currentName == null) {
						Main.LOGGER.log(EngineLogger.Level.ERROR, "Couldn't load sprite definition");
					}
					else {
						map.put(currentName, currentSprite);
					}
				}

				String[] split = currentLine.split(":");
				currentName = split[0];
				int frames = Integer.parseInt(split[1]);

				currentSprite = new Sprite(frames);
			}
			else if (Character.isDigit(currentLine.charAt(0))) {
				String[] splitMore = currentLine.split(":");
				frame = Integer.parseInt(splitMore[0]);

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

				if (currentSprite != null) {
					currentSprite.setFrame(region, frame);
				}
			}

			line++;
		}

		// fixes a bug
		if (currentSprite != null) {
			if (currentName == null) {
				Main.LOGGER.log(EngineLogger.Level.ERROR, "Couldn't load sprite definition");
			}
			else {
				map.put(currentName, currentSprite);
			}
		}
	}
}