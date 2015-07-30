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

/**
 * Created by Alexander on 16.07.2015.
 */
public class BodyComp extends Comp{
//    public static final String name = "BodyComponent";
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
        PolygonShape polygonShape = new PolygonShape();
        polygonShape.setAsBox(0.5f, .5f);
        body.createFixture(polygonShape, 1);
        staticBody.name = "staticBody";
        body.setActive(isEnabled());
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        if(body != null)
            body.setActive(enabled);
    }

    @Override
    public Array<EditableValue> getValues() {
        Array<EditableValue> result = super.getValues();
        result.add(EditableValue.getValue(this, "velocityX"));
        result.add(EditableValue.getValue(this, "velocityY"));
        result.add(EditableValue.getValue(this, "velocityA"));
        result.add(staticBody);
        EditableValue.BoolValue fromPolygon = new EditableValue.BoolValue() {
            @Override
            public void addToTable(Table componentEditor) {
                Button button = new Button(componentEditor.getSkin());
                if (owner.getComponent(PolyLineComp.class) != null)
                    button.addListener(new ChangeListener() {
                        @Override
                        public void changed(ChangeEvent event, Actor actor) {
                            event.cancel();
                            updateFixture();
                        }
                    });
                button.add("fromPolygon");

                componentEditor.add(button);
            }
        };
        fromPolygon.name = "fixtureFromPolygon";
        result.add(fromPolygon); //кнопка чтоб сделать из полилайна
        return result;
    }

    public String toString(){
        return "Body Component";
    }

    private void updateFixture(){
        Array<Fixture> fixtureList = body.getFixtureList();
        for(int i = fixtureList.size-1; i>=0; i--){
            body.destroyFixture(fixtureList.get(i));
        }
        float[] vertices = owner.getComponent(PolyLineComp.class).path.getVertices();
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

}
