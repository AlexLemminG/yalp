package com.lemming.game.gObjects;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.lemming.game.basic.GObject;
import com.lemming.game.basic.Level;
import com.lemming.game.comps.*;
import com.lemming.game.math.Path;

/**
 * Created by Alexander on 16.07.2015.
 */
public class SimpleGObject extends GObject{
    public SimpleGObject(Level level) {
        super(level);
        BodyComp value = new BodyComp(this);
//        value.body.setAngularVelocity(((float) Math.random()));
//        value.body.setLinearVelocity(((float) Math.random()), ((float) Math.random()));
        ImageComp imageComp = new ImageComp(this);
        imageComp.sprite.setScale(0.01f);
        imageComp.setEnabled(false);
        new PointLightComp(this).setEnabled(false);
        new PolyLineComp(this).setEnabled(true);
        new FollowSinComp(this).setEnabled(false);
        new ClampToPathComp(this, new Path(new Array<Vector2>(new Vector2[]{new Vector2(-10, -10), new Vector2(10, 10), new Vector2(20, 0)}))).setEnabled(false);
        new WorldSettingComp(this).setEnabled(false);
    }
}
