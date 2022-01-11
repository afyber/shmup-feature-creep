package afyber.shmupfeaturecreep.engine.screen;

import afyber.shmupfeaturecreep.MainClass;
import afyber.shmupfeaturecreep.engine.CompactFrameArray;
import afyber.shmupfeaturecreep.engine.GeneralUtil;
import afyber.shmupfeaturecreep.engine.errors.SpriteSheetsNotDefinedError;
import afyber.shmupfeaturecreep.engine.input.KeyboardHandler;
import afyber.shmupfeaturecreep.engine.output.EngineLogger;
import afyber.shmupfeaturecreep.engine.sprites.Sprite;
import afyber.shmupfeaturecreep.engine.sprites.SpriteInformation;
import afyber.shmupfeaturecreep.engine.sprites.SpriteSheet;
import afyber.shmupfeaturecreep.engine.sprites.SpriteSheetRegion;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * This class handles the window and drawing to it, all of this is done statically because I am an insane person.
 * Like seriously, why did I do this.
 *
 * @author afyber
 */
public class Screen {
	private Screen() {}

	private static int screenWidth;
	private static int screenHeight;

	private static JFrame frame;
	private static CustomPanel panel;
	private static BufferedImage image;

	private static int[][] currentFrame;
	private static boolean isDrawing;

	private static String currentFont;

	private static boolean windowClosed;

	private static ArrayList<DrawRequest> drawRequests;

	private static HashMap<String, Sprite> allSprites;
	private static HashMap<String, Font> allFonts;

	public static void setupScreen(String name, int width, int height) {
		setupScreen(name, width, height, false);
	}
	public static void setupScreen(String name, int width, int height, boolean splashScreen) {
		screenWidth = width;
		screenHeight = height;
		// Window setup
		image = new BufferedImage(width, height, Image.SCALE_DEFAULT);
		frame = new JFrame() {
			@Override
			public void dispose() {
				super.dispose();
				MainClass.LOGGER.log(EngineLogger.Level.DEBUG, "Window dispose called");
				windowClosed = true;
			}
		};
		windowClosed = false;
		panel = new CustomPanel();
		panel.addKeyListener(new KeyboardHandler());
		panel.setFocusable(true);
		frame.add(panel);
		frame.setResizable(false);
		frame.setTitle(name);
		// these numbers are so that the drawable area is actually the size specified
		frame.setSize(screenWidth + 16, screenHeight + 39);
		frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		frame.setVisible(true);

		currentFrame = new int[height][width];
		isDrawing = false;

		if (splashScreen) {
			SpriteSheetRegion splashScreenData = SpriteSheet.loadSingleImage("/splashscreen.png");
			SpriteSheetRegion splashScreenScaled = scaleImageData(splashScreenData.data(), (double)width / splashScreenData.dataWidth(), (double)height / splashScreenData.dataHeight(), 0, 0);

			image.setRGB(0, 0, width, height, GeneralUtil.arrayOfArraysToSingleArray(splashScreenScaled.data()).a(), 0, width);

			panel.repaint();
		}

		drawRequests = new ArrayList<>();

		allSprites = new HashMap<>();
		allFonts = new HashMap<>();

		loadSpriteSheets();

		loadFonts();
	}

	private static void loadSpriteSheets() {
		try {
			String[] allData = GeneralUtil.readResourceAsLineArray("/spritesheets.txt");

			for (String str: allData) {
				if (GeneralUtil.isLineConfigViable(str)) {
					SpriteSheet.loadSpriteSheet("/spritesheets/" + str, allSprites);
				}
			}
		}
		catch (IOException e) {
			throw new SpriteSheetsNotDefinedError();
		}
	}

	private static void loadFonts() {
		try {
			String[] allData = GeneralUtil.readResourceAsLineArray("/fonts.txt");

			for (String line: allData) {
				if (GeneralUtil.isLineConfigViable(line)) {
					Font newFont = new Font("/fonts/" + line);

					allFonts.put(line, newFont);

					currentFont = line;
				}
			}
		} catch (IOException e) {
			throw new SpriteSheetsNotDefinedError();
		}
	}

