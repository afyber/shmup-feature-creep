package afyber.shmupfeaturecreep.game;

public record EnemyWaveReference(String objectName, Wave.Stage stage, double rating, EnemyTag[] enemyTags) {}
