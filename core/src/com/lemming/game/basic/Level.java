package com.lemming.game.basic;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlWriter;
import com.lemming.game.gObjects.WorldGObject;
import com.lemming.game.trash.PhysicsContactListener;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by Alexander on 16.07.2015.
 */
public class Level {
    public World world;
    Array<GObject> gObjects;
    Array<GObject> justAdded;
    public View view;
    public boolean updatePhysics = true;
    public boolean updateObjects = true;
    public Level() {
        clear();
        new WorldGObject(this);
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

    public Array<GObject> getgObjects() {
        return gObjects;
    }

    public void save(){
        try {
            XmlWriter w = new XmlWriter(new PrintWriter(new FileHandle("level.xml").file()));
            w.element("level");
            w.element("GObjects");
            for(GObject o : getgObjects()){
                o.save(w);
            }
            w.pop();
            w.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void load(){
        try {
            clear();
            XmlReader r = new XmlReader();
            XmlReader.Element element = null;
            element = r.parse(new FileHandle("level.xml"));
            Array<XmlReader.Element> gObjects = element.getChildByName("GObjects").getChildrenByName("GObject");
            for(XmlReader.Element e : gObjects){
                GObject.load(e, this);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void clear(){
        if(world != null)
            world.dispose();
        world = new World(new Vector2(0, 0), true);
        world.setContactListener(new PhysicsContactListener());
        if(gObjects != null){
            for(GObject o : gObjects){
                o.dispose();
            }
        }
        gObjects = new Array<GObject>();
        justAdded = new Array<GObject>();
    }

    public GObject getByName(String name){
        GObject result = null;
        for(int i = 0; i < gObjects.size; i++){
            if(gObjects.get(i).toString().equals(name)){
                result = gObjects.get(i);
                break;
            }
        }
        return result;
    }
}
