package edu.angelo.finalprojecttran;

import java.util.List;

import android.graphics.Color;

import com.badlogic.androidgames.framework.Game;
import com.badlogic.androidgames.framework.Graphics;
import com.badlogic.androidgames.framework.Input.TouchEvent;
import com.badlogic.androidgames.framework.Pixmap;
import com.badlogic.androidgames.framework.Screen;

public class GameScreen extends Screen {

    /**
     * Indicate the game state: Ready to play / Running / Paused / and Game Over.
     */
    enum GameState {
        Ready,
        Running,
        Paused,
        GameOver
    }

    /**
     * Default setting when loading into the game screen is Ready.
     */
    GameState state = GameState.Ready;

    /**
     * Create a World to manage the game's interaction.
     */
    World world;

    /**
     * Store the game score.
     */
    int oldScore = 0;
    String score = "0";

    /**
     * Store the player's health.
     */
    int oldHealth = Player.DEFAULT_HEALTH;
    String health = "" + Player.DEFAULT_HEALTH;

    /**
     * Store the current game's level, default starting is 1.
     */
    int currentLevel = 1;
    String level = "1";

    /**
     * Create a new game activity Game Screen.
     * @param game
     */
    public GameScreen(Game game) {
        super(game);
        //Create a new World object.
        world = new World();
    }

    /**
     * Update the game accordingly to the game's state.
     * @param deltaTime update in real game time.
     */
    @Override
    public void update(float deltaTime) {
        List<TouchEvent> touchEvents = game.getInput().getTouchEvents();
        game.getInput().getKeyEvents();

        if (state == GameState.Ready) {
            updateReady(touchEvents);
        }
        if (state == GameState.Running) {
            updateRunning(touchEvents, deltaTime);
        }
        if (state == GameState.Paused) {
            updatePaused(touchEvents);
        }
        if (state == GameState.GameOver) {
            updateGameOver(touchEvents);
        }
    }

    /**
     * Holding the Ready game status. Transition into Running when the user click on the screen.
     * @param touchEvents get the user's touch input.
     */
    private void updateReady(List<TouchEvent> touchEvents) {
        if (touchEvents.size() > 0) {
            state = GameState.Running;
        }
    }

