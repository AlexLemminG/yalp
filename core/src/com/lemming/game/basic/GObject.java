package com.lemming.game.basic;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.lemming.game.comps.BodyComp;
import com.lemming.game.comps.Comp;
import com.lemming.game.comps.PolyLineComp;

import java.util.HashMap;

/**
 * Created by Alexander on 16.07.2015.
 */
public class GObject {
    private float crossHalfLength = 50;
    public Array<Comp> components = new Array<Comp>();
    private Array<Comp> unshowableComponents = new Array<Comp>();
    public HashMap<String, Object> properties = new HashMap<String, Object>();
    public Vector2 pos = new Vector2();
    public float a;
    public Level level;

    public GObject(Level level) {
        this.level = level;
        level.add(this);
    }

    public void onCreate(){
        addCross();
        for(Comp c : components){
            if(c.isEnabled())
                c.onCreate();
        }
        for(Comp c : unshowableComponents){
            if(c.isEnabled())
                c.onCreate();
        }
    }

    public void update(float dt){
        for(Comp c : components){
            if(c.isEnabled())
                c.update(dt);
        }
        for(Comp c : unshowableComponents){
            if(c.isEnabled())
                c.update(dt);
        }
    }

    public void render(View view){
        for(Comp c : components){
            if(c.isEnabled())
                c.render(view);
        }
        for(Comp c : unshowableComponents){
            if(c.isEnabled())
                c.render(view);
        }
    }

    public Comp getComponent(Class c){
        for(int i = 0; i < components.size; i++){
            Comp comp = components.get(i);
            if(comp.getClass().equals(c))
                return comp;
        }
        return null;
    }

    private void setTransform(float x, float y, float a){
        BodyComp b = ((BodyComp) getComponent(BodyComp.class));
        if(b != null){
            b.body.setTransform(x, y, a);
            b.body.setAwake(true);
        }
        pos.set(x, y);
        this.a = a;
    }

    public void setPos(float x, float y){
        setTransform(x, y, a);
    }

    public void setPos(Vector2 newPos){
        setPos(newPos.x, newPos.y);
    }

    public void setA(float a) {
        setTransform(pos.x, pos.y, a);
    }

    public Object getProperty(String key) {
        Object o = properties.get(key);
        return o != null ? o : false;
    }

    public void setProperty(String key, Object value){
        properties.put(key, value);
    }

    private void addCross(){
        PolyLineComp comp = new PolyLineComp(this){
            @Override
            public String toString() {
                return "Selection Cross";
            }
        };
        comp.addPoint(
                new Vector2(-crossHalfLength, 0),
                new Vector2(crossHalfLength, 0),
                new Vector2(crossHalfLength*0.9f, crossHalfLength * 0.1f),
                new Vector2(crossHalfLength*0.9f, -crossHalfLength * 0.1f),
                new Vector2(crossHalfLength, 0),
                new Vector2(0, 0),
                new Vector2(0, -crossHalfLength),
                new Vector2(0, crossHalfLength),
                new Vector2(crossHalfLength * 0.1f, crossHalfLength*0.9f),
                new Vector2(-crossHalfLength * 0.1f, crossHalfLength*0.9f),
                new Vector2(0, crossHalfLength));
        components.removeValue(comp, true);
        comp.unresizable = true;
        comp.drawPoints = false;
        unshowableComponents.add(comp);
    }
}
