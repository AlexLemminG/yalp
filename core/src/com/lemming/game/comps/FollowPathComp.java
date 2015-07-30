package com.lemming.game.comps;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.lemming.game.basic.GObject;
import com.lemming.game.basic.View;
import com.lemming.game.math.Path;
import com.lemming.game.ui.EditableValue;

/**
 * Created by Alexander on 23.07.2015.
 */
public class FollowPathComp extends Comp {
    Path path;
    EditableValue.FloatValue speed = new EditableValue.FloatValue(1f, "speed");
    public FollowPathComp(GObject gObject, Path path) {
        super(gObject);
        this.path = path;
    }

    float t = 0;

    @Override
    public void render(View view) {
        super.render(view);
    }

    @Override
    public String toString() {
        return "FollowPath Component";
    }

    @Override
    public void update(float dt) {
        super.update(dt);
        t+=dt*speed.get();
        t = t % 1f;
        Vector2 newPos = path.valueAt(t);
        if(newPos != null)
            owner.setPos(newPos);
    }

    @Override
    public Array<EditableValue> getValues() {
        Array<EditableValue> values = super.getValues();
        values.add(speed);
        values.addAll(path.getValues());
        return values;
    }
}
