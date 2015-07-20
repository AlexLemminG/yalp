package com.lemming.game.basic;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

/**
 * Created by Alexander on 16.07.2015.
 */
public class Level {
    public World world;
    Array<GObject> gObjects = new Array<GObject>();
    Array<GObject> justAdded = new Array<GObject>();
    public View view;
    public boolean updatePhysics = true;
    public boolean updateObjects = true;
    public Level() {
        this.world = new World(new Vector2(0, 0), true);
    }

    public void update(float dt){
        if(updatePhysics)
            world.step(dt, 8, 3);
        if(updateObjects)
            for(GObject o : gObjects){
                o.update(dt);
            }
        for(GObject o : justAdded){
            o.onCreate();
            gObjects.add(o);
        }
        justAdded.clear();
    }

    public void render(View view){
        view.render();
    }

    public void add(GObject gObject) {
        justAdded.add(gObject);
    }

    public GObject getGObjectAt(float x, float y) {
        float dist = Float.MAX_VALUE;
        float currentDist = 0;
        GObject result = null;
        for(GObject gObject : gObjects){
            currentDist = gObject.pos.dst2(x, y);
            if(currentDist < dist){
                dist = currentDist;
                result = gObject;
            }
        }
        return result;
    }
}
