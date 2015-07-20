package com.lemming.game.editor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.lemming.game.basic.Assets;
import com.lemming.game.basic.GObject;
import com.lemming.game.comps.ImageComp;
import com.lemming.game.gObjects.SimpleGObject;
import com.lemming.game.trash.AABB;
import com.lemming.game.trash.SomeScreen;
import com.lemming.game.ui.ComponentsList;
import com.lemming.game.ui.ParameterEditor;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Alexander on 16.07.2015.
 */
public class EditorScreen extends SplitScreen{
    GObject selectedGObject;
    BasicScreen bs;
    Vector2 selectedDelta = new Vector2();
    Stage stage;
    Table leftTable;
    Skin skin = Assets.DEFAULT_SKIN;

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        stage.act(delta);
        stage.draw();
        System.out.println(Gdx.input.getX());
        Vector3 v = stage.getCamera().unproject(new Vector3(Gdx.input.getX(),
                        Gdx.input.getY(), 0));
        v.x = v.x - (int)(v.x/Gdx.graphics.getWidth()) * Gdx.graphics.getWidth();
        v.x = v.x < 0 ? v.x + Gdx.graphics.getWidth() : v.x;
//        if(Gdx.input.getX() <= 0){
//            Gdx.input.setCursorPosition(Gdx.graphics.getWidth()-1, Gdx.input.getY());
//        }
    }

    public EditorScreen() {
        bs = new SomeScreen();
        bs.view.getCamera().zoom = 0.01f;
        add(bs, new AABB(0, 0, 1, 1));
        new SimpleGObject(bs.level);
//        new SimpleGObject(bs.level);
//        new SimpleGObject(bs.level);
        stage = new Stage(new ScreenViewport());
        stage.setDebugAll(true);
        leftTable = new Table(skin);
        leftTable.setFillParent(true);
        leftTable.align(Align.topLeft);
        view.drawBackground = false;

        stage.addActor(leftTable);
        Gdx.input.setInputProcessor(new InputMultiplexer(stage, new EditorInputListener()));
    }

    private class EditorInputListener extends InputAdapter{
        Vector3 temp = new Vector3();
        Set<Integer> buttonsDown = new HashSet<Integer>();

        private Table generatePropertiesTable(final GObject object){
            Table table = new Table(skin);
            table.add(new ParameterEditor(object, "x", null).left());
            table.row();
            table.add(new ParameterEditor(object, "y", null));
            table.row();
            table.add(new ParameterEditor(object, "a", null));
            table.setWidth(150);
            return table.align(Align.topLeft);
        }

        private Table generateComponentsTable(final GObject object){
            Table t = new Table(skin);
            t.add(new ComponentsList(object));
            return t;
        }

        public boolean isButtonDown(int button){
            return buttonsDown.contains(button);
        }

        public void setSelected(GObject newSelected, Vector3 mouseWorldXY){
            if(selectedGObject == newSelected)return;
            if(selectedGObject != null){
                ((ImageComp) selectedGObject.getComponent(ImageComp.class)).boundColor = ImageComp.DEFAULT_BOUND_COLOR;
                leftTable.clearChildren();
                selectedGObject.setProperty("selected", Boolean.FALSE);
            }
            selectedGObject = newSelected;
            if(selectedGObject != null){
                ((ImageComp) selectedGObject.getComponent(ImageComp.class)).boundColor = ImageComp.SELECTED_BOUND_COLOR;
                if(mouseWorldXY != null){
                    selectedDelta.set(selectedGObject.pos.x - mouseWorldXY.x, selectedGObject.pos.y - mouseWorldXY.y);
                }
                leftTable.add(generatePropertiesTable(selectedGObject)).align(Align.left);
                leftTable.row();
                leftTable.add(generateComponentsTable(selectedGObject));
                selectedGObject.setProperty("selected", Boolean.TRUE);
            }
            Gdx.app.log("Editor", newSelected + " selected");
        }

        @Override
        public boolean touchUp(int screenX, int screenY, int pointer, int button) {
            buttonsDown.remove(button);

            return super.touchUp(screenX, screenY, pointer, button);
        }

        @Override
        public boolean touchDown(int screenX, int screenY, int pointer, int button) {
            buttonsDown.add(button);
            temp = screenToWorld(screenX, screenY);

            GObject gObject = bs.level.getGObjectAt(temp.x, temp.y);
            if(button == Input.Buttons.LEFT) {
                setSelected(gObject, temp);
            }
            return super.touchDown(screenX, screenY, pointer, button);
        }

        @Override
        public boolean touchDragged(int screenX, int screenY, int pointer) {
            temp = screenToWorld(screenX, screenY);
            if(selectedGObject != null && isButtonDown(Input.Buttons.LEFT))
                selectedGObject.setPos(temp.x + selectedDelta.x, temp.y + selectedDelta.y);

            return super.touchDragged(screenX, screenY, pointer);
        }

        @Override
        public boolean scrolled(int amount) {
            for(int i = 0; i < amount; i++) {
                bs.view.getCamera().zoom /= 1.2f;
            }
            for(int i = 0; i > amount; i--) {
                bs.view.getCamera().zoom *= 1.2f;
            }

            return super.scrolled(amount);
        }

        @Override
        public boolean keyDown(int keycode) {
            if(keycode == Input.Keys.P){
                if(!bs.paused)
                    bs.pause();
                else
                    bs.resume();
            }
            if(keycode == Input.Keys.N){
                new SimpleGObject(bs.level);
            }
            return super.keyDown(keycode);
        }

        private Vector3 screenToWorld(int screenX, int screenY){
            float newScreenX = screenX - viewports.get(0).x;
            float newScreenY = screenY - viewports.get(0).y;
            newScreenX /= 1f * bs.view.getCamera().viewportWidth / view.getCamera().viewportWidth;
            newScreenY /= 1f * bs.view.getCamera().viewportHeight / view.getCamera().viewportHeight;
            temp.set(newScreenX, newScreenY, 0);
            return bs.view.getCamera().unproject(temp);
        }
    }
}
