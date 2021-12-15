package afyber.shmupfeaturecreep.engine.input;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * This class is the KeyListener attached to the custom JFrame
 *
 * @author afyber
 */
public class KeyboardHandler implements KeyListener {

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
		return switch(keyCode) {
			case KeyEvent.VK_UP: yield "up";
			case KeyEvent.VK_DOWN: yield "down";
			case KeyEvent.VK_LEFT: yield "left";
			case KeyEvent.VK_RIGHT: yield "right";
			case KeyEvent.VK_ESCAPE: yield "escape";
			case KeyEvent.VK_ENTER: yield "enter";
			case KeyEvent.VK_BACK_SPACE: yield "backspace";
			case KeyEvent.VK_COMMA: yield "comma";
			case KeyEvent.VK_PERIOD: yield "period";
			case KeyEvent.VK_SLASH: yield "slash";
			case KeyEvent.VK_BACK_SLASH: yield "backslash";
			case KeyEvent.VK_SEMICOLON: yield "semicolon";
			case KeyEvent.VK_1: yield "1";
			case KeyEvent.VK_2: yield "2";
			case KeyEvent.VK_3: yield "3";
			case KeyEvent.VK_4: yield "4";
			case KeyEvent.VK_5: yield "5";
			case KeyEvent.VK_6: yield "6";
			case KeyEvent.VK_7: yield "7";
			case KeyEvent.VK_8: yield "8";
			case KeyEvent.VK_9: yield "9";
			case KeyEvent.VK_0: yield "0";
			case KeyEvent.VK_Q: yield "q";
			case KeyEvent.VK_W: yield "w";
			case KeyEvent.VK_E: yield "e";
			case KeyEvent.VK_R: yield "r";
			case KeyEvent.VK_T: yield "t";
			case KeyEvent.VK_Y: yield "y";
			case KeyEvent.VK_U: yield "u";
			case KeyEvent.VK_I: yield "i";
			case KeyEvent.VK_O: yield "o";
			case KeyEvent.VK_P: yield "p";
			case KeyEvent.VK_A: yield "a";
			case KeyEvent.VK_S: yield "s";
			case KeyEvent.VK_D: yield "d";
			case KeyEvent.VK_F: yield "f";
			case KeyEvent.VK_G: yield "g";
			case KeyEvent.VK_H: yield "h";
			case KeyEvent.VK_J: yield "j";
			case KeyEvent.VK_K: yield "k";
			case KeyEvent.VK_L: yield "l";
			case KeyEvent.VK_Z: yield "z";
			case KeyEvent.VK_X: yield "x";
			case KeyEvent.VK_C: yield "c";
			case KeyEvent.VK_V: yield "v";
			case KeyEvent.VK_B: yield "b";
			case KeyEvent.VK_N: yield "n";
			case KeyEvent.VK_M: yield "m";
			default: yield null;
		};
	}
}
