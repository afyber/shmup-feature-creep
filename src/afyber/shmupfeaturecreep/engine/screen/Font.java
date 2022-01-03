package afyber.shmupfeaturecreep.engine.screen;

import afyber.shmupfeaturecreep.MainClass;
import afyber.shmupfeaturecreep.engine.GeneralUtil;
import afyber.shmupfeaturecreep.engine.errors.SpriteSheetNotDefinedError;
import afyber.shmupfeaturecreep.engine.output.LoggingLevel;
import ar.com.hjg.pngj.PngReaderInt;

import java.io.IOException;
import java.util.HashMap;

public class Font {

	private final HashMap<Character, FontCharacter> characterMap = new HashMap<>();
	private int spaceWidth = 16;
	private int lineHeight = 20;

	public Font(String fileName) {
		String[] fileContents;
		try {
			 fileContents = GeneralUtil.readResourceAsLineArray(fileName + "_info.txt");
		} catch (IOException e) {
			MainClass.LOGGER.log(LoggingLevel.ERROR, "Error loading font definition");
			throw new SpriteSheetNotDefinedError();
		}

		int[][] fontImageData;
		try {
			PngReaderInt reader = new PngReaderInt(MainClass.class.getResourceAsStream(fileName + ".png"));
			fontImageData = setupSpriteSheetData(reader);
		} catch (Exception e) {
			MainClass.LOGGER.log(LoggingLevel.ERROR, "Error loading font image");
			throw new SpriteSheetNotDefinedError();
		}

		for (String line: fileContents) {
			String[] split = line.split(":");

			if (split[0].equals("SPACE")) {
				spaceWidth = Integer.parseInt(split[1]);
				continue;
			}
			else if (split[0].equals("LINE_HEIGHT")) {
				lineHeight = Integer.parseInt(split[1]);
				continue;
			}

			String[] params = split[1].split(",");

			int x = Integer.parseInt(params[0]);
			int y = Integer.parseInt(params[1]);
			int width = Integer.parseInt(params[2]);
			int height = Integer.parseInt(params[3]);
			int xOffset = Integer.parseInt(params[4]);
			int yOffset = Integer.parseInt(params[5]);
			int nextXOffset = Integer.parseInt(params[6]);

			int[][] sprite = new int[height][width];

			for (int i = 0; i < height; i++) {
				System.arraycopy(fontImageData[y + i], x, sprite[i], 0, width);
			}

			characterMap.put(split[0].charAt(0), new FontCharacter(sprite, width, height, xOffset, yOffset, nextXOffset));
		}
	}

	private static int[][] setupSpriteSheetData(PngReaderInt reader) {
		int[][] retur = new int[reader.imgInfo.rows][reader.imgInfo.cols];

		for (int i = 0; i < retur.length; i++) {
			int[] scanline = reader.readRowInt().getScanline();
			int[] aRGBScanline = new int[reader.imgInfo.cols];
			for (int n = 0; n < reader.imgInfo.cols; n++) {
				aRGBScanline[n] = scanline[n * 4 + 3] << 24 | scanline[n * 4] << 16 | scanline[n * 4 + 1] << 8 | scanline[n * 4 + 2];
			}
			retur[i] = aRGBScanline;
		}

		return retur;
	}

	int getLineHeight() {
		return lineHeight;
	}

	int getSpaceWidth() {
		return spaceWidth;
	}

	FontCharacter getCharacter(char character) {
		if (characterMap.containsKey(character)) {
			return characterMap.get(character);
		}

		return null;
	}
}
