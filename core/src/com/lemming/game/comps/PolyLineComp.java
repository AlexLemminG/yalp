package com.lemming.game.comps;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.lemming.game.basic.GObject;
import com.lemming.game.basic.View;
import com.lemming.game.ui.EditableValue;
import com.lemming.game.utils.Utils;

import static com.badlogic.gdx.math.MathUtils.cos;
import static com.badlogic.gdx.math.MathUtils.sin;

/**
 * Created by Alexander on 17.07.2015.
 */
public class PolyLineComp extends Comp{
    private static final Color SELECTED_COLOR = Color.WHITE;
    private static final Color DEFAULT_COLOR = Color.CLEAR;
    Color color = DEFAULT_COLOR;
    public boolean unresizable = false;
    public boolean drawPoints = true;
    Array<Vector2> localPoints = new Array<Vector2>();
    float[] vertices = new float[0];
    @Override
    public void update(float dt) {
        super.update(dt);

    }

    @Override
    public void render(View view) {
        super.render(view);
        float lx, ly;
        for(int i = 0; i < localPoints.size; i++){
            lx = localPoints.get(i).x;
            ly = localPoints.get(i).y;
            if(unresizable){
                lx *= view.getCamera().zoom;
                ly *= view.getCamera().zoom;
            }

            vertices[i*2] =   owner.pos.x +   lx * cos(owner.a) - ly * sin(owner.a);
            vertices[i*2+1] = owner.pos.y +   lx * sin(owner.a) + ly * cos(owner.a);
        }

        if(view.getCurrentRenderer() == View.Renderers.SHAPE_RENDERER) {
            if (localPoints.size >= 2) {
                color = ((Boolean) owner.getProperty("selected")) ? SELECTED_COLOR : DEFAULT_COLOR;
                if (color.equals(Color.CLEAR)) return;
                view.getShapeRenderer().setColor(color);
                view.getShapeRenderer().polyline(vertices);
            }
            if(drawPoints) {
                Vector2 temp = new Vector2();
                for (int i = 0; i < localPoints.size; i++) {
                    Utils.drawPoint(temp.set(vertices[i * 2], vertices[i * 2 + 1]), view, color);
                }
            }
        }
    }

    @Override
    public Array<EditableValue> getValues() {
        Array<EditableValue> result = super.getValues();
        result.add(new EditableValue() {
            @Override
            public String get() {
                return ""+localPoints.size;
            }

            @Override
            public void set(String value) {
                int n = Integer.valueOf(value);
                while(localPoints.size < n)
                    addPoint(new Vector2());
                localPoints.size = n;
            }
        });
        result.get(0).updateEditor = true;
        for(int i = 0; i < localPoints.size; i++){
            result.add(EditableValue.getValue(localPoints.get(i), "vectorX"));
            result.add(EditableValue.getValue(localPoints.get(i), "vectorY"));
        }
        return result;
    }

    public void addPoint(Vector2... points){
        for(Vector2 point : points)
            localPoints.add(point);
        vertices = new float[localPoints.size * 2];
    }

    @Override
    public String toString() {
        return "Polyline Component";
    }

    public PolyLineComp(GObject gObject) {
        super(gObject);
    }
}
