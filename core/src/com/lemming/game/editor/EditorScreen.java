package com.lemming.game.editor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.lemming.game.basic.Assets;
import com.lemming.game.basic.GObject;
import com.lemming.game.comps.PointLightComp;
import com.lemming.game.comps.grid.GridComp;
import com.lemming.game.comps.grid.GridWalkingComp;
import com.lemming.game.gObjects.SimpleGObject;
import com.lemming.game.trash.AABB;
import com.lemming.game.trash.SomeScreen;
import com.lemming.game.ui.ComponentsList;
import com.lemming.game.ui.EditableValue;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Alexander on 16.07.2015.
 */
public class EditorScreen extends SplitScreen{
    private SimpleGObject player;
    GObject selectedGObject;
    BasicScreen bs;
    Vector2 selectedDelta = new Vector2();
    Stage stage;
    Table leftTable;
    Table rightTable;
    Skin skin = Assets.DEFAULT_SKIN;
    SelectBox<GObject> gObjects;
    TextField tf;

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
//        gObjects.setItems(bs.level.getgObjects());
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
        player = new SimpleGObject(bs.level);
        player.getComponent(PointLightComp.class).setEnabled(true);
        stage = new Stage(new ScreenViewport());
        stage.setDebugAll(true);
        leftTable = new Table(skin);
        leftTable.setFillParent(true);
        leftTable.align(Align.topLeft);

        rightTable = new Table(skin);
        rightTable.setFillParent(true);
        rightTable.align(Align.topRight);
        tf = new TextField("", skin);
        tf.setProgrammaticChangeEvents(false);

        tf.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Array<GObject> all = bs.level.getgObjects();
                Array<GObject> result = new Array<GObject>();
                String searchQuote = tf.getText();
                result.clear();
                for(GObject o : all){
                    if(o.toString().contains(searchQuote)){
                        result.add(o);
                    }
                }
                gObjects.setItems(result);
            }
        });
        gObjects = new SelectBox<GObject>(skin);
        final EditorInputListener editorInputListener = new EditorInputListener();

        gObjects.addListener(new ChangeListener() {
            Vector3 temp = new Vector3();
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                editorInputListener.setSelected(gObjects.getSelected(), temp);
            }
        });
        rightTable.add(tf).right();
        rightTable.row();
        rightTable.add(gObjects).right();

        view.drawBackground = false;

        stage.addActor(leftTable);
        stage.addActor(rightTable);
        Gdx.input.setInputProcessor(new InputMultiplexer(stage, editorInputListener));

    }

    private class EditorInputListener extends InputAdapter{
        Vector3 temp = new Vector3();
        Set<Integer> buttonsDown = new HashSet<Integer>();

        private Table generatePropertiesTable(final GObject object){

            Table table = new Table(skin);
            EditableValue.getValue(object, "x").addToTable(table);
            table.row();
            EditableValue.getValue(object, "y").addToTable(table);
            table.row();
            EditableValue.getValue(object, "a").addToTable(table);
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
            if(selectedGObject == newSelected){
                updateSelectedDelta(mouseWorldXY);
                return;
            }
            if (selectedGObject != null){
                selectedGObject.setProperty("selected", Boolean.FALSE.toString());
                //tf.setCursorPosition(tf.getText().length());
//                ((ImageComp) selectedGObject.getComponent(ImageComp.class)).boundColor = ImageComp.DEFAULT_BOUND_COLOR;
                leftTable.clearChildren();
            }
            selectedGObject = newSelected;
            if(selectedGObject != null){
                EditorScreen.this.gObjects.setSelected(newSelected);
//                        ((ImageComp) selectedGObject.getComponent(ImageComp.class)).boundColor = ImageComp.SELECTED_BOUND_COLOR;
                if(mouseWorldXY != null){
                    updateSelectedDelta(mouseWorldXY);
                }
                //tf.setText(selectedGObject.toString());
                //tf.setCursorPosition(tf.getText().length());

                leftTable.add(generatePropertiesTable(selectedGObject)).align(Align.left);
                leftTable.row();
                leftTable.add(generateComponentsTable(selectedGObject));
                selectedGObject.setProperty("selected", Boolean.TRUE.toString());
            }
            Gdx.app.log("Editor", newSelected + " selected");
        }

        private void updateSelectedDelta(Vector3 mouseWorldXY){
            if(selectedGObject != null)
                selectedDelta.set(selectedGObject.pos.x - mouseWorldXY.x, selectedGObject.pos.y - mouseWorldXY.y);
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
            if(button == Input.Buttons.RIGHT){
                GridComp.GridCell c = bs.level.getByName("World").getComponent(GridComp.class).getCell(temp.x, temp.y);
                player.getComponent(GridWalkingComp.class).walkTo(c);
            }
            stage.setKeyboardFocus(null);
            return true;
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

            return false;
        }

        private boolean debugAll = true;
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
            if(keycode == Input.Keys.F1){
                debugAll = !debugAll;
                stage.setDebugAll(debugAll);
                bs.view.setDrawDebug(debugAll);
            }
            if(keycode == Input.Keys.L){
                bs.level.load();
            }
            if(keycode == Input.Keys.S){
                bs.level.save();
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
