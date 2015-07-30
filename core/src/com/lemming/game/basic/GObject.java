package com.lemming.game.basic;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlWriter;
import com.lemming.game.comps.BodyComp;
import com.lemming.game.comps.Comp;
import com.lemming.game.comps.PolyLineComp;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Alexander on 16.07.2015.
 */
public class GObject {
    static int nextId = 0;
    public int id = nextId++;

    private float crossHalfLength = 50;
    public Array<Comp> components = new Array<Comp>();
    private Array<Comp> defaultComponents = new Array<Comp>();
    public HashMap<String, String> properties = new HashMap<String, String>();
    public Vector2 pos = new Vector2();
    public float a;
    public Level level;

    public GObject(Level level) {
        this.level = level;
        level.add(this);
    }

    private boolean created = false;
    public void onCreate(){
        if(!created) {
            addCross();
            for (Comp c : components) {
                c.onCreate();
            }
            for (Comp c : defaultComponents) {
                c.onCreate();
            }
            created = true;
        }
    }

    public void update(float dt){
        for(Comp c : components){
            if(c.isEnabled())
                c.update(dt);
        }
        for(Comp c : defaultComponents){
            if(c.isEnabled())
                c.update(dt);
        }
    }

    public void render(View view){
        for(Comp c : components){
            if(c.isEnabled())
                c.render(view);
        }
        for(Comp c : defaultComponents){
            if(c.isEnabled())
                c.render(view);
        }
    }

    public <T extends Comp> T getComponent(Class<T> c){
        for(int i = 0; i < components.size; i++){
            Comp comp = components.get(i);
            if(comp.getClass().equals(c))
                return ((T) comp);
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

    public String getProperty(String key) {
        String o = properties.get(key);
        return o != null ? o : "False";
    }

    public void setProperty(String key, String value){
        properties.put(key, value);
    }

    @Override
    public String toString() {
        return id+"";
    }

    private void addCross(){
        PolyLineComp comp = new PolyLineComp(this){
            @Override
            public String toString() {
                return "SelectionCross";
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
        comp.circled.set(false);
        defaultComponents.add(comp);
    }

    public void save(XmlWriter w) throws IOException {
        w.element("GObject");
        w.attribute("id", id);
        w.element("x", pos.x);
        w.element("y", pos.y);
        w.element("a", a);

        w.element("components");
        for(Comp c : components) c.save(w);
        w.pop();
//        w.element("defaultComponents");
//        for(Comp c : defaultComponents) c.save(w);
//        w.pop();
        w.element("properties");
        for(Map.Entry<String, String> e : properties.entrySet()) w.element(e.getKey(), e.getValue());
        w.pop();

        w.pop();
    }

    public static void load(XmlReader.Element e, Level level){
        int id = Integer.valueOf(e.getAttribute("id"));
        float x = Float.valueOf(e.get("x"));
        float y = Float.valueOf(e.get("y"));
        float a = Float.valueOf(e.get("a"));
        GObject result = new GObject(level);
//        result.id = nextId++;
        {
            XmlReader.Element comp = e.getChildByName("components");
            for (int i = 0; i < comp.getChildCount(); i++) {
                String name = comp.getChild(i).getName();
                Comp.createByName(name, result);
            }
        }
        {
//            XmlReader.Element comp = e.getChildByName("defaultComponents");
//            for (int i = 0; i < comp.getChildCount(); i++) {
//                String name = comp.getChild(i).getName();
//                Comp c = Comp.createByName(name, result);
//                result.components.removeValue(c, true);
//                result.defaultComponents.add(c);
//            }
        }
        {
            XmlReader.Element prop = e.getChildByName("properties");
            for (int i = 0; i < prop.getChildCount(); i++) {
                String key = prop.getChild(i).getName();
                String val = prop.getChild(i).getText();
                result.properties.put(key, val);
            }
        }
        result.onCreate();
        {
            XmlReader.Element comp = e.getChildByName("components");
            for (int i = 0; i < comp.getChildCount(); i++) {
                result.components.get(i).load(comp.getChild(i));
            }
        }
        {
//            XmlReader.Element comp = e.getChildByName("defaultComponents");
//            for (int i = 0; i < comp.getChildCount(); i++) {
//                result.defaultComponents.get(i).load(comp.getChild(i));
//            }
        }
        result.setTransform(x, y, a);

    }

    public void dispose(){
        for(Comp c : components){
            if(c.isEnabled())
                c.dispose();
        }
        for(Comp c : defaultComponents){
            if(c.isEnabled())
                c.dispose();
        }
    }
}
