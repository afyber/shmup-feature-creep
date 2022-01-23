package afyber.shmupfeaturecreep.game;

public record EnemyWaveSlot(double minRating, double maxRating, TagReference[] tags, EnemyTimeSpacePosition[] positions) {}
