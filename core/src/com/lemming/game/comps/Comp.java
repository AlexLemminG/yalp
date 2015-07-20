package com.lemming.game.comps;

import com.badlogic.gdx.utils.Array;
import com.lemming.game.basic.GObject;
import com.lemming.game.basic.View;
import com.lemming.game.ui.EditableValue;

/**
 * Created by Alexander on 16.07.2015.
 */
public class Comp {
    GObject owner;
    private boolean enabled = true;

    public Comp(GObject gObject){
        this.owner = gObject;
        owner.components.add(this);
    }

    public void onCreate(){

    }

    public void update(float dt){

    }

    public void render(View view){

    }

    public Array<EditableValue> getValues(){
        return new Array<EditableValue>();
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
