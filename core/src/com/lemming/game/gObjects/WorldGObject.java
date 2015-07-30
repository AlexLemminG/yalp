package com.lemming.game.gObjects;

import com.lemming.game.basic.GObject;
import com.lemming.game.basic.Level;
import com.lemming.game.comps.grid.GridComp;
import com.lemming.game.comps.WorldSettingComp;

/**
 * Created by Alexander on 29.07.2015.
 */
public class WorldGObject extends GObject {
    @Override
    public String toString() {
        return "World";
    }

    public WorldGObject(Level level) {
        super(level);
        new WorldSettingComp(this).setEnabled(true);
        new GridComp(this).setEnabled(true);
        setPos(0 / 0f, 0 / 0f);
    }


}
