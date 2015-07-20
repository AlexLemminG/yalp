package com.lemming.game.comps;

import com.badlogic.gdx.graphics.Color;
import com.lemming.game.basic.GObject;
import com.lemming.game.basic.View;
import com.lemming.game.math.Path;

/**
 * Created by Alexander on 20.07.2015.
 */
public class ClampToPathComp extends Comp{

    private Path path;

    public ClampToPathComp(GObject gObject, Path path) {
        super(gObject);
        this.path = path;
    }

    @Override
    public void render(View view) {
        super.render(view);
        if(view.getCurrentRenderer() == View.Renderers.SHAPE_RENDERER){
            view.getShapeRenderer().setColor(Color.WHITE);
            view.getShapeRenderer().polyline(path.toVertices());
        }
    }

    @Override
    public void update(float dt) {
        super.update(dt);
        owner.setPos(path.valueAt(path.projectXT(owner.pos.x)));
    }

    @Override
    public String toString() {
        return "ClampToPath Component";
    }
}
