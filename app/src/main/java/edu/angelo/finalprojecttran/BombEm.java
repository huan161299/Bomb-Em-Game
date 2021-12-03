package edu.angelo.finalprojecttran;

import com.badlogic.androidgames.framework.Screen;
import com.badlogic.androidgames.framework.impl.AndroidGame;

public class BombEm extends AndroidGame {

    public Screen getStartScreen() {
        return new LoadingScreen(this);
    }
}
