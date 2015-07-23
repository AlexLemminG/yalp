package com.lemming.game;

import com.badlogic.gdx.Game;
import com.lemming.game.editor.EditorScreen;

public class MGame extends Game {
    /*TODO
    3 - polylinecomp make it polygon
    3.5 polylineComp make it Path?
    4 - followPathComp
    5 - move some to GUI
     */
    @Override
    public void create() {
        setScreen(new EditorScreen());
    }
}