	public static void draw(String spriteName, double spriteIndex, double x, double y, int depth) {
		draw(spriteName, spriteIndex, x, y, 1, 1, depth);
	}
	public static void draw(String spriteName, double spriteIndex, double x, double y, double xScale, double yScale, int depth) {
		draw(spriteName, spriteIndex, x, y, xScale, yScale, depth, 1);
	}
	public static void draw(String spriteName, double spriteIndex, double x, double y, double xScale, double yScale, int depth, double alpha) {
		if (isDrawing) {
			try {
				drawRequests.add(new SpriteDrawRequest(spriteName, (int)spriteIndex, (int)Math.round(x), (int)Math.round(y), xScale, yScale, depth, alpha));
			} catch (NullPointerException e) {
				MainClass.LOGGER.log(EngineLogger.Level.ERROR, "Draw attempted before 'drawRequests' initialized", e);
			}
		}
	}

	public static void drawRect(double x1, double y1, double x2, double y2, Color color, int depth) {
		drawRect(x1, y1, x2, y2, color.getRGB(), depth);
	}
	public static void drawRect(double x1, double y1, double x2, double y2, int rgbColor, int depth) {
		drawRect(x1, y1, x2, y2, rgbColor, depth, 1.0);
	}
	public static void drawRect(double x1, double y1, double x2, double y2, int rgbColor, int depth, double alpha) {
		if (isDrawing) {
			if ((rgbColor >> 24 & 0xFF) == 0x0) {
				rgbColor = 0xFF000000 | rgbColor;
			}

			try {
				if (x1 > x2) {
					double tmp = x1;
					x1 = x2;
					x2 = tmp;
				}
				if (y1 > y2) {
					double tmp = y1;
					y1 = y2;
					y2 = tmp;
				}
				drawRequests.add(new RectangleDrawRequest((int)Math.round(x1), (int)Math.round(y1), (int)Math.round(x2), (int)Math.round(y2), rgbColor, depth, alpha));
			} catch (NullPointerException e) {
				MainClass.LOGGER.log(EngineLogger.Level.ERROR, "Draw attempted before 'drawRequests' initialized", e);
			}
		}
	}

	public static void drawLine(double x1, double y1, double x2, double y2, double width, Color color, int depth) {
		drawLine(x1, y1, x2, y2, width, color.getRGB(), depth);
	}
	public static void drawLine(double x1, double y1, double x2, double y2, double width, int rgbColor, int depth) {
		drawLine(x1, y1, x2, y2, width, rgbColor, depth, 1.0);
	}
	public static void drawLine(double x1, double y1, double x2, double y2, double width, int rgbColor, int depth, double alpha) {
		if (isDrawing) {
			if ((rgbColor >> 24 & 0xFF) == 0x0) {
				rgbColor = 0xFF000000 | rgbColor;
			}

			try {
				drawRequests.add(new LineDrawRequest((int)Math.round(x1), (int)Math.round(y1), (int)Math.round(x2), (int)Math.round(y2), (int)Math.floor(width), rgbColor, depth, alpha));
			} catch (NullPointerException e) {
				MainClass.LOGGER.log(EngineLogger.Level.ERROR, "Draw attempted before 'drawRequests' initialized", e);
			}
		}
	}

	public static void drawText(String message, double x, double y, int depth) {
		drawText(message, x, y, -1, depth);
	}
	public static void drawText(String message, double x, double y, double wrapWidth, int depth) {
		drawText(message, x, y, wrapWidth, depth, 1.0);
	}
	public static void drawText(String message, double x, double y, double wrapWidth, int depth, double alpha) {
		if (isDrawing) {
			try {
				drawRequests.add(new TextDrawRequest(message, currentFont, (int)Math.round(x), (int)Math.round(y), (int)Math.round(wrapWidth), depth, alpha));
			} catch (NullPointerException e) {
				MainClass.LOGGER.log(EngineLogger.Level.ERROR, "Draw attempted before 'drawRequests' initialized", e);
			}
		}
	}

	public static void clearAllPixelsToColor(Color color) {
		clearAllPixelsToColor(color.getRGB());
	}
	public static void clearAllPixelsToColor(int rgbColor) {
		for (int i = 0; i < currentFrame.length; i++) {
			for (int c = 0; c < currentFrame[0].length; c++) {
				currentFrame[i][c] = rgbColor;
			}
		}
	}

