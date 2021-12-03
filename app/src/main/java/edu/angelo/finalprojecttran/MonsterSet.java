package edu.angelo.finalprojecttran;

import java.util.ArrayList;
import java.util.List;

/**
 * Store and keep track of all the monster in the game. If a monster is killed, remove that monster's object.
 */
public class MonsterSet {
    /**
     * Store all of the monsters in this list.
     */
    private List<Monster> monsters;

    /**
     * Store the maximum number of monsters allow for the current level.
     */
    private int maxMonsters;

    /**
     * Default constructor to create a list of monsters and keep track of them.
     * @param monstersNum set the maximum number allows for the monsters in the list.
     */
    public MonsterSet(int monstersNum) {
        monsters = new ArrayList<>(monstersNum);
        this.maxMonsters = monstersNum;
    }

    /**
     * Add a monsters from the random generation in the World object to the List.
     * @param x the exact x position of the monster on the game's map.
     * @param y the exact y position of the monster on the game's map.
     */
    public void addMonsters(int x, int y) {
        if (monsters.size() < maxMonsters)
            monsters.add(new Monster(x, y));
    }

    /**
     * Cause damage to a monster get from the List, if that monster's health <= 0, destroy that monster's object and remove it from the game's map.
     * @param x the exact x position of the monster on the game's map.
     * @param y the exact y position of the monster on the game's map.
     * @param damage the damage deal to that monster at the fields[x][y] position on the game's map.
     * @param fields the game's map from the World object.
     * @return
     */
    public boolean damageMonster(int x, int y, int damage, int fields[][]) {
        //Check all of the monster and get the monster that has the exact position x, y.
        for (int i = 0; i < monsters.size(); ++i) {
            if (monsters.get(i).valueX() == x && monsters.get(i).valueY() == y) {
                //Deal damage to that monster.
                monsters.get(i).receiveDamage(damage);
                //if that monster's health <= 0, destroy that monster object and remove it from the game's map fields[][]
                if (monsters.get(i).heathAmount() <= 0) {
                    monsters.remove(i);
                    fields[x][y] = World.PATH;
                    return true;
                }

                return false;
            }
        }
        return false;
    }

    /**
     * Call each monsters from the List to attack.
     * @param spikes Store the spikes spawning from each monsters in the List.
     */
    public void advance(SpikeSet spikes) {
        for (int i = 0; i < monsters.size(); ++i) {
            monsters.get(i).attack(spikes);
        }
    }
}
