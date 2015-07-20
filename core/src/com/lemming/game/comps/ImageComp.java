package com.lemming.game.comps;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.lemming.game.basic.Assets;
import com.lemming.game.basic.GObject;
import com.lemming.game.basic.View;
import com.lemming.game.ui.EditableValue;

import static com.badlogic.gdx.graphics.g2d.SpriteBatch.*;

/**
 * Created by Alexander on 16.07.2015.
 */
public class ImageComp extends Comp{
    public static final Color DEFAULT_BOUND_COLOR = Color.BLACK;
    public static final Color SELECTED_BOUND_COLOR = Color.WHITE;
    public Sprite sprite;
    public Color boundColor = DEFAULT_BOUND_COLOR;

    public ImageComp(GObject gObject) {
        this(gObject, Assets.DEFAULT_TEXTURE);
    }

    public ImageComp(GObject gObject, Texture texture){
        this(gObject, new Sprite(texture));
    }

    public ImageComp(GObject gObject, Sprite sprite) {
        super(gObject);
        this.sprite = sprite;
    }

    @Override
    public void render(View view) {
        sprite.setPosition(owner.pos.x - sprite.getOriginX(), owner.pos.y - sprite.getOriginY());
        sprite.setRotation(owner.a * MathUtils.radDeg);

        if(view.getCurrentRenderer() == View.Renderers.SPRITE_BATCH){
            sprite.draw(view.getSpriteBatch());
        }
        if(view.getCurrentRenderer() == View.Renderers.SHAPE_RENDERER){
            boundColor = ((Boolean) owner.getProperty("selected")) ? SELECTED_BOUND_COLOR : DEFAULT_BOUND_COLOR;
            view.getShapeRenderer().setColor(boundColor);
            float[] vertices = new float[10];
            float[] verticesCombined = sprite.getVertices();
            vertices[0] = verticesCombined[X1];
            vertices[1] = verticesCombined[Y1];
            vertices[2] = verticesCombined[X2];
            vertices[3] = verticesCombined[Y2];
            vertices[4] = verticesCombined[X3];
            vertices[5] = verticesCombined[Y3];
            vertices[6] = verticesCombined[X4];
            vertices[7] = verticesCombined[Y4];
            vertices[8] = verticesCombined[X1];
            vertices[9] = verticesCombined[Y1];
            view.getShapeRenderer().polyline(vertices);
        }
        super.render(view);
    }

    @Override
    public Array<EditableValue> getValues() {
        Array<EditableValue> result = new Array<EditableValue>();

        result.add(EditableValue.getValue(this,"originX"));
        result.add(EditableValue.getValue(this,"originY"));
        result.add(EditableValue.getValue(this,"scaleX"));
        result.add(EditableValue.getValue(this,"scaleY"));
        return result;
    }

    public String toString(){
        return "Image Component";
    }
}
