package edu.angelo.finalprojecttran;

import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import java.util.Random;

/**
 * @author Huan Tran
 * ID: 81383734
 *
 * All of the codes in this project are programmed only and only by me. There is no help I attain to during the creation of this project.
 * Inspired from the orginial game content: Bomberman (one of my childhood game on GameBoy console).
 * I consult instruction for the CountDownTimer in this article: "https://stackoverflow.com/questions/32283811/cant-create-handler-inside-thread-that-has-not-called-looper-prepare-in-count"
 *
 * This game use a system to store all of the object on the game screen in a 2D array fields[][] (BOMB, BOX, WALL, MONSTER, PORTAL, and HEALTH_BOOSTER. All of the object store in the map is later on
 *      multiple with 32 pixel so that it can be later on draw on the game UI.
 * There are 2 object that does not bound to the game's map (which make the code getting really complex, but is necessary to make the object move as an arcade), that is: Player object and the Spike object.
 * They have their own x, y location on the screen (so that their movement and location can be updated accordingly on the map), and to make them interactable in the game map. I create two function: mapLocationX() and mapLocationY()
 *      so that their approximate location can be used on the map.
 * In term of efficiency, all of the objects that can be interactable are declared in a seperate script (Player, Monster, Spike, Portal). This means that there is no need to declare a script for the Box and Wall object.
 *      The MonsterSet will keep track and update all of the activity of the monsters on the screen, while the SpikeSet will keep track of all the Spike shooting out from the monster on the screen, and will destroy any Spike
 *      that had caused collision with either the Player, or another object on the game's map fields[][].
 * --> This make the code more reusable, and make it easier to keep track of all the object (and its interaction) to avoid memory leak.
 * The only object that does not have it own script, is HEALTH_BOOSTER. Since I choose to make them really limited (only 1), there is no need to code a seperate file out and keep track of it. Though, even if the Portal object only have one, it is
 *      a one time use object, and will make the game transition to the next level, thus I do not need to destroy it.
 *
 * Note: I apologize if there is any code section that is missing comment, this project is really huge (for me at least alone).
 *
 * Please reach out to me if there is any part of the code you do not understand, but I am really sure that I code all of them myself *smile face* and I am proud of myself to have create such a game. Hopefully you will enjoy it!
 */
public class World {

    /**
     * Indicate the game' upper limit line to draw the map out. (The game will start from line 2.)
     */
    final static int UPPER_LIMIT = 2;

    /**
     * The world's width on map.
     */
    final static int WORLD_WIDTH = 10;

    /**
     * The world's height limit on map.
     */
    final static int WORLD_HEIGHT = 12;

    /**
     * Default UP value.
     */
    final static int UP = 0;

    /**
     * Default LEFT value.
     */
    final static int LEFT = 1;

    /**
     * Default RIGHT value.
     */
    final static int RIGHT = 2;

    /**
     * Default DOWN value.
     */
    final static int DOWN = 3;

    /**
     * random variable to generate maps and monsters.
     */
    private Random random = new Random();

    /**
     * TICK_INITIAL marks the initial speed of the game: the higher the value, the slower the game.
     */
    static final float TICK_INITIAL = 0.1f;

    /**
     * Indicate the tick time (the running time) of the objects ingame.
     */
    float tickTime = 0;

    /**
     * Default tick time (speed) of the game.
     */
    float tick = TICK_INITIAL;

    /**
     * Indicate if the game is over or not (the player lost all health).
     */
    public boolean gameOver = false;

    /**
     * Indicate the score of the player.
     */
    public int score = 0;

    /**
     * Default value of the PATH object on the map.
     */
    final static int PATH = 0;

    /**
     * Default value of the PLAYER_SPAWN object on the map.
     */
    final static int PLAYER_SPAWN = -1;

    /**
     * Default value of the PORTAL object on the map.
     */
    final static int PORTAL = -2;

    /**
     * Default value of the BOMB object on the map.
     */
    final static int BOMB = -3;

    /**
     * Default value of the FIRE object on the map.
     */
    final static int FIRE = -4;

    /**
     * Default value of the HEALTH_BOOSTER pack object on the map.
     */
    final static int HEALTH_BOOSTER = -5;

    /**
     * Default value of the WALL object on the map.
     */
    final static int WALL = 1;

    /**
     * Default value of the BOX object on the map.
     */
    final static int BOX = 2;

