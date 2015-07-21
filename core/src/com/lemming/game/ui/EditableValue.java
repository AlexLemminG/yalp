package com.lemming.game.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.lemming.game.basic.GObject;
import com.lemming.game.comps.BodyComp;
import com.lemming.game.comps.HasColor;
import com.lemming.game.comps.HasRadius;
import com.lemming.game.comps.ImageComp;
import com.lemming.game.utils.Utils;

import java.util.HashMap;

/**
 * Created by Alexander on 17.07.2015.
 */
public abstract class EditableValue<T> implements Cloneable{
    public String name;
    public boolean updateEditor = false;
    Object owner;
    public abstract T get();
    public abstract void set(T value);
    public abstract String getStr();
    public abstract void setStr(String value);

    @Override
    protected EditableValue clone() throws CloneNotSupportedException {
        return ((EditableValue) super.clone());
    }

    public abstract static class FloatValue extends EditableValue<Float>{
        public FloatValue(){}

        private float f;

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
    }
    public abstract static class IntValue extends EditableValue<Integer>{
        public IntValue(){}

        private int anInt;

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
    }
    public abstract static class Vector2Value extends EditableValue<Vector2>{
        public Vector2Value(){}

        private Vector2 anInt;

        public Vector2Value(Vector2 anInt){
            this.anInt = anInt;
        }

        @Override
        public Vector2 get() {
            return anInt;
        }

        @Override
        public void set(Vector2 value) {
            anInt = value;
        }

        @Override
        public String getStr(){
            return get() + "";
        }

        @Override
        public void setStr(String value){
//            set();
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
        @Override
        public Float get() {
            Vector2 owner = ((Vector2) this.owner);
            return owner.x;
        }

        @Override
        public void set(Float value) {
            Vector2 owner = ((Vector2) this.owner);
            owner.x = value;
        }
    };
    private static FloatValue vectorY = new FloatValue() {
        @Override
        public Float get() {
            Vector2 owner = ((Vector2) this.owner);
            return owner.y;
        }

        @Override
        public void set(Float value) {
            Vector2 owner = ((Vector2) this.owner);
            owner.y = value;
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
    private static FloatValue colorB = new FloatValue() {
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
