package com.lemming.game.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.FocusListener;
import com.lemming.game.basic.GObject;
import com.lemming.game.comps.*;
import com.lemming.game.trash.TextureFromFile;
import com.lemming.game.utils.Utils;

import java.util.HashMap;

/**
 * Created by Alexander on 17.07.2015.
 */
public abstract class EditableValue<T> implements Cloneable{
    private static class DefaultTextFieldListener extends InputListener implements EventListener{
        private TextField textField;
        private EditableValue value;

        public DefaultTextFieldListener(TextField textField, EditableValue value, Table componentEditor) {
            this.textField = textField;
            this.value = value;
            this.componentEditor = componentEditor instanceof ComponentEditor ? ((ComponentEditor) componentEditor) : null;
        }

        private ComponentEditor componentEditor;

        @Override
        public boolean keyDown(InputEvent event, int keycode) {


            if (keycode == Input.Keys.ENTER) {
                update();
            }
            return super.keyDown(event, keycode);
        }
        private void update(){
            try {
                value.setStr(textField.getText());
                textField.setColor(Color.WHITE);
                if(value.updateEditor && componentEditor != null)
                    componentEditor.update();
            }catch (IllegalArgumentException e){
                textField.setColor(Color.RED);
            }
        }

        @Override
        public boolean handle(Event e) {
            update();
            if(e instanceof FocusListener.FocusEvent){
                if(((FocusListener.FocusEvent) e).getType() == FocusListener.FocusEvent.Type.keyboard && !((FocusListener.FocusEvent) e).isFocused()){
                    update();
                }
            }
            return super.handle(e);
        }
    }
    private static class UpdateValueAction extends Action{
        private EditableValue value;
        public UpdateValueAction(EditableValue value) {
            super();
            this.value = value;
        }
        String oldValue;
        @Override
        public boolean act(float delta) {
            TextField tf = ((TextField) getActor());
            if(!value.getStr().equals(oldValue)){
                tf.setText(value.getStr());
                oldValue = value.getStr();
            }
            return false;
        }
    }
    private static class FloatLabelListener extends ActorGestureListener{
        float speed = 0;
        EditableValue.FloatValue value;

        public FloatLabelListener(EditableValue.FloatValue value) {
            this.value = value;
        }

        @Override
        public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
            Gdx.input.setCursorCatched(false);
        }

        @Override
        public void touchDown(InputEvent event, float x, float y, int pointer, int button) {
            try {
                Gdx.input.setCursorCatched(true);
                float v = value.get();
                speed = MathUtils.clamp(Math.abs(v) / 100, 0.01f, Float.POSITIVE_INFINITY);
            } catch (NumberFormatException e) {
            }
        }