    /**
     * Default value of the MONSTER object on the map.
     */
    final static int MONSTER = 3;

    /**
     * Indicate the limit of how many bombs can the player placed during the game.
     */
    private final static int maxBomb = 2;

    /**
     * Store how many bomb the player has played during the game.
     */
    private int bombPlaced;

    /**
     * Default score that the player earned when kill a monster.
     */
    private final int monsterScore = 250;

    /**
     * Default score that the player earned when pass the level.
     */
    private final int wrapingScore = 500;

    /**
     * Default score earned when the player destroy a box.
     */
    private final int boxScore = 10;

    /**
     * This is the map that store all of the object in game.
     */
    int[][] fields;

    /**
     * Store all of the shooting spikes on the screen (to draw them out easier and to check for each of their collision easier).
     */
    SpikeSet spikes;

    /**
     * Initial number of the monsters during level 1.
     */
    private int INITIAL_MONSTER = 3;

    /**
     * Store the amount of the monsters' spawning during a new level.
     */
    private int monsterAmount;

    /**
     * Store all of the monsters in this set during a level.
     */
    private MonsterSet monsters;

    /**
     * Indicate the default cooldown for the monster's attack (the smaller, the faster the monster attack).
     */
    private double INITIAL_ATTACK_COOLDOWN = 6.0;

    /**
     * The cooldown of the monster's attack will be reduced by 0.5 everytime the player reach a new level.
     */
    private double COOLDOWN_DECREASE = 0.5;

    /**
     * Store the monter's attack cooldown.
     */
    public static double monsterAttackCoolDown;

    /**
     * Indicate the default speed of the shooting spike of the monters (the bigger, the faster the projectile flies).
     */
    private double INITIAL_SPYKE_SPEED = 3.0;

    /**
     * The monster's attack will fly 0.5 faster every time the player reach a new level.
     */
    private double SPEED_INCREASE = 0.5;

    /**
     * Store the flying speed of the monter's spike attack.
     */
    public static double spikeSpeed;

    /**
     * Indicate how many health booster will spawn during a level (I choose the maximum to 1, which means there will only be one health pack spawn during every level).
     */
    private int MAX_HEALTH_BOOSTER = 1;

    /**
     * Indicate how many bonus health the player will recover. The default I choose is 1.
     */
    static int HEALTH_BOOST_AMOUNT = 1;

    /**
     * Store the current level of the game. Default is 1.
     */
    public int mapLevel = 1;

    /**
     * Create the main player's object, to see its detail please check the Player script object.
     */
    Player mainPlayer;

    /**
     * Create the portal. There will only be 1 portal in each map.
     */
    Portal portal;

    /**
     * Default constructor of the World object.
     * Generating new maps, monster's set, its default attack speed and cooldown speed.
     */
    public World() {
        fields = new int[WORLD_WIDTH][WORLD_HEIGHT];

        spikes = new SpikeSet();
        mainPlayer = new Player();
        bombPlaced = 0;

        monsterAmount = INITIAL_MONSTER;
        monsterAttackCoolDown = INITIAL_ATTACK_COOLDOWN;
        spikeSpeed = INITIAL_SPYKE_SPEED;

        //Create a new game map.
        createMap();

    }

    /**
     * Get the current player's health for display on UI.
     * @return return current player's health
     */
    public int getPlayerHealth() {
        return mainPlayer.getHealth();
    }

    /**
     * Create a new level every time the player reach the portal.
     */
    public void newLevel() {
        //Create a new map.
        fields = new int[WORLD_WIDTH][WORLD_HEIGHT];

        //Create a new SpikeSet to keep track of all the Spike object on the screen.
        spikes = new SpikeSet();

        //Reset the bomb's limit of the player.
        bombPlaced = 0;

        //Increase the map level.
        ++mapLevel;

        //Alter the stats of the monsters
        //Increase 1 more monster spawn.
        monsterAmount += 1;
        //Make the monster's attack cooldown lower.
        monsterAttackCoolDown -= COOLDOWN_DECREASE;
        //Make the monster's attack fly faster.
        spikeSpeed += SPEED_INCREASE;

        //Generate a new map fields[][]
        createMap();
    }

