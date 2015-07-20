package com.lemming.game.ui;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Array;
import com.lemming.game.basic.Assets;
import com.lemming.game.basic.GObject;
import com.lemming.game.comps.Comp;

/**
 * Created by Alexander on 17.07.2015.
 */
public class ComponentsList extends Table{
    SelectBox<Comp> compSelectBox;
    ComponentEditor editor;
    public ComponentsList(GObject gObject) {
        super(Assets.DEFAULT_SKIN);
        final Array<Comp> components = gObject.components;
//        final ComponentsList l = this;
        compSelectBox = new SelectBox<Comp>(Assets.DEFAULT_SKIN);
        add(compSelectBox).left();
        row();
        compSelectBox.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                update();
            }
        });
        compSelectBox.setItems(components);
//        editor = new ComponentEditor(compSelectBox.getSelected());
    }

    public void update(){
//        System.out.println("hi");
        if (editor != null) {
            editor.remove();
        }
        editor = new ComponentEditor(this.compSelectBox.getSelected());
        this.add(editor);
    }
}
