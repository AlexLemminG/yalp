package com.lemming.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.ColorAction;
import com.lemming.game.editor.EditorScreen;

public class MGame extends Game {
    /*TODO
    3 - polylinecomp (But why?) DOOooooo it
     */
    @Override
    public void create() {
        setScreen(new EditorScreen());
    }
}