    //Set the player's spawn.
    public void setPlayerSpawn() {
        //Set the player safe zone. This will make sure that no object will spawn in these area.
        fields[0][UPPER_LIMIT] = PLAYER_SPAWN;
        fields[1][UPPER_LIMIT] = PLAYER_SPAWN;
        fields[0][UPPER_LIMIT + 1] = PLAYER_SPAWN;
        //Spawn 2 extra box to protect the player at the beginning...
        fields[2][UPPER_LIMIT] = BOX;
        fields[0][UPPER_LIMIT + 2] = BOX;

        //Set the player's draw location on screen.
        mainPlayer.setDrawLocation(16, UPPER_LIMIT * 32 + 16);
    }

    //Generate new map settings.
    public void createMap() {
        //Set the player's safe zone spawn.
        setPlayerSpawn();
        //Choose randomly between these two map settings.
        switch (random.nextInt(2)) {
            case 0:
                mapSettings1();
                break;
            case 1:
                mapSettings2();
                break;
            default:
        }
        spawnMonsters();
    }

    /**
     * Map setting 1: Spawn wall with boxes (30 percent of spawning a box).
     */
    public void mapSettings1() {
        //Spawn wall.
        for (int x = 0; x < WORLD_WIDTH; ++x) {
            if (x % 2 != 0) for (int y = UPPER_LIMIT; y < WORLD_HEIGHT; ++y) {
                if (y % 2 != 0) fields[x][y] = WALL;
            }
        }
        //Spawn portal.
        spawnPortal();
        //Spawn health pack.
        spawnHealthBooster();
        //Spawn box (30% it will spawn in an empty place.)
        spawnBox(30);
    }

    /**
     * Map setting 2: Spawn only boxes (with 40% of spawning them).
     */
    public void mapSettings2() {
        //Spawn portal.
        spawnPortal();
        //Spawn health pack.
        spawnHealthBooster();
        //Spawn only boxes (40% of boxes).
        spawnBox(40);
    }

    /**
     * Spawn the boxes with the indicate percentage on the whole map.
     * @param percentage indicate the percentage (max 100) that the box object will spawn.
     */
    private void spawnBox(int percentage) {
        //Go through the map fields[][], if the current position is clear (to make sure the game does not spawn a box on the WALL, PLAYER_SPAWN area, PORTAL or HEALTH_BOOSTER object.
        for (int x = 0; x < WORLD_WIDTH; ++x) {
            for (int y = UPPER_LIMIT; y < WORLD_HEIGHT; ++y) {
                if ((fields[x][y] != WALL) && (fields[x][y] != PLAYER_SPAWN) && (fields[x][y] != PORTAL) && (fields[x][y] != HEALTH_BOOSTER)) {
                    //Randomly pick a number between 0 and 99.
                    int temp = random.nextInt(100);
                    //If that number is smaller than the percentage, spawn the box. Else do nothing.
                    if (temp <= percentage - 1) fields[x][y] = BOX;
                }
            }
        }
    }

    /**
     * Check to make sure if the map still have an empty spot (in order to spawn a new monsters or new health pack).
     * @return true if there is an empty spot (PATH) to spawn item, false if there is no more (the map is full of object).
     */
    private boolean emptyPathAvailable() {
        for (int x = 0; x < WORLD_WIDTH; ++x)
            for (int y = UPPER_LIMIT; y < WORLD_HEIGHT; ++y) {
                if (fields[x][y] == PATH) return true;
            }
        return false;
    }

    /**
     * Spawn the indicate MAX_HEALTH_BOOSTER limit in the map.
     */
    private void spawnHealthBooster() {
        for (int i = 0; i < MAX_HEALTH_BOOSTER; ++i)
            //If there is still spot to spawn the health booster, choose randomly a position.
            if (emptyPathAvailable()) {
                int x = 0, y = UPPER_LIMIT;
                while (fields[x][y] != PATH) {
                    x = random.nextInt(WORLD_WIDTH);
                    y = random.nextInt(WORLD_HEIGHT - UPPER_LIMIT) + UPPER_LIMIT;
                }

                fields[x][y] = HEALTH_BOOSTER;
            }
            else break;
    }

