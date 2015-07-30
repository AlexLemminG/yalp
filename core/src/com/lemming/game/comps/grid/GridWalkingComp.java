package com.lemming.game.comps.grid;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.lemming.game.basic.GObject;
import com.lemming.game.comps.Comp;
import com.lemming.game.ui.EditableValue;

import java.util.List;

/**
 * Created by Alexander on 29.07.2015.
 */
public class GridWalkingComp extends Comp{
    EditableValue.IntValue x = new EditableValue.IntValue(0, "x");
    EditableValue.IntValue y = new EditableValue.IntValue(0, "y");
    EditableValue.FloatValue timeForOne = new EditableValue.FloatValue(0.1f, "timeForOne");
    EditableValue.BoolValue walk = new EditableValue.BoolValue(false, "walk"){
        @Override
        public void set(Boolean value) {
            super.set(value);
            if(value) {
                time = 0;
                clearSelection();
                path = null;
            }
        }
    };

    @Override
    public Array<EditableValue> getValues() {
        Array<EditableValue> values = super.getValues();
        values.add(x);
        values.add(y);
        values.add(timeForOne);
        values.add(walk);
        return values;
    }

    private GridComp gridComp;
    private ClampToGridComp clampToGridComp;
    List<GridComp.GridCell> path;
    @Override
    public void onCreate() {
        super.onCreate();
        gridComp = owner.level.getByName("World").getComponent(GridComp.class);
        clampToGridComp = owner.getComponent(ClampToGridComp.class);
    }

    float time;
    public GridWalkingComp(GObject gObject) {
        super(gObject);
    }

    @Override
    public void update(float dt) {
        super.update(dt);
        if(walk.get() && (path == null)){
            path = gridComp.bfs(clampToGridComp.cell, gridComp.getCell(x.get(), y.get()));
            if(path.size() == 0){
                walk.set(false);
            }
        }
        if(walk.get()){
            int currentCellNum = (int)(time / timeForOne.get());
            currentCellNum = MathUtils.clamp(currentCellNum, 0, path.size()-1);
            GridComp.GridCell currentCell = path.get(currentCellNum);
            clampToGridComp.x.set(currentCell.xi);
            clampToGridComp.y.set(currentCell.yi);
            if(currentCellNum >= path.size() - 1)
                walk.set(false);
            clearSelection();

            if(walk.get())
                for(int i = currentCellNum; i < path.size(); i++){
                    path.get(i).selected = true;
                }
        }
        time += dt;

    }

    private void clearSelection(){
        if(path != null)
            for(GridComp.GridCell c : path){
                c.selected = false;
            }
    }

    public void walkTo(GridComp.GridCell c) {
        x.set(c.xi);
        y.set(c.yi);
        walk.set(true);
    }
}
