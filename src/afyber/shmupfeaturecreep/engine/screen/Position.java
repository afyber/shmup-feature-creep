package afyber.shmupfeaturecreep.engine.screen;

public class Position {

	private final int[] vector = new int[2];

	public Position(int x, int y) {
		vector[0] = x;
		vector[1] = y;
	}

	public int getX() {
		return vector[0];
	}

	public int getY() {
		return vector[1];
	}

	public void rotate(double angle) {
		double r = Math.sqrt(Math.pow(vector[0], 2) + Math.pow(vector[1], 2));
		if (Math.abs(r) < 0.000001) {
			return;
		}

		while (angle >= 360) {
			angle -= 360;
		}

		if (Math.abs(angle) < 0.000001) {
			return;
		}

		double t = Math.toDegrees(Math.atan(vector[1] / (double)vector[0]));

		if (t < 0) {
			if (vector[0] < 0) {
				t += 180;
			}
			else {
				t += 360;
			}
		}
		else if (vector[0] < 0) {
			t += 180;
		}

		t += angle;
		vector[0] = (int)Math.round(r * Math.cos(Math.toRadians(t)));
		vector[1] = (int)Math.round(r * Math.sin(Math.toRadians(t)));
	}
}
