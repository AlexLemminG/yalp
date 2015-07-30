package com.lemming.game.comps;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlWriter;
import com.lemming.game.basic.GObject;
import com.lemming.game.basic.View;
import com.lemming.game.ui.EditableValue;

import java.io.IOException;

/**
 * Created by Alexander on 16.07.2015.
 */
public class Comp {
    private static final String name = "Component";
    protected GObject owner;
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

    public void save(XmlWriter w) throws IOException {
        Array<EditableValue> values = getValues();
        w.element(getClass().getName());
        for(EditableValue v : values){
            w.element(v.name, v.getStr());
        }
        w.pop();
    }

    public void load(XmlReader.Element e){
        Array<EditableValue> values = getValues();
        for(EditableValue v : values){
            v.setStr(e.get(v.name));
        }
    }

    public static Comp createByName(String name, GObject o){
        if(name.equals(BodyComp.class.getName())) return new BodyComp(o);
        if(name.equals(PointLightComp.class.getName())) return new PointLightComp(o);
        else return new Comp(o);
    }

    public void dispose(){}
}
