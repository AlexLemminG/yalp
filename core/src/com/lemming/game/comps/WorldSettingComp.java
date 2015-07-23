package com.lemming.game.comps;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.lemming.game.basic.GObject;
import com.lemming.game.ui.EditableValue;

/**
 * Created by Alexander on 23.07.2015.
 */
public class WorldSettingComp extends Comp{
    World world;
    EditableValue.Vector2Value gravity = new EditableValue.Vector2Value(new Vector2(0, 0));
    @Override
    public void onCreate() {
        super.onCreate();
        world = owner.level.world;
    }

    @Override
    public void update(float dt) {
        super.update(dt);
        world.setGravity(gravity.get());
    }

    public WorldSettingComp(GObject gObject) {
        super(gObject);
    }

    @Override
    public String toString() {
        return "WorldSettings";
    }

    @Override
    public Array<EditableValue> getValues() {
        Array<EditableValue> values = super.getValues();
        values.add(gravity);

        return values;
    }
}
