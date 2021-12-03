package edu.angelo.finalprojecttran;

import android.content.Intent;
import android.net.Uri;

import com.badlogic.androidgames.framework.Game;
import com.badlogic.androidgames.framework.Graphics;
import com.badlogic.androidgames.framework.Input;
import com.badlogic.androidgames.framework.Screen;
import com.badlogic.androidgames.framework.impl.AndroidGame;

import java.util.List;

public class MainMenuScreen extends Screen {

    /**
     * Create the main menu screen in game.
     * @param game the whole game activity.
     */
    public MainMenuScreen(Game game) {
        super(game);
    }

    /**
     * Draw the main menu UI in game, indicates 3 main actions: Play the game, display the High score Screen, or display the Help Screen.
     * @param deltaTime
     */
    public void update(float deltaTime) {
        //Get the game's graphic to draw on.
        Graphics g = game.getGraphics();

        //Get the game's user input.
        List<Input.TouchEvent> touchEvents = game.getInput().getTouchEvents();
        game.getInput().getKeyEvents();

        int len = touchEvents.size();
        for (int i = 0; i < len; i += 1) {
            Input.TouchEvent event = touchEvents.get(i);
            //If user stop click on the screen.
            if (event.type == Input.TouchEvent.TOUCH_UP) {
                //If the user click on the sound icon on the bottom left.
                //Mute the sound in game.
                if (inBounds(event, 0, g.getHeight() - 64, 64, 64)) {
                    Settings.soundEnabled = !Settings.soundEnabled;
                    //Play the click sound if the sound is enable in game.
                    if (Settings.soundEnabled) {
                        Assets.click.play(1);
                    }
                }
                //If the user click on the Play button.
                //Transition to the game screen.
                if (inBounds(event, 64, 220, 192, 42)) {
                    game.setScreen(new GameScreen(game));
                    //Play the click sound if the sound is enable in game.
                    if (Settings.soundEnabled) {
                        Assets.click.play(1);
                    }
                    return;
                }
                //If the user click on the High Score button.
                //Transition to the High Score screen.
                if (inBounds(event, 64, 220 + 42, 192, 42)) {
                    game.setScreen(new HighscoreScreen(game));
                    //Play the click sound if the sound is enable in game.
                    if (Settings.soundEnabled) {
                        Assets.click.play(1);
                    }
                    return;
                }
                //If the user click the help section on the Main Menu screen.
                //Transition to the first help screen (Help Screen 1).
                if (inBounds(event, 64, 220 + 84, 192, 42)) {
                    game.setScreen(new HelpScreen(game));
                    //Play the click sound if the sound is enable in game.
                    if (Settings.soundEnabled) {
                        Assets.click.play(1);
                    }
                    return;
                }
            }
        }
    }

    /**
     * Check if the user click on the button's boundary correctly.
     * @param event get the user's input event.
     * @param x position x of the button.
     * @param y position y of the button.
     * @param width width of the button
     * @param height height of the button
     * @return Return true if user click accordingly on the button, return false if the user not click on the button.
     */
    private boolean inBounds(Input.TouchEvent event, int x, int y, int width, int height) {
        if (event.x > x && event.x < x + width - 1 &&
                event.y > y && event.y < y + height - 1) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Draw the main menu UI from the assets.
     * @param deltaTime draw in real time.
     */
    public void present(float deltaTime) {
        //Get the game's graphic to draw on it.
        Graphics g = game.getGraphics();

        //Draw the game's background.
        g.drawPixmap(Assets.background, 0, 0);

        // Draw the game's logo.
        g.drawPixmap(Assets.logo, 32, 20);

        //Draw the game's main menu button.
        g.drawPixmap(Assets.mainMenu, 64, 220);
        if (Settings.soundEnabled) {
            //Draw the sound button if the sound is enable.
            g.drawPixmap(Assets.buttons, 0, 416, 0, 0, 64, 64);
        } else {
            //Draw the muted sound button if the sound is disable.
            g.drawPixmap(Assets.buttons, 0, 416, 64, 0, 64, 64);
        }
    }

    /**
     * Save the game if the game is being paused.
     */
    public void pause() {
        Settings.save(game.getFileIO());
    }

    public void resume() {
    }

    public void dispose() {
    }
}
