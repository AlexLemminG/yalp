package com.lemming.game.comps;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.lemming.game.basic.GObject;
import com.lemming.game.ui.EditableValue;

/**
 * Created by Alexander on 25.07.2015.
 */
public class UnitComp extends Comp{
    EditableValue.Vector2Value origin = new EditableValue.Vector2Value(new Vector2(), "origin"){
        @Override
        public void set(Vector2 value) {
            super.set(value);
            changed = true;
        }
    };
    EditableValue.Vector2Value size = new EditableValue.Vector2Value(new Vector2(), "size"){
        @Override
        public void set(Vector2 value) {
            super.set(value);
            changed = true;
        }
    };
    boolean changed = true;
    Fixture fixture;
    FixtureDef fixtureDef;
    BodyComp bodyComp;
    public UnitComp(GObject gObject) {
        super(gObject);
        new PlayerControllComp(owner);
    }

    @Override
    public void update(float dt) {
        super.update(dt);
        if(changed)
            updateFixture();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        this.bodyComp = owner.getComponent(BodyComp.class);
        fixtureDef = new FixtureDef();
        fixtureDef.isSensor = true;
        fixtureDef.shape = new PolygonShape();
    }
    private void updateFixture(){
        if(fixture != null)
            bodyComp.body.destroyFixture(fixture);
        ((PolygonShape)fixtureDef.shape).setAsBox(size.get().x / 2, size.get().y / 2, origin.get(), 0);
        fixture = bodyComp.body.createFixture(fixtureDef);
        fixture.setUserData(this);
        changed = false;
    }

    @Override
    public Array<EditableValue> getValues() {
        Array<EditableValue> values = super.getValues();
        values.add(origin);
        values.add(size);
        return values;
    }

    private int touches = 0;
    public void touched(Contact c){
        contacts.add(c);
        touches++;
        System.out.println(touches);
    }
    public void untouched(Contact c){
        contacts.removeValue(c, true);
        touches--;
        System.out.println(touches);
    }

    Vector2 temp = new Vector2();
    public void jump() {
        if(touches > 0){
            System.out.println("jumpED");
            temp.set(bodyComp.body.getLinearVelocity());
            bodyComp.body.setLinearVelocity(temp.add(0, 5));
        }
    }

    public void runRight() {
        System.out.println("right");
        temp.set(bodyComp.body.getLinearVelocity());
        if(onGround())
            bodyComp.body.setLinearVelocity(temp.add(5,0));
    }

    Array<Contact> contacts = new Array<Contact>();
    private boolean onGround() {
        return touches > 0;
    }

    public void runLeft() {
        System.out.println("left");
        temp.set(bodyComp.body.getLinearVelocity());
        if(onGround()) {
//            bodyComp.body.setLinearVelocity(temp.add(-5, 0));
            float x = 10f / contacts.size;
            for(Contact c : contacts){
                Vector2[] points = c.getWorldManifold().getPoints();
                Body b = c.getFixtureB().getUserData() == this ? c.getFixtureA().getBody() : c.getFixtureB().getBody();
                for(int i = 0; i < points.length; i++){
                    b.applyForce(temp.set(x/points.length, 0), points[i], true);
                    bodyComp.body.applyForce(temp.set(-x/points.length, 0), points[i], true);
                }
            }
        }
    }
}
