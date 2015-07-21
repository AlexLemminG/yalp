package com.lemming.game;

import com.badlogic.gdx.Game;
import com.lemming.game.editor.EditorScreen;

public class MGame extends Game {
    /*TODO
    3 - polylinecomp (But why?) DOOooooo it
    4 - pointParameterEditor
    5 - переместить ParameterEditor в EditableValue
     */
    @Override
    public void create() {
        setScreen(new EditorScreen());
    }
}
