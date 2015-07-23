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
    Color color;
    @Override
    public void update(float dt) {
        super.update(dt);
        if(!attachedToBody)
            pointLight.setPosition(owner.pos);
        //важно т к изменяем цвет не вызывая метод
        setColor(getColor());
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
        if(pointLight != null)
            pointLight.setActive(enabled);
    }

    boolean attachedToBody = false;
    @Override
    public void onCreate() {
        super.onCreate();
        pointLight = new PointLight(owner.level.view.getRayHandler(), 1024, new Color(1,1,1,1), 5, owner.pos.x, owner.pos.y);
        pointLight.setActive(isEnabled());
        BodyComp bodyComp = owner.getComponent(BodyComp.class);
        if (bodyComp != null){
            attachedToBody = true;
            pointLight.attachToBody(bodyComp.body);
        }
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