    /**
     * Get the user's input and acts accordingly to the user's input on the game's button.
     * @param touchEvents get the user's input touch.
     * @param deltaTime update in real time.
     */
    private void updateRunning(List<TouchEvent> touchEvents, float deltaTime) {
        int len = touchEvents.size();
        for (int i = 0; i < len; i += 1) {
            TouchEvent event = touchEvents.get(i);
            //If the user touch up.
            if (event.type == TouchEvent.TOUCH_UP) {
                //If the user choose the pause button game, change the game from Running into Paused state.
                if (event.x < 64 && event.y < 64) {
                    //If the game is paused and the sound is enable, play the paused sound.
                    if (Settings.soundEnabled) {
                        Assets.paused.play(1);
                    }
                    state = GameState.Paused;
                    return;
                }

                //If the user stop clicking on any button (including the movement button), then stop the player's movement.
                world.mainPlayer.standStill();


            }

            //If the user start clicking on the button (in this case moving the characters), update the player's position on map.
            if (event.type == TouchEvent.TOUCH_DOWN) {
                //Click up button.
                if ((event.x >= 32 && event.x <= 64) && (event.y >= 385 && event.y <= 417) ) {
                    world.mainPlayer.moveUp();
                }

                //Click left button.
                if ((event.x >= 0 && event.x <= 32) && (event.y >= 417 && event.y <= 449) ) {
                    world.mainPlayer.moveLeft();
                }

                //Click right button.
                if ((event.x >= 64 && event.x <= 96) && (event.y >= 417 && event.y <= 449) ) {
                    world.mainPlayer.moveRight();
                }

                //Click down button.
                if ((event.x >= 32 && event.x <= 64) && (event.y >= 449 && event.y <= 480) ) {
                    world.mainPlayer.moveDown();
                }

                //Click place bomb button.
                if ((event.x >= 232 && event.x <= 296) && (event.y >= 401 && event.y <= 465)) {
                    world.placeBomb();
                }
            }

            //If the user dragged click onto other movement button, then update the player's character position accordingly to the hold-down button.
            if (event.type == TouchEvent.TOUCH_DRAGGED) {
                //Hold up button.
                if ((event.x >= 32 && event.x <= 64) && (event.y >= 385 && event.y <= 417) ) {
                    world.mainPlayer.moveUp();
                }

                //Hold left button.
                if ((event.x >= 0 && event.x <= 32) && (event.y >= 417 && event.y <= 449) ) {
                    world.mainPlayer.moveLeft();
                }

                //Hold right button.
                if ((event.x >= 64 && event.x <= 96) && (event.y >= 417 && event.y <= 449) ) {
                    world.mainPlayer.moveRight();
                }

                //Hold down button.
                if ((event.x >= 32 && event.x <= 64) && (event.y >= 449 && event.y <= 480) ) {
                    world.mainPlayer.moveDown();
                }
            }
        }

        //Update the world game object on real delta time.
        world.update(deltaTime);

        //If the world game object indicates the game is over (player lost all health), play the game over sound and transition into Game Over game state.
        if (world.gameOver) {
            //Play the game over sound if the sound is enable.
            if (Settings.soundEnabled) {
                Assets.gameOverSound.play(1);
            }
            state = GameState.GameOver;
        }

        //If the player gain score, update the score string accordingly (to display) and play the gain score sound effect.
        if (oldScore < world.score) {
            oldScore = world.score;
            score = "" + oldScore;
            //Play the earn point sound if the sound is enable.
            if (Settings.soundEnabled) {
                Assets.earnPoint.play(1);
            }
        }

        //If the player's health change (gain or loss), update the health string to display on the UI.
        if (oldHealth != world.getPlayerHealth()) {
            oldHealth = world.getPlayerHealth();
            health = "" + oldHealth;
        }

        //If the player transition into a new level, update the current level display UI.
        if (currentLevel != world.mapLevel) {
            currentLevel = world.mapLevel;
            level = "" + currentLevel;
        }
    }

    /**
     * Draw and update the game accordingly to the user's input when the game is Paused.
     * @param touchEvents
     */
    private void updatePaused(List<TouchEvent> touchEvents) {
        //Get the user's input.
        int len = touchEvents.size();
        for (int i = 0; i < len; i += 1) {
            TouchEvent event = touchEvents.get(i);
            //If the user Touch-Up on a button.
            if (event.type == TouchEvent.TOUCH_UP) {
                //if the user choose the resume button, transition the game state from Paused into Running.
                if (event.x > 80 && event.x <= 240) {
                    if (event.y > 100 && event.y <= 148) {
                        //Play the click sound if the sound is enable.
                        if (Settings.soundEnabled) {
                            Assets.click.play(1);
                        }
                        state = GameState.Running;
                        return;
                    }
                    //if the user choose to quit, transition the game into the main menu screen, save the game score.
                    if (event.y > 148 && event.y < 196) {
                        //Play the click sound if the sound is enable.
                        if (Settings.soundEnabled) {
                            Assets.click.play(1);
                        }
                        //If the player click the quit button. Update the score to the high score board.
                        Settings.addScore(world.score);
                        Settings.save(game.getFileIO());

                        game.setScreen(new MainMenuScreen(game));
                        return;
                    }
                }
            }
        }
    }

    /**
     * Update the game accordingly when the game is over.
     * @param touchEvents
     */
    private void updateGameOver(List<TouchEvent> touchEvents) {
        //Get the user's input.
        int len = touchEvents.size();
        for (int i = 0; i < len; i += 1) {
            TouchEvent event = touchEvents.get(i);
            //If the user click on the replay button, the game will transition into the main menu.
            if (event.type == TouchEvent.TOUCH_UP) {
                if (event.x >= 128 && event.x <= 192 &&
                        event.y >= 168 && event.y <= 232) {
                    if (Settings.soundEnabled) {
                        //Play the click sound if the sound is enable.
                        Assets.click.play(1);
                    }
                    game.setScreen(new MainMenuScreen(game));
                    return;
                }
            }
        }
    }

