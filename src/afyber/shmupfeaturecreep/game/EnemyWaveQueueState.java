package afyber.shmupfeaturecreep.game;

public class EnemyWaveQueueState {

	public final String objectName;
	public final double x;
	public final double y;
	public double frames;

	public EnemyWaveQueueState(String objectName, double x, double y, int frames) {
		this.objectName = objectName;
		this.x = x;
		this.y = y;
		this.frames = frames;
	}
}
