package afyber.shmupfeaturecreep.engine.sound.javasound;

import afyber.shmupfeaturecreep.MainClass;

import javax.sound.sampled.*;
import java.io.IOException;

public class JavaSound {
	private JavaSound() {}

	public static void test() {
		try {
			AudioInputStream input = AudioSystem.getAudioInputStream(MainClass.class.getResourceAsStream("/sounds/test.wav"));

			Clip clip = AudioSystem.getClip();

			clip.open(input);

			clip.start();
		}
		catch (IOException e) {

		}
		catch (UnsupportedAudioFileException e) {

		}
		catch (LineUnavailableException e ) {

		}
	}
}
