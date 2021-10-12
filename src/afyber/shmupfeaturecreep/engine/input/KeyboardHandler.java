package afyber.shmupfeaturecreep.engine.input;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

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
			case KeyEvent.VK_Z: yield "z";
			case KeyEvent.VK_X: yield "x";
			case KeyEvent.VK_C: yield "c";
			default: yield null;
		};
	}
}
