package com.lemming.game.editor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.lemming.game.basic.Level;
import com.lemming.game.basic.View;

public class BasicScreen extends ScreenAdapter {
    Array<Disposable> disposables = new Array<Disposable>();
    public View view;
    protected Level level;

    public void setViewport(float x, float y, float w, float h){
        view.setViewport(x, y ,w, h);
    }

    public boolean paused = false;

    @Override
    public void resume() {
        super.resume();
        paused = false;
        Gdx.app.log("State", toString() + " resumed");
    }

    @Override
    public void pause() {
        super.pause();
        paused = true;
        Gdx.app.log("State", toString() + " paused");
    }

    @Override
    public void dispose() {
        for(Disposable d : disposables){
            d.dispose();
        }
    }

	public BasicScreen() {
        level = new Level();
        view = new View(level);
	}

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        view.resize(width, height);
        Gdx.app.log("State", toString() + " resized");
    }

    @Override
	public void render (float dt) {
        dt = dt > 45 ? 60 : 30;
        dt = 1f/dt;
        if(!paused)
            level.update(dt);
        level.render(view);

	}
}