	public static void applyDrawRequestsAndPaint() {
		// sort requests by depth
		drawRequests.sort((o1, o2) -> {
			if (o1.depth() < o2.depth()) {
				return -1;
			} else if (o1.depth() > o2.depth()) {
				return 1;
			}
			return 0;
		});

		// draw the things in order
		for (DrawRequest request: drawRequests) {
			switch (request.getType()) {
				case SPRITE -> {
					SpriteDrawRequest requestConvert = (SpriteDrawRequest)request;
					if (allSprites.containsKey(requestConvert.spriteName())) {
						Sprite sprite = allSprites.get(requestConvert.spriteName());
						SpriteSheetRegion region = sprite.getFrame(requestConvert.spriteIndex());
						applySpriteRequestToFrame(region, requestConvert);
					}
				}
				case RECT -> {
					RectangleDrawRequest requestConvert = (RectangleDrawRequest)request;
					applyRectToFrame(requestConvert);
				}
				case LINE -> {
					LineDrawRequest requestConvert = (LineDrawRequest)request;
					applyLineToFrame(requestConvert);
				}
				case TEXT -> {
					TextDrawRequest requestConvert = (TextDrawRequest)request;
					applyTextToFrame(requestConvert);
				}
			}
		}
		// get rid of them all (we processed them all anyways)
		drawRequests.clear();

		// move the frame data to the image
		copyFrameToImage();

		// draw to the window
		panel.repaint();
	}

	private static void applySpriteRequestToFrame(SpriteSheetRegion sprite, SpriteDrawRequest request) {
		// all this to say, if the sprite does not overlap the image, do not draw it
		int actualX = request.x() - (int)Math.round(sprite.originX() * request.xScale());
		int actualY = request.y() - (int)Math.round(sprite.originY() * request.yScale());
		int actualX2 = actualX + (int)Math.round(sprite.dataWidth() * request.xScale());
		int actualY2 = actualY + (int)Math.round(sprite.dataHeight() * request.yScale());
		if (!GeneralUtil.areRectanglesIntersecting(actualX, actualY, actualX2, actualY2, 0, 0, image.getWidth() - 1, image.getHeight() - 1)) {
			return;
		}

		// the sprite is at least partially on-screen

		SpriteSheetRegion scaledSprite = scaleImageData(sprite.data(), request.xScale(), request.yScale(), sprite.originX(), sprite.originY());
		int[][] spriteData = scaledSprite.data();
		double alphaPercent = Math.max(Math.min(1, request.alpha()), 0);

		for (int y = 0; y < scaledSprite.dataHeight(); y++) {
			for (int x = 0; x < scaledSprite.dataWidth(); x++) {
				int calculatedX = request.x() + x - scaledSprite.originX();
				int calculatedY = request.y() + y - scaledSprite.originY();

				applyPixelToFrameWithAlpha(calculatedX, calculatedY, spriteData[y][x], alphaPercent);
			}
		}
	}

	private static void applySpriteDataToFrame(int[][] data, int x, int y, double alpha) {
		// less efficient sometimes, faster other times

		for (int i = 0; i < data.length; i++) {
			for (int c = 0; c < data[0].length; c++) {
				applyPixelToFrameWithAlpha(x + c, y + i, data[i][c], alpha);
			}
		}
	}

	private static void applyRectToFrame(RectangleDrawRequest request) {
		if (!GeneralUtil.areRectanglesIntersecting(request.x1(), request.y1(), request.x2(), request.y2(), 0, 0, image.getWidth() - 1, image.getHeight() - 1)) {
			return;
		}

		for (int y = request.y1(); y < request.y2(); y++) {
			for (int x = request.x1(); x < request.x2(); x++) {
				applyPixelToFrameWithAlpha(x, y, request.rgbColor(), request.alpha());
			}
		}
	}

	private static void applyLineToFrame(LineDrawRequest request) {
		if (request.x1() == request.x2()) {
			// This is a vertical line
			applyRectToFrame(new RectangleDrawRequest(request.x1() - request.width() / 2, request.y1(), request.x2() + request.width() / 2 + 1, request.y2(), request.rgbColor(), request.depth(), request.alpha()));
		}
		else if (request.y1() == request.y2()) {
			// This is a horizontal line
			applyRectToFrame(new RectangleDrawRequest(request.x1(), request.y1() - request.width() / 2, request.x2(), request.y2() + request.width() / 2 + 1, request.rgbColor(), request.depth(), request.alpha()));
		}
		else {
			// This is a diagonal line, this is where it gets complicated
			// TODO LOW PRIORITY: Line drawing for diagonal lines
		}
	}

