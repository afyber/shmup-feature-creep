package afyber.shmupfeaturecreep.engine.rooms;

import java.util.ArrayList;

public record Room(ArrayList<StaticObject> tiles, ArrayList<ObjectCreationReference> objects) {}
