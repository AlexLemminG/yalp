package com.lemming.game.editor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.lemming.game.trash.AABB;

/**
 * Created by Alexander on 16.07.2015.
 */
public class SplitScreen extends BasicScreen{
    protected Array<AABB> bounds = new Array<AABB>();
    protected Array<Rectangle> viewports = new Array<Rectangle>();
    protected Array<BasicScreen> screens = new Array<BasicScreen>();

    public void add(BasicScreen screen, AABB partOfScreen){
        screens.add(screen);
        bounds.add(partOfScreen);
        viewports.add(new Rectangle());

        screen.view.disableClear = true;
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        for(int i = 0; i < bounds.size; i++) {
            AABB b = bounds.get(i);
            viewports.get(i).set(width * b.x, height * b.y, width * b.w, height * b.h);
        }
    }

    public SplitScreen() {
        super();
        view.disableClear = true;
    }

    @Override
    public void render(float delta) {
        float howWhite = 0.5f;
        Gdx.gl.glClearColor(howWhite, howWhite, howWhite, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        for(int i = 0; i < bounds.size; i++){
            Rectangle b = viewports.get(i);
            screens.get(i).setViewport(b.x, b.y, b.width, b.height);
            screens.get(i).render(delta);
        }

        Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        view.render();
    }
}
