package afyber.shmupfeaturecreep.engine.input;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashMap;

/**
 * This class is the KeyListener attached to the custom JFrame
 *
 * @author afyber
 */
public class KeyboardHandler implements KeyListener {
	
	private final HashMap<Integer, String> keyMap = new HashMap<>();
	
	public KeyboardHandler() {
		super();
		setupKeyMap();
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// not concerned with this
	}

	@Override
	public void keyPressed(KeyEvent e) {
		int keyCode = e.getKeyCode();

		Keyboard.queueKeyDown(getKeyName(keyCode));
	}

	@Override
	public void keyReleased(KeyEvent e) {
		int keyCode = e.getKeyCode();

		Keyboard.queueKeyUp(getKeyName(keyCode));
	}

	private String getKeyName(int keyCode) {
		return keyMap.get(keyCode);
	}
	
	private void setupKeyMap() {
		keyMap.put(KeyEvent.VK_UP, "up");
		keyMap.put(KeyEvent.VK_DOWN, "down");
		keyMap.put(KeyEvent.VK_LEFT, "left");
		keyMap.put(KeyEvent.VK_RIGHT, "right");
		keyMap.put(KeyEvent.VK_ESCAPE, "escape");
		keyMap.put(KeyEvent.VK_ENTER, "enter");
		keyMap.put(KeyEvent.VK_BACK_SPACE, "backspace");
		keyMap.put(KeyEvent.VK_COMMA, "comma");
		keyMap.put(KeyEvent.VK_PERIOD, "period");
		keyMap.put(KeyEvent.VK_SLASH, "slash");
		keyMap.put(KeyEvent.VK_BACK_SLASH, "backslash");
		keyMap.put(KeyEvent.VK_SEMICOLON, "semicolon");
		keyMap.put(KeyEvent.VK_1, "1");
		keyMap.put(KeyEvent.VK_2, "2");
		keyMap.put(KeyEvent.VK_3, "3");
		keyMap.put(KeyEvent.VK_4, "4");
		keyMap.put(KeyEvent.VK_5, "5");
		keyMap.put(KeyEvent.VK_6, "6");
		keyMap.put(KeyEvent.VK_7, "7");
		keyMap.put(KeyEvent.VK_8, "8");
		keyMap.put(KeyEvent.VK_9, "9");
		keyMap.put(KeyEvent.VK_0, "0");
		keyMap.put(KeyEvent.VK_Q, "q");
		keyMap.put(KeyEvent.VK_W, "w");
		keyMap.put(KeyEvent.VK_E, "e");
		keyMap.put(KeyEvent.VK_R, "r");
		keyMap.put(KeyEvent.VK_T, "t");
		keyMap.put(KeyEvent.VK_Y, "y");
		keyMap.put(KeyEvent.VK_U, "u");
		keyMap.put(KeyEvent.VK_I, "i");
		keyMap.put(KeyEvent.VK_O, "o");
		keyMap.put(KeyEvent.VK_P, "p");
		keyMap.put(KeyEvent.VK_A, "a");
		keyMap.put(KeyEvent.VK_S, "s");
		keyMap.put(KeyEvent.VK_D, "d");
		keyMap.put(KeyEvent.VK_F, "f");
		keyMap.put(KeyEvent.VK_G, "g");
		keyMap.put(KeyEvent.VK_H, "h");
		keyMap.put(KeyEvent.VK_J, "j");
		keyMap.put(KeyEvent.VK_K, "k");
		keyMap.put(KeyEvent.VK_L, "l");
		keyMap.put(KeyEvent.VK_Z, "z");
		keyMap.put(KeyEvent.VK_X, "x");
		keyMap.put(KeyEvent.VK_C, "c");
		keyMap.put(KeyEvent.VK_V, "v");
		keyMap.put(KeyEvent.VK_B, "b");
		keyMap.put(KeyEvent.VK_N, "n");
		keyMap.put(KeyEvent.VK_M, "m");
	}
}