        @Override
        public void pan(InputEvent event, float x, float y, float deltaX, float deltaY) {
            try {
                float v = value.get();
                v += deltaX * speed;
                value.setStr("" + v);
            } catch (NumberFormatException e) {
            }
            super.pan(event, x, y, deltaX, deltaY);
        }
    }
    private static class IntLabelListener extends ActorGestureListener{
        float speed = 0;
        float v;
        EditableValue.IntValue value;

        public IntLabelListener(EditableValue.IntValue value) {
            this.value = value;
        }

        @Override
        public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
            Gdx.input.setCursorCatched(false);
        }

        @Override
        public void touchDown(InputEvent event, float x, float y, int pointer, int button) {
            try {
                Gdx.input.setCursorCatched(true);
                v = value.get();
                speed = ( MathUtils.clamp(Math.abs(v) / 100f, 0.01f, Integer.MAX_VALUE));
            } catch (NumberFormatException e) {
            }
        }

        @Override
        public void pan(InputEvent event, float x, float y, float deltaX, float deltaY) {
            try {
                v += deltaX * speed;
                value.setStr("" + (int)v);
            } catch (NumberFormatException e) {
            }
            super.pan(event, x, y, deltaX, deltaY);
        }
    }
    private static class Vector2LabelListener extends ActorGestureListener{
        float speed = 0;
        Vector2Value value;

        public Vector2LabelListener(Vector2Value value) {
            this.value = value;
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

        @Override
        public void pan(InputEvent event, float x, float y, float deltaX, float deltaY) {
            try {
                Vector2 v = value.get();
                value.set(v.add(deltaX * speed, deltaY * speed));
            } catch (NumberFormatException e) {
            }
            super.pan(event, x, y, deltaX, deltaY);
        }
    }



    public String name;
    public boolean updateEditor = false;
    Object owner;
    public abstract T get();
    public abstract void set(T value);
    public abstract String getStr();
    public abstract void setStr(String value);
    public void addToTable(Table componentEditor){
        Label label = new Label(name, componentEditor.getSkin());
        TextField tf = new TextField(getStr(), componentEditor.getSkin());
        tf.addAction(new UpdateValueAction(this));
        tf.addListener(new DefaultTextFieldListener(tf, this, componentEditor));
        componentEditor.add(label);
        componentEditor.add(tf);
    }



    @Override
    protected EditableValue clone() throws CloneNotSupportedException {
        return ((EditableValue) super.clone());
    }

    public static class Vector2Value extends EditableValue<Vector2>{
        public Vector2Value(){}

        private Vector2 f;

        public Vector2Value(Vector2 f, String name){
            this.f = f;
            this.name = name;
        }
        public Vector2Value(Vector2 f){
            this.f = f;
        }

        @Override
        public Vector2 get() {
            return f;
        }

        @Override
        public void set(Vector2 value) {
            f.set(value);
        }

        @Override
        public String getStr(){
            return get() + "";
        }
    Vector2 temp = new Vector2();
        @Override
        public void setStr(String value){
            value = value.replace('[', ' ');
            value = value.replace(':', ' ');
            value = value.replace(']', ' ');
            String[] v = value.split(" ");
            set(temp.set(Float.valueOf(v[1]), Float.valueOf(v[3])));
        }

        @Override
        public void addToTable(Table componentEditor) {
            Label label = new Label(name, componentEditor.getSkin());
            label.addListener(new Vector2LabelListener(this));
            componentEditor.add(label);
            EditableValue x = getValue(this, "vectorX");
            EditableValue y = getValue(this, "vectorY");
            x.name = "x";
            y.name = "y";

            componentEditor.row();
            x.addToTable(componentEditor);
            componentEditor.row();
            y.addToTable(componentEditor);

        }
    }
    public static class FloatValue extends EditableValue<Float>{
        public FloatValue(){}

        private float f;

        public FloatValue(Float f, String name){
            this.f = f;
            this.name = name;
        }
        public FloatValue(Float f){
            this.f = f;
        }

        @Override
        public Float get() {
            return f;
        }

        @Override
        public void set(Float value) {
            f = value;
        }

        @Override
        public String getStr(){
            return get() + "";
        }

        @Override
        public void setStr(String value){
            set(Float.valueOf(value));
        }

        @Override
        public void addToTable(Table componentEditor) {
            Label label = new Label(name, componentEditor.getSkin());
            label.addListener(new FloatLabelListener(this));
            TextField tf = new TextField(getStr(), componentEditor.getSkin());
            tf.addAction(new UpdateValueAction(this));
            tf.addListener(new DefaultTextFieldListener(tf, this, componentEditor));
            componentEditor.add(label);
            componentEditor.add(tf);
        }
    }
    public static class IntValue extends EditableValue<Integer>{
        public IntValue(){}

        private int anInt;

        public IntValue(Integer anInt, String name){
            this.anInt = anInt;
            this.name = name;
        }
        public IntValue(Integer anInt){
            this.anInt = anInt;
        }

        @Override
        public Integer get() {
            return anInt;
        }

        @Override
        public void set(Integer value) {
            anInt = value;
        }

        @Override
        public String getStr(){
            return get() + "";
        }

        @Override
        public void setStr(String value){
            set(Integer.valueOf(value));
        }

        @Override
        public void addToTable(Table componentEditor) {
            Label label = new Label(name, componentEditor.getSkin());
            label.addListener(new IntLabelListener(this));
            TextField tf = new TextField(getStr(), componentEditor.getSkin());
            tf.addAction(new UpdateValueAction(this));
            tf.addListener(new DefaultTextFieldListener(tf, this, componentEditor));
            componentEditor.add(label);
            componentEditor.add(tf);
        }
    }
    public static class BoolValue extends EditableValue<Boolean>{
        public BoolValue(){}

        public boolean value;

        public BoolValue(Boolean value, String name){
            this.value = value;
            this.name = name;
        }
        public BoolValue(Boolean value){
            this.value = value;
        }

        @Override
        public Boolean get() {
            return value;
        }

        @Override
        public void set(Boolean value) {
            this.value = value;
        }

        @Override
        public String getStr(){
            return get() + "";
        }

        @Override
        public void addToTable(Table componentEditor) {
//            Label label = new Label(name, componentEditor.getSkin());
            final CheckBox cb = new CheckBox(name, componentEditor.getSkin());
            cb.setChecked(value);
            cb.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    set(cb.isChecked());
                }
            });