    /**
     * Spawn the monster (limit by monsterAmount) on the map and store them in the MonsterSet.
     */
    public void spawnMonsters() {
        //Create a new MonsterSet to keep track of the monster.
        monsters = new MonsterSet(monsterAmount);
        for (int i = 0; i < monsterAmount; ++i)
            //If there is still an empty spot on the fields[][] map to spawn a monster, proceed to choose a position randomly.
            if (emptyPathAvailable()) {
                int x = 0, y = UPPER_LIMIT;
                while (fields[x][y] != PATH) {
                    x = random.nextInt(WORLD_WIDTH);
                    y = random.nextInt(WORLD_HEIGHT - UPPER_LIMIT) + UPPER_LIMIT;
                }

                fields[x][y] = MONSTER;
                monsters.addMonsters(x, y);
            }
            else break;
    }

    /**
     * Spawn a portal so that the player can continue to the next level.
     */
    public void spawnPortal() {
        int x = 0, y = UPPER_LIMIT;
        while (fields[x][y] != PATH) {
            x = random.nextInt(WORLD_WIDTH);
            y = random.nextInt(WORLD_HEIGHT - UPPER_LIMIT) + UPPER_LIMIT;
        }

        fields[x][y] = PORTAL;
        //Create a new Portal object to keep track of the portal position.
        portal = new Portal(x, y);
    }

