package edu.angelo.finalprojecttran;

import com.badlogic.androidgames.framework.Game;
import com.badlogic.androidgames.framework.Graphics;
import com.badlogic.androidgames.framework.Screen;
import com.badlogic.androidgames.framework.Graphics.PixmapFormat;

public class LoadingScreen extends Screen {
    public LoadingScreen(Game game) {
        super(game);
    }

    /**
     * Load the game's graphic and sound assets throughout the game.
     * @param deltaTime Load the assets with delta (real time).
     */
    public void update(float deltaTime) {
        Graphics g = game.getGraphics();

        //Game's background assets.
        Assets.background = g.newPixmap("background.png", PixmapFormat.ARGB8888);

        //Game's logo bomb em'!
        Assets.logo = g.newPixmap("logo.png", PixmapFormat.ARGB8888);

        //Main menu assets.
        Assets.mainMenu = g.newPixmap("mainmenu.png", PixmapFormat.ARGB8888);

        //The pause / continue buttons.
        Assets.buttons = g.newPixmap("buttons.png", PixmapFormat.ARGB8888);

        //Help screen 1.
        Assets.help1 = g.newPixmap("help1.png", PixmapFormat.ARGB8888);

        //Help screen 2.
        Assets.help2 = g.newPixmap("help2.png", PixmapFormat.ARGB8888);

        //Help screen 3.
        Assets.help3 = g.newPixmap("help3.png", PixmapFormat.ARGB8888);

        //Help screen 4.
        Assets.help4 = g.newPixmap("help4.png", PixmapFormat.ARGB8888);

        //Numbers used to draw ingame.
        Assets.numbers = g.newPixmap("numbers.png", PixmapFormat.ARGB8888);

        //Ready to play UI assets.
        Assets.ready = g.newPixmap("ready.png", PixmapFormat.ARGB8888);

        //Pause UI.
        Assets.pause = g.newPixmap("pausemenu.png", PixmapFormat.ARGB8888);

        //Game over UI.
        Assets.gameOver = g.newPixmap("gameover.png", PixmapFormat.ARGB8888);

        //Replay button.
        Assets.replayButton = g.newPixmap("replay.png", PixmapFormat.ARGB8888);

        //Up down left right button.
        Assets.upButton = g.newPixmap("upButton.png", PixmapFormat.ARGB8888);
        Assets.leftButton = g.newPixmap("leftButton.png", PixmapFormat.ARGB8888);
        Assets.rightButton = g.newPixmap("rightButton.png", PixmapFormat.ARGB8888);
        Assets.downButton = g.newPixmap("downButton.png", PixmapFormat.ARGB8888);

        //Bomb button asset.
        Assets.bombButton = g.newPixmap("bombButton.png", PixmapFormat.ARGB8888);

        //Bomb game asset.
        //Wall / Box / Bomb / Fire Explosion / Bomb man (Player) / Monster / Monster's spike up-down-left-right.
        Assets.wall = g.newPixmap("wall.png", PixmapFormat.ARGB8888);
        Assets.box = g.newPixmap("box.png", PixmapFormat.ARGB8888);
        Assets.bomb = g.newPixmap("bomb.png", PixmapFormat.ARGB8888);
        Assets.fire = g.newPixmap("fire.png", PixmapFormat.ARGB8888);
        Assets.player = g.newPixmap("bombman.png", PixmapFormat.ARGB8888);
        Assets.monster = g.newPixmap("target.png", PixmapFormat.ARGB8888);
        Assets.monsterSpikeUpward = g.newPixmap("spikeUpward.png", PixmapFormat.ARGB8888);
        Assets.monsterSpikeLeftward = g.newPixmap("spikeLeftward.png", PixmapFormat.ARGB8888);
        Assets.monsterSpikeRightward = g.newPixmap("spikeRightward.png", PixmapFormat.ARGB8888);
        Assets.monsterSpikeDownward = g.newPixmap("spikeDownward.png", PixmapFormat.ARGB8888);

        //Portal (to continue to the next level).
        Assets.portal = g.newPixmap("portal.png", PixmapFormat.ARGB8888);

        //Player's health.
        Assets.health = g.newPixmap("health.png", PixmapFormat.ARGB8888);

        //Health pack ingame assets.
        Assets.healthBooster = g.newPixmap("healthPack.png", PixmapFormat.ARGB8888);

        //Level display assets.
        Assets.level = g.newPixmap("level.png", PixmapFormat.ARGB8888);

        //Click sounds.
        Assets.click = game.getAudio().newSound("click.ogg");

        //Earn points sound.
        Assets.earnPoint = game.getAudio().newSound("earnpoints.ogg");

        //Player received damage sound.
        Assets.damaged = game.getAudio().newSound("damaged.ogg");

        //Bomb exploding sound.
        Assets.bombExplode = game.getAudio().newSound("explode.ogg");

        //Game over sound.
        Assets.gameOverSound = game.getAudio().newSound("gameover.ogg");

        //Pause game sound.
        Assets.paused = game.getAudio().newSound("paused.ogg");

        Settings.load(game.getFileIO());
        game.setScreen(new MainMenuScreen(game));
    }

    public void present(float deltaTime) {
    }

    public void pause() {
    }

    public void resume() {
    }

    public void dispose() {
    }
}
