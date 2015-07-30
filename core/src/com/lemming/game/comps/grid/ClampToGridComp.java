package com.lemming.game.comps.grid;

import com.badlogic.gdx.utils.Array;
import com.lemming.game.basic.GObject;
import com.lemming.game.comps.Comp;
import com.lemming.game.ui.EditableValue;

/**
 * Created by Alexander on 29.07.2015.
 */
public class ClampToGridComp extends Comp {
    EditableValue.IntValue x = new EditableValue.IntValue(0, "x"){
        @Override
        public void set(Integer value) {
            if(!value.equals(get()))
                changed = true;
            super.set(value);
        }
    };
    EditableValue.IntValue y = new EditableValue.IntValue(0, "y"){
        @Override
        public void set(Integer value) {
            if(!value.equals(get()))
                changed = true;
            super.set(value);
        }
    };
    boolean changed = false;
    GridComp.GridCell cell;

    @Override
    public void update(float dt) {
        super.update(dt);
        GridComp.GridCell newCell;
        if(changed){
            newCell = gridComp.getCell(x.get(), y.get());
            changed = false;
        }else{
            newCell = gridComp.getCell(owner.pos.x, owner.pos.y);
        }
        setCell(newCell);

    }

    private void setCell(GridComp.GridCell newCell){
        if(cell != newCell){
            if(cell != null) cell.removeValue(owner, true);
            newCell.add(owner);
            cell = newCell;
        }
        x.set(cell.xi);
        y.set(cell.yi);
        owner.setPos(cell.getCenter());
    }

    public ClampToGridComp(GObject gObject) {
        super(gObject);
    }

    GridComp gridComp;
    @Override
    public void onCreate() {
        super.onCreate();
        gridComp = owner.level.getByName("World").getComponent(GridComp.class);
    }

    @Override
    public Array<EditableValue> getValues() {
        Array<EditableValue> values = super.getValues();
        values.add(x);
        values.add(y);
        return values;
    }
}
