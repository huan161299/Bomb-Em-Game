package edu.angelo.finalprojecttran;

/**
 * An object that deals damage to the monster and the player also, a mighty weapon!
 */
public class Bomb {
    /**
     * Position x of the bomb object on the game's map.
     */
    private int x;

    /**
     * Position y of the bomb object on the game's map.
     */
    private int y;

    /**
     * Bomb exploding radius (counting from the bomb position itself).
     */
    private int radius = 2;

    /**
     * The damage the bomb deals when exploding.
     */
    private final int damage = 1;

    /**
     * Default constructor to create a bomb at the indication position x,y on the map fields[][]
     * @param x position x on the map
     * @param y position y on the map.
     */
    public Bomb(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * @return current X position on the map.
     */
    public int valueX() {
        return x;
    }

    /**
     * @return current Y position on the map.
     */
    public int valueY() {
        return y;
    }

    /**
     * @return return the deal damage amount of this bomb.
     */
    public int damageAmount() {
        return damage;
    }

    /**
     * @return the bomb exploding radius.
     */
    public int bombRadius() {
        return radius;
    }
}
