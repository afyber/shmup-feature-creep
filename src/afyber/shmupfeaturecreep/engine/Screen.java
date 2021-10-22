package afyber.shmupfeaturecreep.engine;

import afyber.shmupfeaturecreep.MainClass;
import afyber.shmupfeaturecreep.engine.input.KeyboardHandler;
import afyber.shmupfeaturecreep.engine.sprites.SpriteSheet;
import afyber.shmupfeaturecreep.engine.sprites.SpriteSheetRegion;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

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

	private static boolean closed;

	private static ArrayList<DrawRequest> drawRequests;

	private static ArrayList<SpriteSheet> allSpriteSheets;

	public static void setupScreen(String name, int width, int height) {
		// Window setup
		image = new BufferedImage(width, height, Image.SCALE_DEFAULT);
		frame = new JFrame() {
			@Override
			public void dispose() {
				super.dispose();
				if (MainClass.DEBUG)
					System.out.println("Window exit");
				closed = true;
			}
		};
		closed = false;
		panel = new CustomPanel();
		panel.addKeyListener(new KeyboardHandler());
		panel.setFocusable(true);
		frame.add(panel);
		frame.setResizable(false);
		frame.setTitle(name);
		frame.setSize(width, height);
		frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		frame.setVisible(true);

		drawRequests = new ArrayList<>();

		allSpriteSheets = new ArrayList<>();
		allSpriteSheets.add(new SpriteSheet("spritesheets/test6"));
		allSpriteSheets.add(new SpriteSheet("spritesheets/spritesheet1"));
	}

	public static void draw(String spriteName, float x, float y, int depth) {
		draw(spriteName, x, y, 1, 1, depth);
	}
	public static void draw(String spriteName, float x, float y, float xScale, float yScale, int depth) {
		try {
			drawRequests.add(new DrawRequest(spriteName, Math.round(x), Math.round(y), xScale, yScale, depth));
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
		for (int y = 0; y < image.getHeight(); y++) {
			for (int x = 0; x < image.getWidth(); x++) {
				image.setRGB(x, y, rgbColor);
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
			for (SpriteSheet spriteSheet: allSpriteSheets) {
				if (spriteSheet.hasSprite(request.spriteName())) {
					SpriteSheetRegion region = spriteSheet.getSprite(request.spriteName());
					copySpriteRegionToImage(region, request);
					break;
				}
			}
		}
		// get rid of them all (we processed them all anyways)
		drawRequests.clear();

		// draw to the window
		panel.repaint();
	}

	private static void copySpriteRegionToImage(SpriteSheetRegion sprite, DrawRequest request) {
		// all this to say, if the sprite does not overlap the image, do not draw it
		int actualX = request.x() - Math.round(sprite.originX() * request.xScale());
		int actualY = request.y() - Math.round(sprite.originY() * request.yScale());
		int actualX2 = actualX + Math.round(sprite.dataWidth() * request.xScale());
		int actualY2 = actualY + Math.round(sprite.dataHeight() * request.yScale());
		if ((actualX < 0 || actualX >= image.getWidth() || actualY < 0 || actualY >= image.getHeight()) &&
				(actualX2 < 0 || actualX2 >= image.getWidth() || actualY2 < 0 || actualY2 >= image.getHeight())) {
			return;
		}

		// the sprite is at least partially on-screen
		SpriteSheetRegion scaledSprite = scaleImageData(sprite.data(), request.xScale(), request.yScale(), sprite.originX(), sprite.originY());
		byte[][] spriteData = scaledSprite.data();
		for (int y = 0; y < scaledSprite.dataHeight(); y++) {
			for (int x = 0; x < scaledSprite.dataWidth() / 4; x++) {
				int calculatedX = request.x() + x - scaledSprite.originX();
				int calculatedY = request.y() + y - scaledSprite.originY();
				if (calculatedX < 0 || calculatedX >= image.getWidth() ||
						calculatedY < 0 || calculatedY >= image.getHeight()) {
					// if this pixel is not on-screen, do not attempt to draw it
					continue;
				}

				if (Byte.toUnsignedInt(spriteData[y][x * 4 + 3]) == 0xFF) {
					image.setRGB(calculatedX, calculatedY, 0xFF000000 | Byte.toUnsignedInt(spriteData[y][x * 4]) << 16 | Byte.toUnsignedInt(spriteData[y][x * 4 + 1]) << 8 | Byte.toUnsignedInt(spriteData[y][x * 4 + 2]));
				}
				else if (Byte.toUnsignedInt(spriteData[y][x * 4 + 3]) != 0) {
					// TODO: apply transparency
				}
			}
		}
	}

	public static SpriteSheetRegion scaleImageData(byte[][] data, float xScale, float yScale, int originX, int originY) {
		// TODO: properly figure out scaling to prevent annoying jerkiness when constantly changing scale
		if (xScale == 1 && yScale == 1) {
			return new SpriteSheetRegion(data, data[0].length, data.length, originX, originY);
		}
		else if (xScale == 0 || yScale == 0) {
			return new SpriteSheetRegion(new byte[1][4], 4, 1, 0, 0);
		}

		boolean xNegative = xScale < 0;
		if (xNegative) {
			xScale = -xScale;
		}
		boolean yNegative = yScale < 0;
		if (yNegative) {
			yScale = -yScale;
		}

		byte[][] newDataX;

		float runningTally = 0;
		int intTally;
		int newXOrigin = 0;
		int newX = data[0].length / 4;
		if (xScale != 1) {
			newX = 0;
			newDataX = new byte[data.length][Math.round(data[0].length / 4 * xScale) * 4];
			for (int oldX = 0; oldX < data[0].length; oldX += 4) {
				if (oldX / 4 == originX) {
					newXOrigin = newX;
				}
				runningTally += xScale;
				intTally = (int)runningTally;
				if (intTally > 0) {
					for (int y = 0; y < data.length; y++) {
						for (int i = 0; i < intTally; i++) {
							newDataX[y][newX * 4 + i * 4] = data[y][oldX];
							newDataX[y][newX * 4 + i * 4 + 1] = data[y][oldX + 1];
							newDataX[y][newX * 4 + i * 4 + 2] = data[y][oldX + 2];
							newDataX[y][newX * 4 + i * 4 + 3] = data[y][oldX + 3];
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

		byte[][] newDataY;

		int newYOrigin = 0;
		int newY = data.length;
		if (yScale != 1) {
			newY = 0;
			newDataY = new byte[Math.round(data.length * yScale)][newDataX[0].length];
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
			newXOrigin = newDataY[0].length / 4 - newXOrigin;
		}
		if (yNegative) {
			GeneralUtil.reverseTopSpriteArray(newDataY);
			newYOrigin = newDataY.length - newYOrigin;
		}

		return new SpriteSheetRegion(newDataY, newX * 4, newY, newXOrigin, newYOrigin);
	}

	public static SpriteSheetRegion getSpriteScaled(String spriteName, float xScale, float yScale) {
		SpriteSheetRegion region = null;
		for (SpriteSheet spriteSheet: allSpriteSheets) {
			if (spriteSheet.hasSprite(spriteName)) {
				region = spriteSheet.getSprite(spriteName);
				break;
			}
		}
		if (region != null) {
			return scaleImageData(region.data(), xScale, yScale, region.originX(), region.originY());
		}
		return null;
	}

	public static boolean isWindowClosed() {
		return closed;
	}

	// NOTE: private classes and records

	private static class CustomPanel extends JPanel {
		@Override
		public void paint(Graphics g) {
			Graphics2D g2d = (Graphics2D)g;

			g2d.drawImage(image, 0, 0, MainClass.WINDOW_WIDTH, MainClass.WINDOW_HEIGHT, null);
		}
	}

	private record DrawRequest(String spriteName, int x, int y, float xScale, float yScale, int depth) {}

	private record ScaledSpriteInfo(byte[][] data, int originX, int originY) {}
}