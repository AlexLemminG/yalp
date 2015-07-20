package com.lemming.game.ui;

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
public abstract class EditableValue implements Cloneable{
    public String name;
    public boolean updateEditor = false;
    Object owner;
    public abstract String get();
    public abstract void set(String value);

    @Override
    protected EditableValue clone() throws CloneNotSupportedException {
        return ((EditableValue) super.clone());
    }

    private static EditableValue x = new EditableValue() {
        @Override
        public String get() {
            GObject owner = ((GObject) this.owner);
            return ""+owner.pos.x;
        }

        @Override
        public void set(String value) throws NumberFormatException{
            GObject owner = ((GObject) this.owner);
            owner.setPos(Float.valueOf(value), owner.pos.y);
        }
    };
    private static EditableValue y = new EditableValue() {
        @Override
        public String get() {
            GObject owner = ((GObject) this.owner);
            return ""+owner.pos.y;
        }

        @Override
        public void set(String value) throws NumberFormatException{
            GObject owner = ((GObject) this.owner);
            owner.setPos(owner.pos.x, Float.valueOf(value));
        }
    };
    private static EditableValue aRad = new EditableValue() {
        @Override
        public String get() {
            GObject owner = ((GObject) this.owner);
            return ""+owner.a;
        }

        @Override
        public void set(String value) throws NumberFormatException{
            GObject owner = ((GObject) this.owner);
            owner.setA(Float.valueOf(value));
        }
    };
    private static EditableValue aDeg = new EditableValue() {
        @Override
        public String get() {
            GObject owner = ((GObject) this.owner);
            return ""+owner.a* MathUtils.radDeg;
        }

        @Override
        public void set(String value) throws NumberFormatException{
            GObject owner = ((GObject) this.owner);
            owner.setA(Float.valueOf(value) * MathUtils.degRad);
        }
    };
    private static EditableValue originX = new EditableValue() {
        @Override
        public String get() {
            ImageComp owner = ((ImageComp) this.owner);
            return ""+owner.sprite.getOriginX();
        }

        @Override
        public void set(String value) {
            ImageComp owner = ((ImageComp) this.owner);
            owner.sprite.setOrigin(Float.valueOf(value), owner.sprite.getOriginY());
        }
    };
    private static EditableValue originY = new EditableValue() {
        @Override
        public String get() {
            ImageComp owner = ((ImageComp) this.owner);
            return ""+owner.sprite.getOriginY();
        }

        @Override
        public void set(String value) {
            ImageComp owner = ((ImageComp) this.owner);
            owner.sprite.setOrigin(owner.sprite.getOriginX(), Float.valueOf(value));
        }
    };
    private static EditableValue scaleX = new EditableValue() {
        @Override
        public String get() {
            ImageComp owner = ((ImageComp) this.owner);
            return ""+owner.sprite.getScaleX();
        }

        @Override
        public void set(String value) {
            ImageComp owner = ((ImageComp) this.owner);
            owner.sprite.setScale(Float.valueOf(value), owner.sprite.getScaleY());
        }
    };
    private static EditableValue scaleY = new EditableValue() {
        @Override
        public String get() {
            ImageComp owner = ((ImageComp) this.owner);
            return ""+owner.sprite.getScaleY();
        }

        @Override
        public void set(String value) {
            ImageComp owner = ((ImageComp) this.owner);
            owner.sprite.setScale(owner.sprite.getScaleX(), Float.valueOf(value));
        }
    };
    private static EditableValue velocityX = new EditableValue() {
        @Override
        public String get() {
            BodyComp owner = ((BodyComp) this.owner);
            return ""+owner.body.getLinearVelocity().x;
        }

        @Override
        public void set(String value) {
            BodyComp owner = ((BodyComp) this.owner);
            owner.body.setLinearVelocity(Float.valueOf(value), owner.body.getLinearVelocity().y);
        }
    };
    private static EditableValue velocityY = new EditableValue() {
        @Override
        public String get() {
            BodyComp owner = ((BodyComp) this.owner);
            return ""+owner.body.getLinearVelocity().y;
        }

        @Override
        public void set(String value) {
            BodyComp owner = ((BodyComp) this.owner);
            owner.body.setLinearVelocity(owner.body.getLinearVelocity().x, Float.valueOf(value));
        }
    };
    private static EditableValue velocityA = new EditableValue() {
        @Override
        public String get() {
            BodyComp owner = ((BodyComp) this.owner);
            return ""+owner.body.getAngularVelocity();
        }

        @Override
        public void set(String value) {
            BodyComp owner = ((BodyComp) this.owner);
            owner.body.setAngularVelocity(Float.valueOf(value));
        }
    };
    private static EditableValue color = new EditableValue() {
        @Override
        public String get() {
            HasColor owner = ((HasColor) this.owner);
            return Utils.colorToString(owner.getColor());
        }

        @Override
        public void set(String value) {
            HasColor owner = ((HasColor) this.owner);
            owner.setColor(Utils.stringToColor(value));
        }
    };
    private static EditableValue radius = new EditableValue() {
        @Override
        public String get() {
            HasRadius owner = ((HasRadius) this.owner);
            return ""+owner.getRadius();
        }

        @Override
        public void set(String value) {
            HasRadius owner = ((HasRadius) this.owner);
            owner.setRadius(Float.valueOf(value));
        }
    };
    private static EditableValue vectorX = new EditableValue() {
        @Override
        public String get() {
            Vector2 owner = ((Vector2) this.owner);
            return ""+owner.x;
        }

        @Override
        public void set(String value) {
            Vector2 owner = ((Vector2) this.owner);
            owner.x = Float.valueOf(value);
        }
    };
    private static EditableValue vectorY = new EditableValue() {
        @Override
        public String get() {
            Vector2 owner = ((Vector2) this.owner);
            return ""+owner.y;
        }

        @Override
        public void set(String value) {
            Vector2 owner = ((Vector2) this.owner);
            owner.y = Float.valueOf(value);
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
