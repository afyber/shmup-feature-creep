package afyber.shmupfeaturecreep.game;

public record EnemyWaveReference(String objectName, WaveProperties.Stage stage, double rating, EnemyTag[] enemyTags) {}
