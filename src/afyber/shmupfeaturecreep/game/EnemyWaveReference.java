package afyber.shmupfeaturecreep.game;

public record EnemyWaveReference(String objectName, WaveProperties.Stage stage, int batch, double rating, EnemyTag[] enemyTags) {}
