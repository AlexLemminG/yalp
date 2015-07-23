package com.lemming.game.comps;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.lemming.game.basic.GObject;
import com.lemming.game.ui.EditableValue;

/**
 * Created by Alexander on 21.07.2015.
 */
public class FollowSinComp extends Comp{
    EditableValue.FloatValue a = new EditableValue.FloatValue();
    EditableValue.FloatValue b = new EditableValue.FloatValue();

    public FollowSinComp(GObject gObject) {
        super(gObject);
    }
    float t = 0;

    @Override
    public Array<EditableValue> getValues() {
        Array<EditableValue> result = super.getValues();
        a.name = "a";
        b.name = "b";
        result.add(a);
        result.add(b);
        return result;
    }

    @Override
    public void update(float dt) {
        super.update(dt);
        t += dt;
        BodyComp bodyComp = owner.getComponent(BodyComp.class);
        if(bodyComp != null){
            bodyComp.body.setLinearVelocity(0, b.get() * MathUtils.cos(b.get() * t) * a.get());
        }else
            owner.setPos(owner.pos.x, owner.pos.y + dt * b.get() * MathUtils.cos(b.get() * t) * a.get());
    }

    @Override
    public String toString() {
        return "FollowSin Component";
    }
}
