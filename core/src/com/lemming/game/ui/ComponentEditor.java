package com.lemming.game.ui;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Array;
import com.lemming.game.basic.Assets;
import com.lemming.game.comps.Comp;

/**
 * Created by Alexander on 17.07.2015.
 */
public class ComponentEditor extends Table {
    private final Comp component;

    public ComponentEditor(Comp c) {
        super(Assets.DEFAULT_SKIN);
        this.component = c;
        update();
    }

    public void update(){
        Array<EditableValue> values = component.getValues();
        this.clear();
        final CheckBox enabled = new CheckBox("enabled", Assets.DEFAULT_SKIN);
        enabled.setChecked(component.isEnabled());
        enabled.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                component.setEnabled(enabled.isChecked());
            }
        });
        add(enabled);
        row();
        for(EditableValue e : values){
            add(new ParameterEditor(e, this)).left();
            row();
        }
    }
}
