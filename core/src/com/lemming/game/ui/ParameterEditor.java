package com.lemming.game.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
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


        Label label = new Label(paramName != null ? paramName : value.name, Assets.DEFAULT_SKIN);
        if(value instanceof EditableValue.FloatValue) {
            label.addListener(new FloatLabelListener(((EditableValue.FloatValue) value), this));
        }
        if(value instanceof EditableValue.Vector2Value){
            label.addListener(new Vector2LabelListener(((EditableValue.Vector2Value) value), this));
        }

        label.setWidth(25);

        textField = new TextField(oldValue = value.getStr(), Assets.DEFAULT_SKIN);
        textField.addListener(new DefaultTextFieldListener(textField, value, componentEditor));
        textField.setWidth(25);

        //КОСТЫЛЬ
        Table t = new Table(Assets.DEFAULT_SKIN);
        t.add(label).left();
        t.add(textField).right().fill(true, false);
        add(t);
        if(value.get() instanceof Color){
            EditableValue r = EditableValue.getValue((value.get()), "colorR");
            EditableValue g = EditableValue.getValue((value.get()), "colorG");
            EditableValue b = EditableValue.getValue((value.get()), "colorB");
            EditableValue a = EditableValue.getValue((value.get()), "colorA");
            row();
            add(new ParameterEditor(r, componentEditor));
            row();
            add(new ParameterEditor(g, componentEditor));
            row();
            add(new ParameterEditor(b, componentEditor));
            row();
            add(new ParameterEditor(a, componentEditor));
        }
    }

    public void defaultEnterAction(){
        try {
            value.setStr(textField.getText());
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
        if(!oldValue.equals(value.getStr())){
            textField.setText(value.getStr());
            oldValue = value.getStr();
        }
    }

    public Object getOwner(){
        return owner;
    }

    private static class DefaultTextFieldListener extends InputListener{
        private TextField textField;
        private EditableValue value;

        public DefaultTextFieldListener(TextField textField, EditableValue value, ComponentEditor componentEditor) {
            this.textField = textField;
            this.value = value;
            this.componentEditor = componentEditor;
        }

        private ComponentEditor componentEditor;

        @Override
            public boolean keyDown(InputEvent event, int keycode) {
            if (keycode == Input.Keys.ENTER) {
                try {
                    value.setStr(textField.getText());
                    textField.setColor(Color.WHITE);
                    if(value.updateEditor && componentEditor != null){
                        componentEditor.update();
                    }
                }catch (NumberFormatException e){
                    textField.setColor(Color.RED);
                }
            }
            return super.keyDown(event, keycode);
        }

    }
    private static class FloatLabelListener extends ActorGestureListener{
        float speed = 0;
        EditableValue.FloatValue value;
        ParameterEditor editor;

        public FloatLabelListener(EditableValue.FloatValue value, ParameterEditor editor) {
            this.value = value;
            this.editor = editor;
        }


        @Override
        public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
            Gdx.input.setCursorCatched(false);
        }

        @Override
        public void touchDown(InputEvent event, float x, float y, int pointer, int button) {
            try {
                Gdx.input.setCursorCatched(true);
                float v = Float.parseFloat(value.getStr());
                speed = MathUtils.clamp(Math.abs(v) / 100, 0.01f, Float.POSITIVE_INFINITY);
            } catch (NumberFormatException e) {
            }
        }

        @Override
        public void pan(InputEvent event, float x, float y, float deltaX, float deltaY) {
            try {
                float v = Float.parseFloat(editor.value.getStr());
                v += deltaX * speed;
                editor.value.setStr("" + v);
            } catch (NumberFormatException e) {
            }
            super.pan(event, x, y, deltaX, deltaY);
        }
    }
    private static class Vector2LabelListener extends ActorGestureListener{
        float speed = 0;
        EditableValue.Vector2Value value;
        ParameterEditor editor;

        public Vector2LabelListener(EditableValue.Vector2Value value, ParameterEditor editor) {
            this.value = value;
            this.editor = editor;
        }


        @Override
        public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
            Gdx.input.setCursorCatched(false);
        }

        @Override
        public void touchDown(InputEvent event, float x, float y, int pointer, int button) {
            try {
                Gdx.input.setCursorCatched(true);
                float v = value.get().len();
                speed = MathUtils.clamp(Math.abs(v) / 100, 0.01f, Float.POSITIVE_INFINITY);
            } catch (NumberFormatException e) {
            }
        }
        Vector2 temp = new Vector2();
        @Override
        public void pan(InputEvent event, float x, float y, float deltaX, float deltaY) {
            try {
                Vector2 old = value.get();
                temp.set(old.x + deltaX * speed, old.y + deltaY * speed);
                value.set(temp);
            } catch (NumberFormatException e) {
            }
            super.pan(event, x, y, deltaX, deltaY);
        }
    }
}