	private static void applyTextToFrame(TextDrawRequest request) {
		Font font;
		if (allFonts.containsKey(request.font())) {
			font = allFonts.get(request.font());
		}
		else {
			MainClass.LOGGER.log(EngineLogger.Level.ERROR, "Could not draw text: invalid or missing font \"" + request.font() + "\"");
			return;
		}

		int runningX = 0;
		int runningY = 0;

		for (int i = 0; i < request.message().length(); i++) {

			char current = request.message().charAt(i);

			FontCharacter character = font.getCharacter(current);

			if (character == null || current == ' ') {
				// The character is either an actual space, or not in the font-set, also make sure to wrap, even for whitespace
				if (request.wrapWidth() != -1 && runningX != 0 && runningX + font.getSpaceWidth() > request.wrapWidth()) {
					runningX = 0;
					runningY += font.getLineHeight();
				}
				runningX += font.getSpaceWidth();
			}
			else {
				// most of the magic happens in here

				// if this character will go past the wrap width, and we have drawn a character this line, wrap around
				if (request.wrapWidth() != -1 && runningX != 0 && runningX + character.imageWidth() > request.wrapWidth()) {
					runningX = 0;
					runningY += font.getLineHeight();
				}

				applySpriteDataToFrame(character.imageData(), request.x() + runningX + character.xOffs(), request.y() + runningY + character.yOffs(), request.alpha());

				runningX += character.xOffs() + character.imageWidth() + character.nextXOffs();
			}
		}
	}

	private static void applyPixelToFrame(int x, int y, int rgbColor) {
		applyPixelToFrameWithAlpha(x, y, rgbColor, 1);
	}
	private static void applyPixelToFrameWithAlpha(int x, int y, int rgbColor, double alpha) {
		if (y < 0 || y >= image.getHeight() ||
				x < 0 || x >= image.getWidth()) {
			return;
		}

		if (alpha == 1 && (rgbColor >> 24 & 0xFF) == 0xFF) {
			currentFrame[y][x] = rgbColor;
		}
		else if (alpha > 0 && (rgbColor >> 24 & 0xFF) > 0x0) {
			// basically takes a weighted average of the R, G, and B components of the pixel and the current frame, with the weight being the alpha of the sprite being drawn
			double percentage = ((float)(rgbColor >> 24 & 0xFF) / 0xFF) * alpha;
			currentFrame[y][x] = 0xFF000000 | Math.min(0xFF, (int)((currentFrame[y][x] >> 16 & 0xFF) * (1 - percentage) + (rgbColor >> 16 & 0xFF) * percentage)) << 16 | Math.min(0xFF, (int)((currentFrame[y][x] >> 8 & 0xFF) * (1 - percentage) + (rgbColor >> 8 & 0xFF) * percentage)) << 8 | Math.min(0xFF, (int)((currentFrame[y][x] & 0xFF) * (1 - percentage) + (rgbColor & 0xFF) * percentage));
		}
	}

	private static void copyFrameToImage() {
		CompactFrameArray singleArray = GeneralUtil.arrayOfArraysToSingleArray(currentFrame);
		image.setRGB(0, 0, screenWidth, screenHeight, singleArray.a(), 0, singleArray.dataWidth());
	}

