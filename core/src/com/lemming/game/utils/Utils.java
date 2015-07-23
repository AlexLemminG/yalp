package com.lemming.game.utils;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.lemming.game.basic.View;

/**
 * Created by Alexander on 18.07.2015.
 */
public class Utils {
    public static Color stringToColor(String string){
        Color c = new Color();
        if(string.length() < 8)
            throw new IllegalArgumentException();
//        int rgba = Integer.valueOf(string.substring(0, 8), 16);
        int r = Integer.valueOf(string.substring(0, 2), 16);
        int g = Integer.valueOf(string.substring(2, 4), 16);
        int b = Integer.valueOf(string.substring(4, 6), 16);
        int a = Integer.valueOf(string.substring(6, 8), 16);
        c.set(r / 255f, g / 255f, b / 255f, a / 255f);
//        c.set(rgba);
        return c;
    }
    public static String colorToString(Color color){
        return color.toString();
    }

    public static void drawPoint(Vector2 point, View view, Color color){
        ShapeRenderer shapeRenderer = view.getShapeRenderer();
        shapeRenderer.set(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(color);
        float w, h;
        w = h = 5 * view.getCamera().zoom;
        shapeRenderer.rect(point.x - w / 2, point.y - h / 2, w, h);
    }

    public static float[] arrayToVertices(Array<Vector2> points){
        float[] result = new float[points.size * 2];
        for (int i = 0; i < points.size; i++) {
            result[i * 2    ] = points.get(i).x;
            result[i * 2 + 1] = points.get(i).y;
        }
        return result;
    }
}
