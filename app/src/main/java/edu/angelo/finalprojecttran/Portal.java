package edu.angelo.finalprojecttran;

/**
 * The object that allows player to transition to a new level.
 */
public class Portal {

    /**
     * The exact position x of the portal on the game's map.
     */
    private int x;

    /**
     * The exact position y of the portal on the game's map.
     */
    private int y;

    /**
     * Default constructor to create a new portal at the indicate position.
     * @param x exact position x on the game's map.
     * @param y exact position y on the game's map.
     */
    public Portal(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * @return exact position x on the game's map.
     */
    public int getX() {
        return x;
    }

    /**
     * @return exact position y on the game's map.
     */
    public int getY() {
        return y;
    }
}