    /**
     * Draw the game's assets in the Game Screen.
     * @param deltaTime draw in real time.
     */
    @Override
    public void present(float deltaTime) {
        //Get the game's graphic.
        Graphics g = game.getGraphics();

        //Draw the game's background.
        g.drawPixmap(Assets.background, 0, 0);

        //Draw the game's world.
        drawWorld(world);
        if (state == GameState.Ready) {
            drawReadyUI();
        }
        if (state == GameState.Running) {
            drawRunningUI();
        }
        if (state == GameState.Paused) {
            drawPausedUI();
        }
        if (state == GameState.GameOver) {
            drawGameOverUI();
        }

        //Draw the other game screen UI.
        //Draw the player's health.
        Pixmap healthPixmap = Assets.health;
        g.drawPixmap(healthPixmap, (g.getWidth() / 2) - 16, 0);
        drawText(g, health, (g.getWidth() / 2) + 16, 0);

        //Draw score.
        drawText(g, score, g.getWidth() - score.length() * 20, 0);

        //Draw current level.
        Pixmap levelPixmap = Assets.level;
        g.drawPixmap(levelPixmap, (g.getWidth() / 2) - 16, 432);
        drawText(g, level, (g.getWidth() / 2) + 16, 432);
    }

    /**
     * Draw the ingame assets.
     * @param world
     */
    private void drawWorld(World world) {
        //Get the game's graphic to draw on.
        Graphics g = game.getGraphics();

        //Draw the square line in game map.
        for (int x = 0; x < World.WORLD_WIDTH; ++x)
            g.drawLine(x * 32, 2 * 32, x * 32, World.WORLD_HEIGHT * 32, Color.BLACK);
        for (int y = World.UPPER_LIMIT; y <= World.WORLD_HEIGHT; ++y)
            g.drawLine(0, y * 32, World.WORLD_WIDTH * 32, y * 32, Color.BLACK);

        //Draw all item on the fields[][] map (Box, Wall, Monster, Bomb, Fire)
        Pixmap wallPixMap = Assets.wall;
        Pixmap boxPixMap = Assets.box;
        Pixmap bombPixmap = Assets.bomb;
        Pixmap firePixmap = Assets.fire;
        Pixmap monsterPixmap = Assets.monster;
        Pixmap portalPixmap = Assets.portal;
        Pixmap healthPackPixmap = Assets.healthBooster;
        for (int x = 0; x < World.WORLD_WIDTH; ++x) {
            for (int y = World.UPPER_LIMIT; y < World.WORLD_HEIGHT; ++y) {
                if (world.fields[x][y] == World.WALL)
                    g.drawPixmap(wallPixMap, x * 32, y * 32);
                else if (world.fields[x][y] == World.BOX)
                    g.drawPixmap(boxPixMap, x * 32, y * 32);
                else if (world.fields[x][y] == World.BOMB)
                    g.drawPixmap(bombPixmap, x * 32, y * 32);
                else if (world.fields[x][y] == World.FIRE)
                    g.drawPixmap(firePixmap, x * 32, y * 32);
                else if (world.fields[x][y] == World.MONSTER)
                    g.drawPixmap(monsterPixmap, x * 32, y * 32);
                else if (world.fields[x][y] == World.PORTAL)
                    g.drawPixmap(portalPixmap, x * 32, y * 32);
                else if (world.fields[x][y] == World.HEALTH_BOOSTER)
                    g.drawPixmap(healthPackPixmap, x * 32, y * 32);
            }
        }

        //Draw the monster's spike.
        Pixmap spikeUpPixMap = Assets.monsterSpikeUpward;
        Pixmap spikeLeftPixMap = Assets.monsterSpikeLeftward;
        Pixmap spikeRightPixMap = Assets.monsterSpikeRightward;
        Pixmap spikeDownPixMap = Assets.monsterSpikeDownward;

        //Draw Spikes accordingly to its direction: Up / Down / Left / Right.
        for (int i = 0; i < world.spikes.spikeSize(); ++i) {
            switch(world.spikes.spikeAt(i).direction) {
                case World.UP:
                    g.drawPixmap(spikeUpPixMap, world.spikes.spikeAt(i).x - 16, world.spikes.spikeAt(i).y - 16);
                    break;
                case World.LEFT:
                    g.drawPixmap(spikeLeftPixMap, world.spikes.spikeAt(i).x - 16, world.spikes.spikeAt(i).y - 16);
                    break;
                case World.RIGHT:
                    g.drawPixmap(spikeRightPixMap, world.spikes.spikeAt(i).x - 16, world.spikes.spikeAt(i).y - 16);
                    break;
                case World.DOWN:
                    g.drawPixmap(spikeDownPixMap, world.spikes.spikeAt(i).x - 16, world.spikes.spikeAt(i).y - 16);
                    break;
                default:
            }
        }

        //Draw player.
        Pixmap playerPixMap = Assets.player;
        g.drawPixmap(playerPixMap, world.mainPlayer.drawX() - 16, world.mainPlayer.drawY() - 16);

    }

