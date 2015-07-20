package com.lemming.game.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.lemming.game.basic.Action;
import com.lemming.game.basic.Assets;

/**
 * Created by Alexander on 17.07.2015.
 */
public class ParameterEditor extends Table {
    String paramName;
    Object owner;
    TextField textField;
    String oldValue;
    EditableValue value;
    Action enterAction;
    ComponentEditor componentEditor;

    public ParameterEditor(Object owner, String paramName, ComponentEditor componentEditor) {
        this(EditableValue.getValue(owner, paramName), componentEditor); //Assets.DEFAULT_SKIN);
        this.paramName = paramName;
//        value = EditableValue.getValue(owner, paramName);
    }

    public ParameterEditor(final EditableValue value, ComponentEditor componentEditor){
        super(Assets.DEFAULT_SKIN);
        this.componentEditor = componentEditor;




        this.value = value;
        textField = new TextField(oldValue = value.get(), Assets.DEFAULT_SKIN);
        enterAction = new Action() {
            @Override
            public void act() {
                defaultEnterAction();
            }
        };
        textField.addListener(new InputListener(){
            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                if(keycode == Input.Keys.ENTER){
                    enterAction.act();
                }
                return super.keyDown(event, keycode);
            }
        });
        final Label label = new Label(paramName != null ? paramName : value.name, Assets.DEFAULT_SKIN);
        label.addListener(new ActorGestureListener(){
            float speed = 0;

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                Gdx.input.setCursorCatched(false);
            }

            @Override
            public void touchDown(InputEvent event, float x, float y, int pointer, int button) {
                try{
                    Gdx.input.setCursorCatched(true);
                    float v = Float.parseFloat(value.get());
                    speed = MathUtils.clamp(Math.abs(v) / 100, 0.01f, Float.POSITIVE_INFINITY);
                }catch (NumberFormatException e){}
            }

            @Override
            public void pan(InputEvent event, float x, float y, float deltaX, float deltaY) {
                try{
                    float v = Float.parseFloat(value.get());
                    v += deltaX * speed;
                    value.set("" + v);
                }catch (NumberFormatException e){}
                super.pan(event, x, y, deltaX, deltaY);
            }
        });
        label.setWidth(25);
        add(label).left();
        textField.setWidth(25);
        add(textField).right().fill(true,false);
    }

    public void defaultEnterAction(){
        try {
            value.set(textField.getText());
            textField.setColor(Color.WHITE);
            if(value.updateEditor && componentEditor != null){
                componentEditor.update();
            }
        }catch (NumberFormatException e){
            textField.setColor(Color.RED);
        }
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        if(!oldValue.equals(value.get())){
            textField.setText(value.get());
            oldValue = value.get();
        }
    }

    public Object getOwner(){
        return owner;
    }
}
