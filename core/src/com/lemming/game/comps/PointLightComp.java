package com.lemming.game.comps;

import box2dLight.PointLight;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.Array;
import com.lemming.game.basic.GObject;
import com.lemming.game.ui.EditableValue;

/**
 * Created by Alexander on 18.07.2015.
 */
public class PointLightComp extends Comp implements HasColor, HasRadius{
    PointLight pointLight;

    @Override
    public void update(float dt) {
        super.update(dt);
        pointLight.setPosition(owner.pos);
    }

    @Override
    public Array<EditableValue> getValues() {
        Array<EditableValue> result = super.getValues();
        result.add(EditableValue.getValue(this,"color"));
        result.add(EditableValue.getValue(this,"radius"));
        return result;
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        pointLight.setActive(enabled);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        this.pointLight = new PointLight(owner.level.view.getRayHandler(), 1024, new Color(), 100, owner.pos.x, owner.pos.y);
    }

    public PointLightComp(GObject gObject) {
        super(gObject);
    }

    @Override
    public void setColor(Color color) {
        pointLight.setColor(color);
    }


    @Override
    public Color getColor() {
        return pointLight.getColor();
    }

    @Override
    public String toString() {
        return "PointLight Component";
    }

    @Override
    public void setRadius(float radius) {
        pointLight.setDistance(radius);
    }

    @Override
    public float getRadius() {
        return pointLight.getDistance();
    }
}
