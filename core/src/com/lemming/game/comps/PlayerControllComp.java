package com.lemming.game.comps;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.lemming.game.basic.GObject;

/**
 * Created by Alexander on 25.07.2015.
 */
public class PlayerControllComp extends Comp{
    public PlayerControllComp(GObject gObject) {
        super(gObject);
    }
    UnitComp unitComp;
    @Override
    public void onCreate() {
        super.onCreate();
        unitComp = owner.getComponent(UnitComp.class);
        InputAdapter inputAdapter = new InputAdapter() {
            @Override
            public boolean keyDown(int keycode) {
                if(isEnabled()) {
                    if (keycode == Input.Keys.J) {
                        unitComp.jump();
                    }
                    if(keycode == Input.Keys.LEFT){
                        unitComp.runLeft();
                    }
                    if(keycode == Input.Keys.RIGHT){
                        unitComp.runRight();
                    }
                }
                return super.keyDown(keycode);
            }
        };
        ((InputMultiplexer) Gdx.input.getInputProcessor()).addProcessor(inputAdapter);
    }

}