    /**
     * One of the most complicated game mechanic: the bomb explosion time.
     * After the player click on the placeBomb button, this will place a bomb on a empty PATH on the map. The bomb will be place accordingly by the player's current map location (more details on this in the Player object script).
     * Upon placing the bomb on the map. I use a CountDownTimer to count until after 3 seconds that the bomb start to explode and cause damage. For detail where I learn about CountDownTimer was mentioned during my MidTermProject.
     * However, during the creation of this game, I step across a problem: this CountDownTimer code can only run on the game's activity (or say the game UI), which mean that it cannot be executed in this part of the game's code
     *      this is consider the background game UI, so it is expected to execute immidiately without delay), since there is no thread or game's timer to run the CountDownTimer on. To tackle this problem, I follow the instruction
     *      in this article: "https://stackoverflow.com/questions/32283811/cant-create-handler-inside-thread-that-has-not-called-looper-prepare-in-count"
     *      This will create a new Looper inside a Handler, thus allowing a new timer activity inside the section of this code. And thus I can use the CountDownTimer in it. For more detail, please read the code to understand how it works.
     * After exploding, there will be another CountDownTimer to allow the drawing of the explosion of the bomb on the map last for 1 seconds, before disappearing.
     */
    public void placeBomb() {
        //The player can only place the bomb on a empty PATH or in the PLAYER_SPAWN area. If the player reach the maximum number of bomb allow to be placed at the same time on the map, then do nothing.
        if ((bombPlaced < maxBomb) && ((fields[mainPlayer.mapLocationX()][mainPlayer.mapLocationY()] == PATH) || (fields[mainPlayer.mapLocationX()][mainPlayer.mapLocationY()] == PLAYER_SPAWN))) {
            //Create a new bomb object at the current approximate location of the player on the map.
            Bomb thisBomb = new Bomb(mainPlayer.mapLocationX(), mainPlayer.mapLocationY());
            //Update how many bomb had been placed
            ++bombPlaced;
            //Update the current position on the map to be a bomb.
            fields[thisBomb.valueX()][thisBomb.valueY()] = BOMB;

            //Create a new Handler and Looper to run a CountDownTimer (making the bomb exploding after 3 seconds).
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                public void run() {
                    /**
                     * CountDownTimer for the bomb exploding.
                     */
                    new CountDownTimer(3000, 1000) {
                        /**
                         * Indicate the action between each counting interval (in this case, it will count every 1 second in 3 second. Since I do not need to do anything in between, this code is empty (I could have make it to update the bomb's graphic, but I
                         * do not have enough time to draw it).
                         * @param millisUntilFinished count down before the milliesInFuture.
                         */
                        @Override
                        public void onTick(long millisUntilFinished) {
                            //do nothing.
                        }

                        /**
                         * Execute these line of code upon reaching the end of the counting interval (in this case 3000 miliseconds).
                         * These code will make to draw out the explosion (fire) on the map, cause damage to the BOX / MONSTER / PLAYER object on the screen.
                         */
                        @Override
                        public void onFinish() {
                            //Remove the exploded bomb.
                            fields[thisBomb.valueX()][thisBomb.valueY()] = 0;
                            --bombPlaced;

                            if (Settings.soundEnabled)
                                Assets.bombExplode.play(1);

                            //Draw the explosion.
                            //Draw at the bomb.
                            if (fields[thisBomb.valueX()][thisBomb.valueY()] == PATH)
                                fields[thisBomb.valueX()][thisBomb.valueY()] = FIRE;
                            //Draw upper.
                            try {
                                if (fields[thisBomb.valueX()][thisBomb.valueY() - 1] == PATH || fields[thisBomb.valueX()][thisBomb.valueY() - 1] == PLAYER_SPAWN) {
                                    fields[thisBomb.valueX()][thisBomb.valueY() - 1] = FIRE;
                                    if (fields[thisBomb.valueX()][thisBomb.valueY() - 2] == PATH || fields[thisBomb.valueX()][thisBomb.valueY() - 2] == PLAYER_SPAWN)
                                        fields[thisBomb.valueX()][thisBomb.valueY() - 2] = FIRE;
                                }
                            }
                            catch (ArrayIndexOutOfBoundsException exception) {}
                            //Draw leftward.
                            try {
                                if (fields[thisBomb.valueX() - 1][thisBomb.valueY()] == PATH || fields[thisBomb.valueX() - 1][thisBomb.valueY()] == PLAYER_SPAWN) {
                                    fields[thisBomb.valueX() - 1][thisBomb.valueY()] = FIRE;
                                    if (fields[thisBomb.valueX() - 2][thisBomb.valueY()] == PATH || fields[thisBomb.valueX() - 2][thisBomb.valueY()] == PLAYER_SPAWN)
                                        fields[thisBomb.valueX() - 2][thisBomb.valueY()] = FIRE;
                                }
                            }
                            catch (ArrayIndexOutOfBoundsException exception) {}
                            //Draw rightward.
                            try {
                                if (fields[thisBomb.valueX() + 1][thisBomb.valueY()] == PATH || fields[thisBomb.valueX() + 1][thisBomb.valueY()] == PLAYER_SPAWN) {
                                    fields[thisBomb.valueX() + 1][thisBomb.valueY()] = FIRE;
                                    if (fields[thisBomb.valueX() + 2][thisBomb.valueY()] == PATH || fields[thisBomb.valueX() + 2][thisBomb.valueY()] == PLAYER_SPAWN)
                                        fields[thisBomb.valueX() + 2][thisBomb.valueY()] = FIRE;
                                }
                            }
                            catch (ArrayIndexOutOfBoundsException exception) {}
                            //Draw downward.
                            try {
                                if (fields[thisBomb.valueX()][thisBomb.valueY() + 1] == PATH || fields[thisBomb.valueX()][thisBomb.valueY() + 1] == PLAYER_SPAWN) {
                                    fields[thisBomb.valueX()][thisBomb.valueY() + 1] = FIRE;
                                    if (fields[thisBomb.valueX()][thisBomb.valueY() + 2] == PATH || fields[thisBomb.valueX()][thisBomb.valueY() + 2] == PLAYER_SPAWN)
                                        fields[thisBomb.valueX()][thisBomb.valueY() + 2] = FIRE;
                                }
                            }
                            catch (ArrayIndexOutOfBoundsException exception) {}

                            //Delete the explosion flame after 1 secs.
                            new CountDownTimer(1000, 1000) {
                                @Override
                                public void onTick(long millisUntilFinished) {
                                    //do nothing.
                                }

                                @Override
                                public void onFinish() {
                                    //Draw at the bomb.
                                    if (fields[thisBomb.valueX()][thisBomb.valueY()] == FIRE)
                                        fields[thisBomb.valueX()][thisBomb.valueY()] = PATH;
                                    //Draw upper.
                                    try {
                                        if (fields[thisBomb.valueX()][thisBomb.valueY() - 1] == FIRE) {
                                            fields[thisBomb.valueX()][thisBomb.valueY() - 1] = PATH;
                                            if (fields[thisBomb.valueX()][thisBomb.valueY() - 2] == FIRE)
                                                fields[thisBomb.valueX()][thisBomb.valueY() - 2] = PATH;
                                        }
                                    }
                                    catch (ArrayIndexOutOfBoundsException exception) {}
                                    //Draw leftward.
                                    try {
                                        if (fields[thisBomb.valueX() - 1][thisBomb.valueY()] == FIRE) {
                                            fields[thisBomb.valueX() - 1][thisBomb.valueY()] = PATH;
                                            if (fields[thisBomb.valueX() - 2][thisBomb.valueY()] == FIRE)
                                                fields[thisBomb.valueX() - 2][thisBomb.valueY()] = PATH;
                                        }
                                    }
                                    catch (ArrayIndexOutOfBoundsException exception) {}
                                    //Draw rightward.
                                    try {
                                        if (fields[thisBomb.valueX() + 1][thisBomb.valueY()] == FIRE) {
                                            fields[thisBomb.valueX() + 1][thisBomb.valueY()] = PATH;
                                            if (fields[thisBomb.valueX() + 2][thisBomb.valueY()] == FIRE)
                                                fields[thisBomb.valueX() + 2][thisBomb.valueY()] = PATH;
                                        }
                                    }
                                    catch (ArrayIndexOutOfBoundsException exception) {}
                                    //Draw downward.
                                    try {
                                        if (fields[thisBomb.valueX()][thisBomb.valueY() + 1] == FIRE) {
                                            fields[thisBomb.valueX()][thisBomb.valueY() + 1] = PATH;
                                            if (fields[thisBomb.valueX()][thisBomb.valueY() + 2] == FIRE)
                                                fields[thisBomb.valueX()][thisBomb.valueY() + 2] = PATH;
                                        }
                                    }
                                    catch (ArrayIndexOutOfBoundsException exception) {}
                                }
                            }.start();

                            //Damage the player.
                            if ((Math.abs(mainPlayer.mapLocationX() - thisBomb.valueX()) <= thisBomb.bombRadius()) && (Math.abs(mainPlayer.mapLocationY() - thisBomb.valueY()) == 0)
                                || (Math.abs(mainPlayer.mapLocationY() - thisBomb.valueY()) <= thisBomb.bombRadius()) && (Math.abs(mainPlayer.mapLocationX() - thisBomb.valueX()) == 0)) {
                                mainPlayer.receiveDamage(thisBomb.damageAmount());
                                if (mainPlayer.getHealth() <= 0) gameOver = true;
                            }

                            //Damage and or destroy the box / monster around.

                            //Check to destroy the upper path.
                            try {
                                //Destroy box.
                                if (fields[thisBomb.valueX()][thisBomb.valueY() - 1] == BOX) {
                                    fields[thisBomb.valueX()][thisBomb.valueY() - 1] = PATH;
                                    score += boxScore;
                                }
                                //Damage monsters.
                                else if (fields[thisBomb.valueX()][thisBomb.valueY() - 1] == MONSTER) {
                                    if (monsters.damageMonster(thisBomb.valueX(), thisBomb.valueY() - 1, thisBomb.damageAmount(), fields))
                                        score += monsterScore;
                                }
                                //Reach wall then do nothing.
                                else if (fields[thisBomb.valueX()][thisBomb.valueY() - 1] == WALL) {
                                    //do nothing.
                                }
                                //Destroy box.
                                else if (fields[thisBomb.valueX()][thisBomb.valueY() - 2] == BOX) {
                                    fields[thisBomb.valueX()][thisBomb.valueY() - 2] = PATH;
                                    score += boxScore;
                                }
                                //Damage monsters.
                                else if (fields[thisBomb.valueX()][thisBomb.valueY() - 2] == MONSTER) {
                                    if (monsters.damageMonster(thisBomb.valueX(), thisBomb.valueY() - 2, thisBomb.damageAmount(), fields))
                                        score += monsterScore;
                                }
                            }
                            catch (ArrayIndexOutOfBoundsException exception) {
                                //do nothing.
                            }

                            //Check to destroy the left path.
                            try {
                                //Destroy box.
                                if (fields[thisBomb.valueX() - 1][thisBomb.valueY()] == BOX) {
                                    fields[thisBomb.valueX() - 1][thisBomb.valueY()] = PATH;
                                    score += boxScore;
                                }
                                //Damage monsters.
                                else if (fields[thisBomb.valueX() - 1][thisBomb.valueY()] == MONSTER) {
                                    if (monsters.damageMonster(thisBomb.valueX() - 1, thisBomb.valueY(), thisBomb.damageAmount(), fields))
                                        score += monsterScore;
                                }
                                //Reach wall.
                                else if (fields[thisBomb.valueX() - 1][thisBomb.valueY()] == WALL) {
                                    //do nothing.
                                }
                                //Destroy box.
                                else if (fields[thisBomb.valueX() - 2][thisBomb.valueY()] == BOX) {
                                    fields[thisBomb.valueX() - 2][thisBomb.valueY()] = PATH;
                                    score += boxScore;
                                }
                                //Damage monsters.
                                else if (fields[thisBomb.valueX() - 2][thisBomb.valueY()] == MONSTER) {
                                    if (monsters.damageMonster(thisBomb.valueX() - 2, thisBomb.valueY(), thisBomb.damageAmount(), fields))
                                        score += monsterScore;
                                }
                            }
                            catch (ArrayIndexOutOfBoundsException exception) {
                                //do nothing.
                            }

                            //Check to destroy the right path.
                            try {
                                //Destroy box.
                                if (fields[thisBomb.valueX() + 1][thisBomb.valueY()] == BOX) {
                                    fields[thisBomb.valueX() + 1][thisBomb.valueY()] = PATH;
                                    score += boxScore;
                                }
                                //Damage monsters.
                                else if (fields[thisBomb.valueX() + 1][thisBomb.valueY()] == MONSTER) {
                                    if (monsters.damageMonster(thisBomb.valueX() + 1, thisBomb.valueY(), thisBomb.damageAmount(), fields))
                                        score += monsterScore;
                                }
                                //Reach wall.
                                else if (fields[thisBomb.valueX() + 1][thisBomb.valueY()] == WALL) {
                                    //do nothing.
                                }
                                //Damage box.
                                else if (fields[thisBomb.valueX() + 2][thisBomb.valueY()] == BOX) {
                                    fields[thisBomb.valueX() + 2][thisBomb.valueY()] = PATH;
                                    score += boxScore;
                                }
                                //Damage monsters.
                                else if (fields[thisBomb.valueX() + 2][thisBomb.valueY()] == MONSTER) {
                                    if (monsters.damageMonster(thisBomb.valueX() + 2, thisBomb.valueY(), thisBomb.damageAmount(), fields))
                                        score += monsterScore;
                                }
                            }
                            catch (ArrayIndexOutOfBoundsException exception) {
                                //do nothing.
                            }

                            //Check to destroy the lower path.
                            try {
                                //Destroy box.
                                if (fields[thisBomb.valueX()][thisBomb.valueY() + 1] == BOX) {
                                    fields[thisBomb.valueX()][thisBomb.valueY() + 1] = PATH;
                                    score += boxScore;
                                }
                                //Damage monsters.
                                else if (fields[thisBomb.valueX()][thisBomb.valueY() + 1] == MONSTER) {
                                    if (monsters.damageMonster(thisBomb.valueX(), thisBomb.valueY() + 1, thisBomb.damageAmount(), fields))
                                        score += monsterScore;
                                }
                                //Reach wall.
                                else if (fields[thisBomb.valueX()][thisBomb.valueY() + 1] == WALL) {
                                    //do nothing.
                                }
                                else if (fields[thisBomb.valueX()][thisBomb.valueY() + 2] == BOX) {
                                    fields[thisBomb.valueX()][thisBomb.valueY() + 2] = PATH;
                                    score += boxScore;
                                }
                                //Damage monsters.
                                else if (fields[thisBomb.valueX()][thisBomb.valueY() + 2] == MONSTER) {
                                    if (monsters.damageMonster(thisBomb.valueX(), thisBomb.valueY() + 2, thisBomb.damageAmount(), fields))
                                        score += monsterScore;
                                }
                            }
                            catch (ArrayIndexOutOfBoundsException exception) {
                                //do nothing.
                            }

                        }
                    }.start();
                }
            });
        }
    }

    /**
     * Update the game status accordingly to real time: Make the monster attack, make the spike projectile flies, update the player's movement.
     * @param deltaTime
     */
    public void update(float deltaTime) {
        //If the game is over, then do not need to update this anymore.
        if (gameOver) {
            return;
        }

        tickTime += deltaTime;

        //Update the game as the time goes on.
        while (tickTime > tick) {
            tickTime -= tick;
            //Update player movement.
            mainPlayer.advance(fields);
            if (mainPlayer.mapLocationX() == portal.getX() && mainPlayer.mapLocationY() == portal.getY()) {
                score += wrapingScore;
                newLevel();
            }

            //Make monster attack.
            monsters.advance(spikes);
            if (mainPlayer.getHealth() <= 0) gameOver = true;

            //Make the spike move.
            spikes.advance(fields, mainPlayer);

        }
    }
}
