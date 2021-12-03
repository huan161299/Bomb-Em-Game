package edu.angelo.finalprojecttran;

import com.badlogic.androidgames.framework.Game;
import com.badlogic.androidgames.framework.Graphics;
import com.badlogic.androidgames.framework.Screen;
import com.badlogic.androidgames.framework.Input.TouchEvent;

import java.util.List;

/**
 * Display the whole High Score UI screen. It indicates the highest score that the player has achieve so far.
 */

public class HighscoreScreen extends Screen {
    /**
     * Store the top 5 highest score.
     */
    String[] lines = new String[5];

    /**
     * Create a new game activity. Store all of top 5 highest score into the lines[] string array.
     * @param game this game activity.
     */
    public HighscoreScreen(Game game) {
        super(game);

        //Get all of the high score into the lines string array.
        for (int i = 0; i < 5; i += 1) {
            lines[i] = "" + (i + 1) + ". " + Settings.highscores[i];
        }
    }

    /**
     * Update the game screen, get the user's input and allow transition back into the main menu screen.
     * @param deltaTime
     */
    @Override
    public void update(float deltaTime) {
        List<TouchEvent> touchEvents = game.getInput().getTouchEvents();
        game.getInput().getKeyEvents();

        int len = touchEvents.size();
        for (int i = 0; i < len; i += 1) {
            TouchEvent event = touchEvents.get(i);
            if (event.type == TouchEvent.TOUCH_UP) {
                if (event.x < 64 && event.y > 416) {
                    if(Settings.soundEnabled) {
                        Assets.click.play(1);
                    }
                    game.setScreen(new MainMenuScreen(game));
                    return;
                }
            }
        }
    }

    /**
     * Draw the high scrore UI assets out.
     * @param deltaTime
     */
    @Override
    public void present(float deltaTime) {
        Graphics g = game.getGraphics();

        //Draw the background.
        g.drawPixmap(Assets.background, 0, 0);
        g.drawPixmap(Assets.mainMenu, 64, 20, 0, 42, 196, 42);

        //Draw the top 5 highest score.
        int y = 100;
        for (int i = 0; i < 5; i += 1) {
            drawText(g, lines[i], 20, y);
            y += 50;
        }

        //Draw the return button.
        g.drawPixmap(Assets.buttons, 0, 416, 64, 64, 64, 64);
    }

    /**
     * Draw the text (high score) from the number assets.
     * @param g get the game's graphic to draw on.
     * @param line the string to write / draw on the screen.
     * @param x position to draw the string out on the screen.
     * @param y position to draw the string out on the screen.
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

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void dispose() {
    }
}
