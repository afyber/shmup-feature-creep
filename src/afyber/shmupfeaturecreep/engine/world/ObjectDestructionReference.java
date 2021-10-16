package afyber.shmupfeaturecreep.engine.world;

import afyber.shmupfeaturecreep.engine.rooms.ObjectReference;

public record ObjectDestructionReference(boolean useInstanceID, ObjectReference reference, Class objClass) {}
