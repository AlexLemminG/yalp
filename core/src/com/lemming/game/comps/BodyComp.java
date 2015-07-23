package com.lemming.game.comps;

import com.badlogic.gdx.math.EarClippingTriangulator;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Array;
import com.lemming.game.basic.GObject;
import com.lemming.game.ui.EditableValue;
import com.lemming.game.utils.Utils;

/**
 * Created by Alexander on 16.07.2015.
 */
public class BodyComp extends Comp{
    public BodyComp(GObject gObject) {
        super(gObject);
    }

    public Body body;
    public EditableValue.BoolValue staticBody = new EditableValue.BoolValue(false){
        @Override
        public void set(Boolean value) {
            super.set(value);
            body.setType(value ? BodyDef.BodyType.KinematicBody : BodyDef.BodyType.DynamicBody);
        }
    };

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
        staticBody.name = "staticBody";
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
        result.add(staticBody);
        result.add(new EditableValue() {
            @Override
            public Object get() {
                return null;
            }

            @Override
            public void set(Object value) {

            }

            @Override
            public String getStr() {
                return null;
            }

            @Override
            public void setStr(String value) {

            }

            @Override
            public void addToTable(Table componentEditor) {
                Button button = new Button(componentEditor.getSkin());
                if(owner.getComponent(PolyLineComp.class) != null)
                    button.addListener(new ChangeListener() {
                        @Override
                        public void changed(ChangeEvent event, Actor actor) {
                            event.cancel();
                            Array<Fixture> fixtureList = body.getFixtureList();
                            for(int i = fixtureList.size-1; i>=0; i--){
                                body.destroyFixture(fixtureList.get(i));
                            }
                            float[] vertices = Utils.arrayToVertices(owner.getComponent(PolyLineComp.class).localPoints);
                            short[] s = new EarClippingTriangulator().computeTriangles(vertices).toArray();
                            for(int i = 0; i < s.length / 3; i++){
                                PolygonShape shape = new PolygonShape();
                                float[] v = new float[6];
                                v[0] = vertices[s[i * 3]*2];
                                v[1] = vertices[s[i * 3]*2+1];
                                v[2] = vertices[s[i * 3+1]*2];
                                v[3] = vertices[s[i * 3+1]*2+1];
                                v[4] = vertices[s[i * 3+2]*2];
                                v[5] = vertices[s[i * 3+2]*2+1];
                                shape.set(v);
                                PolygonShape p = new PolygonShape();
                                p.set(v);
                                FixtureDef fDef = new FixtureDef();
                                fDef.shape = p;
                                body.createFixture(fDef);
                            }
                        }
                    });
                button.add("fromPolygon");

                componentEditor.add(button);
            }
        });
        return result;
    }

    public String toString(){
        return "Body Component";
    }

}
