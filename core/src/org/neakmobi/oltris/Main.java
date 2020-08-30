package org.neakmobi.oltris;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;

public class Main extends Game {

    @Override
    public void create() {
        Screen gameScreen = new GameScreen();
        setScreen(gameScreen);
    }

}
