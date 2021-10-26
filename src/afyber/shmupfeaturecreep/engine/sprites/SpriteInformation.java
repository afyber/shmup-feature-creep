package afyber.shmupfeaturecreep.engine.sprites;

// IMPORTANT: from now on dataBeginX and dataWidth ALWAYS refers to the x value/width in pixels TIMES 4!
// HOWEVER: originX DOES NOT, and refers to the origin in pixels
public record SpriteInformation(int dataBeginX, int dataBeginY, int dataWidth, int dataHeight, int originX, int originY) {}