//            componentEditor.add(label);
            componentEditor.add(cb);
        }

        @Override
        public void setStr(String value){
            set(Boolean.valueOf(value));

        }
    }
    public abstract static class TextureValue extends EditableValue<com.badlogic.gdx.graphics.Texture>{
        public TextureValue(){}
        TextureFromFile textureFromFile;

        public TextureValue(HasTexture owner){
            this.owner = owner;
        }

        @Override
        public Texture get() {
            return textureFromFile.texture;
        }

        @Override
        public void set(Texture value) {
            textureFromFile.texture = value;
            ((HasTexture) owner).setTexture(value);
        }

        @Override
        public String getStr() {
            return textureFromFile.fileName;
        }

        @Override
        public void setStr(String value) {
            textureFromFile.set(value);
        }

    }

    private static FloatValue x = new FloatValue() {
        @Override
        public Float get() {
            GObject owner = ((GObject) this.owner);
            return owner.pos.x;
        }

        @Override
        public void set(Float value) throws NumberFormatException{
            GObject owner = ((GObject) this.owner);
            owner.setPos(value, owner.pos.y);
        }
    };
    private static FloatValue y = new FloatValue() {
        @Override
        public Float  get() {
            GObject owner = ((GObject) this.owner);
            return owner.pos.y;
        }

        @Override
        public void set(Float value) throws NumberFormatException{
            GObject owner = ((GObject) this.owner);
            owner.setPos(owner.pos.x, value);
        }
    };
    private static FloatValue aRad = new FloatValue() {
        @Override
        public Float get() {
            GObject owner = ((GObject) this.owner);
            return owner.a;
        }

        @Override
        public void set(Float value) throws NumberFormatException{
            GObject owner = ((GObject) this.owner);
            owner.setA(value);
        }
    };
    private static FloatValue aDeg = new FloatValue() {
        @Override
        public Float get() {
            GObject owner = ((GObject) this.owner);
            return owner.a* MathUtils.radDeg;
        }

        @Override
        public void set(Float value) throws NumberFormatException{
            GObject owner = ((GObject) this.owner);
            owner.setA(value * MathUtils.degRad);
        }
    };
    private static FloatValue originX = new FloatValue() {
        @Override
        public Float get() {
            ImageComp owner = ((ImageComp) this.owner);
            return owner.sprite.getOriginX();
        }

        @Override
        public void set(Float value) {
            ImageComp owner = ((ImageComp) this.owner);
            owner.sprite.setOrigin(value, owner.sprite.getOriginY());
        }
    };
    private static FloatValue originY = new FloatValue() {
        @Override
        public Float get() {
            ImageComp owner = ((ImageComp) this.owner);
            return owner.sprite.getOriginY();
        }

        @Override
        public void set(Float value) {
            ImageComp owner = ((ImageComp) this.owner);
            owner.sprite.setOrigin(owner.sprite.getOriginX(), value);
        }
    };
    private static FloatValue scaleX = new FloatValue() {
        @Override
        public Float get() {
            ImageComp owner = ((ImageComp) this.owner);
            return owner.sprite.getScaleX();
        }

        @Override
        public void set(Float value) {
            ImageComp owner = ((ImageComp) this.owner);
            owner.sprite.setScale(value, owner.sprite.getScaleY());
        }
    };
    private static FloatValue scaleY = new FloatValue() {
        @Override
        public Float get() {
            ImageComp owner = ((ImageComp) this.owner);
            return owner.sprite.getScaleY();
        }

        @Override
        public void set(Float value) {
            ImageComp owner = ((ImageComp) this.owner);
            owner.sprite.setScale(owner.sprite.getScaleX(), value);
        }
    };
    private static FloatValue velocityX = new FloatValue() {
        @Override
        public Float get() {
            BodyComp owner = ((BodyComp) this.owner);
            return owner.body.getLinearVelocity().x;
        }

        @Override
        public void set(Float value) {
            BodyComp owner = ((BodyComp) this.owner);
            owner.body.setLinearVelocity(value, owner.body.getLinearVelocity().y);
        }
    };
    private static FloatValue velocityY = new FloatValue() {
        @Override
        public Float get() {
            BodyComp owner = ((BodyComp) this.owner);
            return owner.body.getLinearVelocity().y;
        }

        @Override
        public void set(Float value) {
            BodyComp owner = ((BodyComp) this.owner);
            owner.body.setLinearVelocity(owner.body.getLinearVelocity().x, value);
        }
    };
    private static FloatValue velocityA = new FloatValue() {
        @Override
        public Float get() {
            BodyComp owner = ((BodyComp) this.owner);
            return owner.body.getAngularVelocity();
        }

        @Override
        public void set(Float value) {
            BodyComp owner = ((BodyComp) this.owner);
            owner.body.setAngularVelocity(value);
        }
    };
    private static FloatValue radius = new FloatValue() {
        @Override
        public Float get() {
            HasRadius owner = ((HasRadius) this.owner);
            return owner.getRadius();
        }

        @Override
        public void set(Float value) {
            HasRadius owner = ((HasRadius) this.owner);
            owner.setRadius(value);
        }
    };
    private static FloatValue vectorX = new FloatValue() {
        Vector2 temp = new Vector2();
        @Override
        public Float get() {
            Vector2Value owner = ((Vector2Value) this.owner);
            return owner.get().x;
        }

        @Override
        public void set(Float value) {
            Vector2Value owner = ((Vector2Value) this.owner);
            owner.set(temp.set(value, owner.get().y));
        }
    };
    private static FloatValue vectorY = new FloatValue() {
        Vector2 temp = new Vector2();
        @Override
        public Float get() {
            Vector2Value owner = ((Vector2Value) this.owner);
            return owner.get().y;
        }

        @Override
        public void set(Float value) {
            Vector2Value owner = ((Vector2Value) this.owner);
            owner.set(temp.set(owner.get().x, value));
        }
    };
    private static FloatValue colorR = new FloatValue() {
        @Override
        public Float get() {
            Color owner = ((Color) this.owner);
            return owner.r;
        }

        @Override
        public void set(Float value) {
            value = MathUtils.clamp(value, 0, 1);
            Color owner = ((Color) this.owner);
            owner.r = value;
        }
    };
    private static FloatValue colorG = new FloatValue() {
        @Override
        public Float get() {
            Color owner = ((Color) this.owner);
            return owner.g;
        }

        @Override
        public void set(Float value) {
            value = MathUtils.clamp(value, 0, 1);
            Color owner = ((Color) this.owner);
            owner.g = value;
        }
    };
    public static FloatValue colorB = new FloatValue() {
        @Override
        public Float get() {
            Color owner = ((Color) this.owner);
            return owner.b;
        }

        @Override
        public void set(Float value) {
            value = MathUtils.clamp(value, 0, 1);
            Color owner = ((Color) this.owner);
            owner.b = value;
        }
    };
    private static FloatValue colorA = new FloatValue() {
        @Override
        public Float get() {
            Color owner = ((Color) this.owner);
            return owner.a;
        }

        @Override
        public void set(Float value) {
            value = MathUtils.clamp(value, 0, 1);
            Color owner = ((Color) this.owner);
            owner.a = value;
        }
    };
    private static EditableValue<Color> color = new EditableValue<Color>() {
        @Override
        public String getStr() {
            HasColor owner = ((HasColor) this.owner);
            return Utils.colorToString(owner.getColor());
        }

        @Override
        public void setStr(String value) {
            HasColor owner = ((HasColor) this.owner);
            set(Utils.stringToColor(value));
        }

        @Override
        public Color get() {
            return ((HasColor) owner).getColor();
        }

        @Override
        public void set(Color value) {
            ((HasColor) owner).setColor(value);
        }

        @Override
        public void addToTable(Table componentEditor) {
            super.addToTable(componentEditor);
            EditableValue r = EditableValue.getValue((get()), "colorR");
            EditableValue g = EditableValue.getValue((get()), "colorG");
            EditableValue b = EditableValue.getValue((get()), "colorB");
            EditableValue a = EditableValue.getValue((get()), "colorA");
            componentEditor.row();
            r.addToTable(componentEditor);
            componentEditor.row();
            g.addToTable(componentEditor);
            componentEditor.row();
            b.addToTable(componentEditor);
            componentEditor.row();
            a.addToTable(componentEditor);
        }
    };
    private static EditableValue<Texture> textureFromFile = new EditableValue<Texture>(){
        TextureFromFile textureFromFile = new TextureFromFile("");

        @Override
        public Texture get() {
            return textureFromFile.texture;
        }

        @Override
        public void set(Texture value) {
            textureFromFile.texture = value;
            if(textureFromFile.texture != null)
                ((HasTexture) owner).setTexture(value);
        }

        @Override
        public String getStr() {
            return textureFromFile.fileName;
        }

        @Override
        public void setStr(String value) {
            textureFromFile.set(value);
            set(textureFromFile.texture);
        }
    };


    private static HashMap<String, EditableValue> values = new HashMap<String, EditableValue>();
    static{
        put("x", x);
        put("y", y);
        put("vectorX", vectorX);
        put("vectorY", vectorY);
        put("aRad", aRad);
        put("a", aDeg);
        put("originX", originX);
        put("originY", originY);
        put("scaleX", scaleX);
        put("scaleY", scaleY);
        put("velocityX", velocityX);
        put("velocityY", velocityY);
        put("velocityA", velocityA);
        put("color", color);
        put("colorR", colorR);
        put("colorG", colorG);
        put("colorB", colorB);
        put("colorA", colorA);
        put("radius", radius);
        put("textureFromFile", textureFromFile);
    }
    private static void put(String name, EditableValue value){
        values.put(name, value);
        value.name = name;
    }

    public static EditableValue getValue(final Object object, String valueName){
        try {
            EditableValue result = values.get(valueName);
            if(result != null) {
                result = result.clone();
                result.owner = object;
//                result.name = valueName;
            }
            return result;
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return null;
    }
}
