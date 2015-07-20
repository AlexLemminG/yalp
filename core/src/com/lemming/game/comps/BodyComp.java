package com.lemming.game.comps;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.utils.Array;
import com.lemming.game.basic.GObject;
import com.lemming.game.ui.EditableValue;

/**
 * Created by Alexander on 16.07.2015.
 */
public class BodyComp extends Comp{
    public BodyComp(GObject gObject) {
        super(gObject);
    }

    public Body body;

    @Override
    public void update(float dt) {
        super.update(dt);
        owner.pos.set(body.getPosition());
        owner.a = body.getAngle();
    }

    public void onCreate(){
        BodyDef bDef = new BodyDef();
        bDef.type = BodyDef.BodyType.DynamicBody;
        CircleShape circleShape = new CircleShape();
        circleShape.setRadius(1);
        body = owner.level.world.createBody(bDef);
        body.createFixture(circleShape, 1);
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        body.setActive(enabled);
    }

    @Override
    public Array<EditableValue> getValues() {
        Array<EditableValue> result = super.getValues();
        result.add(EditableValue.getValue(this, "velocityX"));
        result.add(EditableValue.getValue(this, "velocityY"));
        result.add(EditableValue.getValue(this, "velocityA"));
        return result;
    }

    public String toString(){
        return "Body Component";
    }

}
