package edu.angelo.finalprojecttran;

/**
 * In details for the introduction please read the BombEm file.
 */
public class Player {
    /**
     * Store the default health upon creating a new Player object.
     */
    static final int DEFAULT_HEALTH = 5;

    /**
     * Store the current player's health.
     */
    private int health;

    /**
     * Indicate the player's movement speed.
     */
    private int speed = 8;

    /**
     * Indicate the exact player's position on the game screen (to draw it out).
     */
    private int x, y;

    /**
     * Indicate the STAND_STILL action of the player' object.
     */
    public static final int STAND_STILL = -1;

    /**
     * Indicate which direction is the player moving to update the Player's position accordingly.
     */
    public int direction;

    /**
     * Default constructor for the player's object. Define default health and make the player direction is stand still.
     */
    public Player() {
        health = DEFAULT_HEALTH;
        standStill();
    }

    /**
     * Get the current player's health.
     * @return the player's health.
     */
    public int getHealth() {
        return health;
    }

    /**
     * Increase the player's health by an extraHealth amount.
     * @param extraHealth
     */
    public void receiveHealth(int extraHealth) {health += extraHealth;}

    /**
     * Reduced the player's health by a damage amount.
     * @param damage
     */
    public void receiveDamage(int damage) {
        health -= damage;
        if (Settings.soundEnabled)
                Assets.damaged.play(1);
    }

    /**
     * Set the default spawn location of the player.
     * @param x
     * @param y
     */
    public void setDrawLocation(int x, int y) {
        this.x = x;
        this.y = y;
    }

    //Position to draw on screen.
    public int drawX() {
        return x;
    }

    //Position to draw on screen.
    public int drawY() {
        return y;
    }

    /**
     * Make the player's movement is stand still.
     */
    public void standStill() {
        direction = STAND_STILL;
    }

    /**
     * Update the player's movement to go UP.
     */
    public void moveUp() {
        direction = World.UP;
    }

    /**
     * Update the player's movement to go LEFT.
     */
    public void moveLeft() {
        direction = World.LEFT;
    }

    /**
     * Update the player's movement to go RIGHT.
     */
    public void moveRight() {
        direction = World.RIGHT;
    }

    /**
     * Update the player's movement to go DOWN.
     */
    public void moveDown() {
        direction = World.DOWN;
    }

    /**
     * Return the approximate X location on the map fields[][] of the player from the exact drawing location (x).
     * @return x location of the player on the map.
     */
    public int mapLocationX() {
        return Math.round((float)(x - 16) / 32);
    }

    /**
     * Return the approximate Y location on the map fields[][] of the player from the exact drawing location (y).
     * @return Y location of the player on the map.
     */
    public int mapLocationY() {
        return Math.round((float)(y - 16) / 32);
    }

    /**
     * Make and update the player's movement.
     * @param fields used to check for the surrounding object. The player can only go through an empty PATH, BOMB, HEALTH_BOOSTER, and PLAYER_SPAWN (all of those object are indicate <= 0 on the map default object declaration).
     */
    public void advance(int fields[][]) {
        //If the player goes UP, --y.
        if (direction == World.UP) {
            y -= speed;
            //If the player goes out of bound, stop the player's movement at the boundary.
            if (y < 80) y = 80;
            else try {
                //If there is an object > 0 on the path of the player, or the player is more than 20 pixel away to go in that direction, stop the movement of the player.
                if (fields[mapLocationX()][mapLocationY() - 1] > 0 || (Math.abs(x - (mapLocationX() * 32 + 16)) > 20)) y = mapLocationY() * 32 + 16;
                else {
                    //Update the X location accordingly to center the player in the square so that the player can move on that direction.
                    x = mapLocationX() * 32 + 16;
                }
            }
            catch (ArrayIndexOutOfBoundsException exception) {
                //do nothing.
            }
        }
        //If the player goes LEFT, --x.
        else if (direction == World.LEFT) {
            x -= speed;
            //If the player goes out of bound, stop the player's movement at the boundary.
            if (x < 16) x = 16;
            else try {
                //If there is an object > 0 on the path of the player, or the player is more than 20 pixel away to go in that direction, stop the movement of the player.
                if (fields[mapLocationX() - 1][mapLocationY()] > 0 || (Math.abs(y - (mapLocationY() * 32 + 16)) > 20)) x = mapLocationX() * 32 + 16;
                else {
                    //Update the Y location accordingly to center the player in the square so that the player can move on that direction.
                    y = mapLocationY() * 32 + 16;
                }
            }
            catch (ArrayIndexOutOfBoundsException exception) {
                //do nothing.
            }
        }
        //If the player goes RIGHT, ++x.
        else if (direction == World.RIGHT) {
            x += speed;
            //If the player goes out of bound, stop the player's movement at the boundary.
            if (x > 304) x = 304;
            else try {
                //If there is an object > 0 on the path of the player, or the player is more than 20 pixel away to go in that direction, stop the movement of the player.
                if (fields[mapLocationX() + 1][mapLocationY()] > 0 || (Math.abs(y - (mapLocationY() * 32 + 16)) > 20)) x = mapLocationX() * 32 + 16;
                else {
                    //Update the Y location accordingly to center the player in the square so that the player can move on that direction.
                    y = mapLocationY() * 32 + 16;
                }
            }
            catch (ArrayIndexOutOfBoundsException exception) {
                //do nothing.
            }
        }
        //If the player goes DOWN, ++y.
        else if (direction == World.DOWN) {
            y += speed;
            //If the player goes out of bound, stop the player's movement at the boundary.
            if (y > 368) y = 368;
            else try {
                //If there is an object > 0 on the path of the player, or the player is more than 20 pixel away to go in that direction, stop the movement of the player.
                if (fields[mapLocationX()][mapLocationY() + 1] > 0 || (Math.abs(x - (mapLocationX() * 32 + 16)) > 20)) y = mapLocationY() * 32 + 16;
                else {
                    //Update the X location accordingly to center the player in the square so that the player can move on that direction.
                    x = mapLocationX() * 32 + 16;
                }
            }
            catch (ArrayIndexOutOfBoundsException exception) {
                //do nothing.
            }
        }

        //If player step on a health pack, increase life.
        if (fields[mapLocationX()][mapLocationY()] == World.HEALTH_BOOSTER) {
            receiveHealth(World.HEALTH_BOOST_AMOUNT);
            //Remove that health pack from the map.
            fields[mapLocationX()][mapLocationY()] = World.PATH;
        }
    }
}
