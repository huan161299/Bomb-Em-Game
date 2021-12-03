package edu.angelo.finalprojecttran;

public class Spike {
    /**
     * Indicate the damage deal by the spike.
     */
    private static int damage = 1;

    /**
     * Check if this object is still exist (still flying ~). If this object no longer exist, the SpikeSet will destroy it.
     */
    private boolean objectExist;

    /**
     * The exact location of the spike on the game screen.
     */
    public int x, y;

    /**
     * Indicate which direction is this current Spike flying.
     */
    public int direction;

    /**
     * Default constructor to create a Spike object, it MUST indicate which direction it is flying to (so that its position can be updated accordingly).
     * @param x the exact location X on the screen.
     * @param y the exact location Y on the screen
     * @param direction the direction UP / DOWN / LEFT / RIGHT of this projectile.
     */
    public Spike(int x, int y, int direction) {
        this.x = x;
        this.y = y;
        this.direction = direction;
        //Update so that this object is still exist.
        objectExist = true;
    }

    /**
     * @return true or false whether this object still exist or not.
     */
    public boolean isObjectExist() {
        return objectExist;
    }

    /**
     * @return the approximate location X of this object on the game's map fields[][]
     */
    private int mapLocationX() {
        return Math.round((float)(x - 16) / 32);
    }

    /**
     * @return the approximate location Y of this object on the game's map fields[][]
     */
    private int mapLocationY() {
        return Math.round((float)(y - 16) / 32);
    }

    /**
     * Update the location of the current Spike. Make it interact with the Player (deal damage) or with the object in the game's map fields[][]
     * @param fields the game map from the World object.
     * @param mainPlayer the Player object from the World object.
     */
    public void advance(int fields[][], Player mainPlayer) {
        //Update the location if the Spike goes UP.
        if (direction == World.UP) {
            y -= World.spikeSpeed;

            //If hit the player, then deal Damage.
            if (mapLocationX() == mainPlayer.mapLocationX() && mapLocationY() == mainPlayer.mapLocationY()) {
                mainPlayer.receiveDamage(damage);
                objectExist = false;
            }
            else try {
                //Object reach boundary / box / wall / bomb --> Destroy object.
                if (y < 80 || fields[mapLocationX()][mapLocationY() - 1] > 0 || fields[mapLocationX()][mapLocationY() - 1] == World.BOMB) objectExist = false;
            }
            catch (ArrayIndexOutOfBoundsException exception) {
                //do nothing.
            }
        }
        //Update the location if the Spike goes LEFT.
        else if (direction == World.LEFT) {
            x -= World.spikeSpeed;
            //If hit the player, then deal 1 receiveDamage.
            if (mapLocationX() == mainPlayer.mapLocationX() && mapLocationY() == mainPlayer.mapLocationY()) {
                mainPlayer.receiveDamage(damage);
                objectExist = false;
            }
            else try {
                //Object reach boundary / box / wall / bomb --> Destroy object.
                if (x < 16 || fields[mapLocationX() - 1][mapLocationY()] > 0 || fields[mapLocationX() - 1][mapLocationY()] == World.BOMB) objectExist = false;
            }
            catch (ArrayIndexOutOfBoundsException exception) {
                //do nothing.
            }
        }
        //Update the location if the Spike goes RIGHT.
        else if (direction == World.RIGHT) {
            x += World.spikeSpeed;
            //If hit the player, then deal 1 receiveDamage.
            if (mapLocationX() == mainPlayer.mapLocationX() && mapLocationY() == mainPlayer.mapLocationY()) {
                mainPlayer.receiveDamage(damage);
                objectExist = false;
            }
            else try {
                //Object reach boundary / box / wall / bomb --> Destroy object.
                if (x > 304 || fields[mapLocationX() + 1][mapLocationY()] > 0 || fields[mapLocationX() + 1][mapLocationY()] == World.BOMB) objectExist = false;
            }
            catch (ArrayIndexOutOfBoundsException exception) {
                //do nothing.
            }
        }
        //Update the location if the Spike goes DOWN.
        else if (direction == World.DOWN) {
            y += World.spikeSpeed;
            //If hit the player, then deal 1 receiveDamage.
            if (mapLocationX() == mainPlayer.mapLocationX() && mapLocationY() == mainPlayer.mapLocationY()) {
                mainPlayer.receiveDamage(damage);
                objectExist = false;
            }
            else try {
                //Object reach boundary / box / wall / bomb --> Destroy object.
                if (y > 368 || fields[mapLocationX()][mapLocationY() + 1] > 0 || fields[mapLocationX()][mapLocationY() + 1] == World.BOMB) objectExist = false;
            }
            catch (ArrayIndexOutOfBoundsException exception) {
                //do nothing.
            }
        }
    }
}
