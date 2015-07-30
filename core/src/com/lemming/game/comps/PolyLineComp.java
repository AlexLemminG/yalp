package com.lemming.game.comps;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.lemming.game.basic.GObject;
import com.lemming.game.basic.View;
import com.lemming.game.math.Path;
import com.lemming.game.ui.EditableValue;
import com.lemming.game.utils.Utils;

/**
 * Created by Alexander on 17.07.2015.
 */
public class PolyLineComp extends Comp{
    private static final Color SELECTED_COLOR = Color.WHITE;
    private static final Color DEFAULT_COLOR = Color.CLEAR;
//    public static String name = "PolylineComponent";
    Color color = DEFAULT_COLOR;
    public EditableValue.BoolValue circled = new EditableValue.BoolValue(true);{circled.name = "circled";}
    public boolean unresizable = false;
    public boolean drawPoints = true;
//    Array<Vector2> localPoints = new Array<Vector2>();
//    float[] vertices = new float[0];
    Path path = new Path();
    @Override
    public void update(float dt) {
        super.update(dt);
    }
    Vector2 temp = new Vector2();
    @Override
    public void render(View view) {
        super.render(view);
        float lx, ly;
        Vector2 pos = temp.set(path.getPos());
        float aRad = path.getAngleDeg();
        float scale = path.getScale();
        if(unresizable)
            path.setScale(view.getCamera().zoom);
//        path.setPos(path.getPos().add(owner.pos));
//        path.setAngleDeg(owner.a + angleDeg);


        float[] vertices = path.getVertices();
        int pointsNum = vertices.length / 2;
        for (int i = 0; i < pointsNum; i++) {
            temp.set(vertices[i*2], vertices[i*2+1]).rotateRad(owner.a);
            vertices[i*2] = owner.pos.x + temp.x;
            vertices[i*2 + 1] = owner.pos.y + temp.y;
        }
        path.setDirtyVertices(true);

        if(view.getCurrentRenderer() == View.Renderers.SHAPE_RENDERER) {
            if (vertices.length >= 4) {
                color = owner.getProperty("selected").equals(Boolean.TRUE.toString()) ? SELECTED_COLOR : DEFAULT_COLOR;
                if (color.equals(Color.CLEAR)) return;
                view.getShapeRenderer().setColor(color);
                view.getShapeRenderer().polyline(vertices);
                if(circled.get())
                    view.getShapeRenderer().line(vertices[vertices.length-2],
                                                vertices[vertices.length -1],
                                                vertices[0], vertices[1]);
            }
            if(drawPoints) {
                Vector2 temp = new Vector2();
                for (int i = 0; i < pointsNum; i++) {
                    Utils.drawPoint(temp.set(vertices[i * 2], vertices[i * 2 + 1]), view, color);
                }
            }
        }
        path.setScale(scale);
//        path.setAngleDeg(angleDeg);
//        path.setPos(pos);
    }

    @Override
    public Array<EditableValue> getValues() {
        Array<EditableValue> result = super.getValues();
        result.add(circled);
        result.addAll(path.getValues());
        return result;
    }

    public void addPoint(Vector2... points){
        path.addPoint(points);
    }

    @Override
    public String toString() {
        return "Polyline Component";
    }


    public PolyLineComp(GObject gObject) {
        super(gObject);
    }
}
