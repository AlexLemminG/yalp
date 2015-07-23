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
    private EditableValue.BoolValue enabled = new EditableValue.BoolValue(true){
        @Override
        public void set(Boolean value) {
            super.set(value);
            Comp.this.setEnabled(value);
        }
    };

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
        Array<EditableValue> editableValues = new Array<EditableValue>();
        enabled.name = "enabled";
        editableValues.add(enabled);
        return editableValues;
    }

    public boolean isEnabled() {
        return enabled.get();
    }

    public void setEnabled(boolean enabled) {
        this.enabled.value = enabled;
    }
}
