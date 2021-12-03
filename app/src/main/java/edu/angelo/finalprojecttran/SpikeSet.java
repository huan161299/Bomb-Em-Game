package edu.angelo.finalprojecttran;

import java.util.ArrayList;
import java.util.List;

/**
 * Store all of the Spike that spawn from the monsters. Keep track and destroy any spike that has been used / destroy by collision in the game's map.
 */
public class SpikeSet {
    /**
     * List of Spike object that store from the monster's attack.
     */
    List<Spike> spikes;

    /**
     * Default constructor that create a new Spike List.
     */
    public SpikeSet() {
        spikes = new ArrayList<>();
    }

    /**
     * Add a Spike with direction from the monster's attack into the list.
     * @param x The exact x location of the spike on the game's screen (not map).
     * @param y The exact y location of the spike on the game's screen (not map).
     * @param direction the direction that the Spike is flying toward (UP, DOWN, LEFT, RIGHT).
     */
    public void addSpike(int x, int y, int direction) {
        spikes.add(new Spike(x, y, direction));
    }

    /**
     * @return how many spike are left in the list, use this to draw all the spike out on the game screen.
     */
    public int spikeSize() {
        return spikes.size();
    }

    /**
     * @param pos the current position of the Spike in the list.
     * @return that exact Spike from the List.
     */
    public Spike spikeAt(int pos) {
        return spikes.get(pos);
    }

    /**
     * Make every spikes fly and interact with the Player / Object in the game's map.
     * @param fields the game's map from the World object.
     * @param mainPlayer the Player object from the World object.
     */
    public void advance(int[][] fields, Player mainPlayer) {
        for (int i = 0; i < spikes.size(); ++i) {
            //make it fly and interact with the map's object / player.
            spikes.get(i).advance(fields, mainPlayer);
            //After advance, if that object is no longer exist, destroy that object.
            if (!spikes.get(i).isObjectExist()) {
                spikes.remove(i);
                --i;
            }
        }
    }
}