    /**
     * Draw the game ready UI.
     */
    private void drawReadyUI() {
        Graphics g = game.getGraphics();

        g.drawPixmap(Assets.ready, 47, 100);
    }

    //Draw the game UI during Running.
    private void drawRunningUI() {
        Graphics g = game.getGraphics();

        //Draw pause button game.
        g.drawPixmap(Assets.buttons, 0, 0, 64, 128, 64, 64);

        //Draw the player's movement button (Up, Down, Left, Right).
        Pixmap upButtonPixmap = Assets.upButton;
        Pixmap leftButtonPixmap = Assets.leftButton;
        Pixmap rightButtonPixmap = Assets.rightButton;
        Pixmap downButtonPixmap = Assets.downButton;

        //Draw up button.
        g.drawPixmap(upButtonPixmap, 40, 385);

        //Draw left button.
        g.drawPixmap(leftButtonPixmap, 8, 417);

        //Draw right button.
        g.drawPixmap(rightButtonPixmap, 72, 417);

        //Draw down button.
        g.drawPixmap(downButtonPixmap, 40, 449);

        //Draw place bomb button.
        g.drawPixmap(Assets.bombButton, 232, 401);
        //g.drawRect(232, 401, 64, 64, Color.GREEN);
    }

    /**
     * Draw the pause game UI.
     */
    private void drawPausedUI() {
        Graphics g = game.getGraphics();
        g.drawPixmap(Assets.pause, 80, 100);
    }

    /**
     * Draw the game over UI.
     */
    private void drawGameOverUI() {
        Graphics g = game.getGraphics();

        g.drawPixmap(Assets.gameOver, 62, 100);
        //g.drawPixmap(Assets.buttons, 128, 200, 0, 128, 64, 64);
        g.drawPixmap(Assets.replayButton, 128, 168);
    }

    /**
     * Draw the text to the screen accordingly from the String input.
     * @param g get the game's graphic
     * @param line get the string to draw out on.
     * @param x get position x
     * @param y get position y.
     */
    public void drawText(Graphics g, String line, int x, int y) {
        int len = line.length();
        for (int i = 0; i < len; i += 1) {
            char character = line.charAt(i);

            if (character == ' ') {
                x += 20;
                continue;
            }

            int srcX = 0;
            int srcWidth = 0;
            if (character == '.') {
                srcX = 200;
                srcWidth = 10;
            } else {
                srcX = (character - '0') * 20;
                srcWidth = 20;
            }

            g.drawPixmap(Assets.numbers, x, y, srcX, 0, srcWidth, 32);
            x += srcWidth;
        }
    }

    /**
     * Make the the game transition into the pause state if the game is running.
     * Or if the game is over the save the game's high score into the save file.
     */
    @Override
    public void pause() {
        if (state == GameState.Running) {
            state = GameState.Paused;
        }
        if (world.gameOver) {
            Settings.addScore(world.score);
            Settings.save(game.getFileIO());
        }
    }

    @Override
    public void resume() {
    }

    @Override
    public void dispose() {
    }
}