	public static SpriteSheetRegion scaleImageData(int[][] data, double xScale, double yScale, int originX, int originY) {
		// TODO: properly figure out scaling to prevent annoying jerkiness when constantly changing scale with an origin not equal to 0,0
		if (xScale == 1 && yScale == 1) {
			return new SpriteSheetRegion(data, data[0].length, data.length, originX, originY);
		}
		else if (xScale == 0 || yScale == 0) {
			return new SpriteSheetRegion(new int[1][1], 1, 1, 0, 0);
		}

		boolean xNegative = xScale < 0;
		if (xNegative) {
			xScale = -xScale;
		}
		boolean yNegative = yScale < 0;
		if (yNegative) {
			yScale = -yScale;
		}

		int[][] newDataX;

		double runningTally = 0;
		int intTally;
		int newXOrigin = 0;
		int newX = data[0].length;
		if (xScale != 1) {
			newX = 0;
			newDataX = new int[data.length][(int)Math.floor(data[0].length * xScale)];
			for (int oldX = 0; oldX < data[0].length; oldX++) {
				if (oldX == originX) {
					newXOrigin = newX;
				}
				runningTally += xScale;
				intTally = (int)runningTally;
				if (intTally > 0) {
					for (int y = 0; y < data.length; y++) {
						for (int i = 0; i < intTally; i++) {
							newDataX[y][newX + i] = data[y][oldX];
						}
					}
					runningTally -= intTally;
					newX += intTally;
				}
			}
		}
		else {
			newDataX = data;
			newXOrigin = originX;
		}

		int[][] newDataY;

		int newYOrigin = 0;
		int newY = data.length;
		if (yScale != 1) {
			newY = 0;
			newDataY = new int[(int)Math.floor(data.length * yScale)][newDataX[0].length];
			runningTally = 0;
			for (int oldY = 0; oldY < data.length; oldY++) {
				if (oldY == originY) {
					newYOrigin = newY;
				}
				runningTally += yScale;
				intTally = (int)runningTally;
				if (intTally > 0) {
					for (int x = 0; x < newDataY[0].length; x++) {
						for (int i = 0; i < intTally; i++) {
							newDataY[newY + i][x] = newDataX[oldY][x];
						}
					}
					runningTally -= intTally;
					newY += intTally;
				}
			}
		}
		else {
			newYOrigin = originY;
			newDataY = newDataX;
		}

		if (xNegative && newDataY.length != 0) {
			GeneralUtil.reverseBottomSpriteArrays(newDataY);
			newXOrigin = newX - newXOrigin;
		}
		if (yNegative) {
			GeneralUtil.reverseTopSpriteArray(newDataY);
			newYOrigin = newY - newYOrigin;
		}

		return new SpriteSheetRegion(newDataY, newX, newY, newXOrigin, newYOrigin);
	}

	public static SpriteSheetRegion getSprite(String spriteName, int spriteIndex) {
		SpriteSheetRegion region = null;
		if (allSprites.containsKey(spriteName)) {
			region = allSprites.get(spriteName).getFrame(spriteIndex);
		}
		return region;
	}

	public static SpriteSheetRegion getSpriteScaled(String spriteName, int spriteIndex, double xScale, double yScale) {
		SpriteSheetRegion region = null;
		if (allSprites.containsKey(spriteName)) {
			region = allSprites.get(spriteName).getFrame(spriteIndex);
		}
		if (region != null) {
			return scaleImageData(region.data(), xScale, yScale, region.originX(), region.originY());
		}
		return null;
	}

	public static SpriteInformation getSpriteInfo(String spriteName, int spriteIndex) {
		SpriteInformation info = null;
		if (allSprites.containsKey(spriteName)) {
			SpriteSheetRegion tmp = allSprites.get(spriteName).getFrame(spriteIndex);
			info = new SpriteInformation(tmp.dataWidth(), tmp.dataHeight(), tmp.originX(), tmp.originY());
		}
		return info;
	}

	public static SpriteInformation getScaledSpriteInfo(String spriteName, int spriteIndex, double xScale, double yScale) {
		SpriteInformation info = getSpriteInfo(spriteName, spriteIndex);
		if (info != null) {
			int newOriginX;
			int newOriginY;
			int newDataWidth;
			int newDataHeight;
			if (xScale < 0) {
				newDataWidth = (int)Math.floor(info.dataWidth() * -xScale);
				newOriginX = (int)Math.ceil(newDataWidth - info.originX() * -xScale);
			}
			else {
				newDataWidth = (int)Math.floor(info.dataWidth() * xScale);
				newOriginX = (int)Math.floor(info.originX() * xScale);
			}
			if (yScale < 0) {
				newDataHeight = (int)Math.floor(info.dataHeight() * -yScale);
				newOriginY = (int)Math.ceil(newDataHeight - info.originY() * -yScale);
			}
			else {
				newDataHeight = (int)Math.floor(info.dataHeight() * yScale);
				newOriginY = (int)Math.floor(info.originY() * yScale);
			}
			return new SpriteInformation(newDataWidth, newDataHeight, newOriginX, newOriginY);
		}
		return null;
	}

	public static boolean isWindowClosed() {
		return windowClosed;
	}

	public static void setIsDrawing(boolean val) {
		isDrawing = val;
	}

	public static boolean getIsDrawing() {
		return isDrawing;
	}

	// NOTE: private classes

	private static class CustomPanel extends JPanel {
		@Override
		public void paint(Graphics g) {
			Graphics2D g2d = (Graphics2D)g;

			g2d.drawImage(image, 0, 0, screenWidth, screenHeight, null);
		}
	}
}