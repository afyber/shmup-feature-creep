package afyber.shmupfeaturecreep.engine;

import afyber.shmupfeaturecreep.MainClass;
import afyber.shmupfeaturecreep.engine.input.KeyboardHandler;
import afyber.shmupfeaturecreep.engine.sprites.SpriteInformation;
import afyber.shmupfeaturecreep.engine.sprites.SpriteSheet;
import afyber.shmupfeaturecreep.engine.sprites.SpriteSheetRegion;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
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

	private static JFrame frame;
	private static CustomPanel panel;
	private static BufferedImage image;

	private static int[][] currentFrame;

	private static boolean windowClosed;

	private static ArrayList<DrawRequest> drawRequests;

	private static HashMap<String, SpriteSheetRegion> allSprites;

	public static void setupScreen(String name, int width, int height) {
		// Window setup
		image = new BufferedImage(width, height, Image.SCALE_DEFAULT);
		frame = new JFrame() {
			@Override
			public void dispose() {
				super.dispose();
				if (MainClass.DEBUG)
					System.out.println("Window exit");
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
		frame.setSize(width + 16, height + 39);
		frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		frame.setVisible(true);

		currentFrame = new int[height][width];

		drawRequests = new ArrayList<>();

		allSprites = new HashMap<>();
		SpriteSheet.loadSpriteSheet("spritesheets/spritesheet1", allSprites);
		SpriteSheet.loadSpriteSheet("spritesheets/test6", allSprites);
	}

	public static void draw(String spriteName, float x, float y, int depth) {
		draw(spriteName, x, y, 1, 1, depth);
	}
	public static void draw(String spriteName, float x, float y, float xScale, float yScale, int depth) {
		draw(spriteName, x, y, xScale, yScale, depth, 1);
	}
	public static void draw(String spriteName, float x, float y, float xScale, float yScale, int depth, float alpha) {
		try {
			drawRequests.add(new DrawRequest(spriteName, Math.round(x), Math.round(y), xScale, yScale, depth, alpha));
		}
		catch (NullPointerException e) {
			System.out.println("Draw attempted before 'drawRequests' initialized");
			e.printStackTrace();
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
			if (allSprites.containsKey(request.spriteName())) {
				SpriteSheetRegion region = allSprites.get(request.spriteName());
				copySpriteRegionToFrame(region, request);
			}
		}
		// get rid of them all (we processed them all anyways)
		drawRequests.clear();

		// move the frame data to the image
		copyFrameToImage();

		// draw to the window
		panel.repaint();
	}

	private static void copySpriteRegionToFrame(SpriteSheetRegion sprite, DrawRequest request) {
		// all this to say, if the sprite does not overlap the image, do not draw it
		int actualX = request.x() - Math.round(sprite.originX() * request.xScale());
		int actualY = request.y() - Math.round(sprite.originY() * request.yScale());
		int actualX2 = actualX + Math.round(sprite.dataWidth() * request.xScale());
		int actualY2 = actualY + Math.round(sprite.dataHeight() * request.yScale());
		if (!GeneralUtil.areRectanglesIntersecting(actualX, actualY, actualX2, actualY2, 0, 0, MainClass.WINDOW_WIDTH - 1, MainClass.WINDOW_HEIGHT - 1)) {
			return;
		}

		// the sprite is at least partially on-screen

		SpriteSheetRegion scaledSprite = scaleImageData(sprite.data(), request.xScale(), request.yScale(), sprite.originX(), sprite.originY());
		int[][] spriteData = scaledSprite.data();
		float alphaPercent = Math.min(1, request.alpha());

		for (int y = 0; y < scaledSprite.dataHeight(); y++) {
			for (int x = 0; x < scaledSprite.dataWidth(); x++) {
				int calculatedX = request.x() + x - scaledSprite.originX();
				int calculatedY = request.y() + y - scaledSprite.originY();
				if (calculatedX < 0 || calculatedX >= image.getWidth() ||
						calculatedY < 0 || calculatedY >= image.getHeight()) {
					// if this pixel is not on-screen, do not attempt to draw it
					continue;
				}

				if ((spriteData[y][x] >> 24 & 0xFF) == 0xFF && alphaPercent == 1) {
					currentFrame[calculatedY][calculatedX] = spriteData[y][x];
				}
				else if ((spriteData[y][x] >> 24 & 0xFF) != 0x0 && alphaPercent > 0) {
					// basically takes a weighted average of the R, G, and B components of the sprite and the current frame, with the weight being the alpha of the sprite being drawn
					float percentage = ((float)(spriteData[y][x] >> 24 & 0xFF) / 0xFF) * alphaPercent;
					currentFrame[calculatedY][calculatedX] = 0xFF000000 | Math.min(0xFF, (int)((currentFrame[calculatedY][calculatedX] >> 16 & 0xFF) * (1 - percentage) + (spriteData[y][x] >> 16 & 0xFF) * percentage)) << 16 | Math.min(0xFF, (int)((currentFrame[calculatedY][calculatedX] >> 8 & 0xFF) * (1 - percentage) + (spriteData[y][x] >> 8 & 0xFF) * percentage)) << 8 | Math.min(0xFF, (int)((currentFrame[calculatedY][calculatedX] & 0xFF) * (1 - percentage) + (spriteData[y][x] & 0xFF) * percentage));
				}
			}
		}
	}

	private static void copyFrameToImage() {
		CompactFrameArray singleArray = GeneralUtil.arrayOfArraysToSingleArray(currentFrame);
		image.setRGB(0, 0, MainClass.WINDOW_WIDTH, MainClass.WINDOW_HEIGHT, singleArray.a(), 0, singleArray.dataWidth());
	}

	public static SpriteSheetRegion scaleImageData(int[][] data, float xScale, float yScale, int originX, int originY) {
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

		float runningTally = 0;
		int intTally;
		int newXOrigin = 0;
		int newX = data[0].length;
		if (xScale != 1) {
			newX = 0;
			newDataX = new int[data.length][Math.round(data[0].length * xScale)];
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
			newDataY = new int[Math.round(data.length * yScale)][newDataX[0].length];
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

	public static SpriteSheetRegion getSprite(String spriteName) {
		SpriteSheetRegion region = null;
		if (allSprites.containsKey(spriteName)) {
			region = allSprites.get(spriteName);
		}
		return region;
	}

	public static SpriteSheetRegion getSpriteScaled(String spriteName, float xScale, float yScale) {
		SpriteSheetRegion region = null;
		if (allSprites.containsKey(spriteName)) {
			region = allSprites.get(spriteName);
		}
		if (region != null) {
			return scaleImageData(region.data(), xScale, yScale, region.originX(), region.originY());
		}
		return null;
	}

	public static SpriteInformation getSpriteInfo(String spriteName) {
		SpriteInformation info = null;
		if (allSprites.containsKey(spriteName)) {
			SpriteSheetRegion tmp = allSprites.get(spriteName);
			info = new SpriteInformation(tmp.dataWidth(), tmp.dataHeight(), tmp.originX(), tmp.originY());
		}
		return info;
	}

	// TODO: check accuracy against getSpriteScaled
	public static SpriteInformation getScaledSpriteInfo(String spriteName, float xScale, float yScale) {
		SpriteInformation info = getSpriteInfo(spriteName);
		if (info != null) {
			int newOriginX;
			int newOriginY;
			int newDataWidth;
			int newDataHeight;
			if (xScale < 0) {
				newOriginX = Math.round(info.dataWidth() - info.originX() * -xScale);
				newDataWidth = Math.round(info.dataWidth() * -xScale);
			}
			else {
				newOriginX = Math.round(info.originX() * xScale);
				newDataWidth = Math.round(info.dataWidth() * xScale);
			}
			if (yScale < 0) {
				newOriginY = Math.round(info.dataHeight() - info.originY() * -yScale);
				newDataHeight = Math.round(info.dataHeight() * -yScale);
			}
			else {
				newOriginY = Math.round(info.originY() * yScale);
				newDataHeight = Math.round(info.dataHeight() * yScale);
			}
			return new SpriteInformation(newDataWidth, newDataHeight, newOriginX, newOriginY);
		}
		return null;
	}

	public static boolean isWindowClosed() {
		return windowClosed;
	}

	// NOTE: private classes and records

	private static class CustomPanel extends JPanel {
		@Override
		public void paint(Graphics g) {
			Graphics2D g2d = (Graphics2D)g;

			g2d.drawImage(image, 0, 0, MainClass.WINDOW_WIDTH, MainClass.WINDOW_HEIGHT, null);
		}
	}

	private record DrawRequest(String spriteName, int x, int y, float xScale, float yScale, int depth, float alpha) {}
}