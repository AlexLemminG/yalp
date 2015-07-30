package com.lemming.game;

import com.badlogic.gdx.Game;
import com.lemming.game.editor.EditorScreen;

public class MGame extends Game {
    /*TODO
    1 - доделать polylineToFixtures через demertfun"a
    5 - move some to GUI
    6 - тэги Comp
    7 - добавить фильтры в bodyComp ?
    8 - добавить повороты в ImageComp
    9 - объединить pos-angle-scale в EditableValue
    11 - polylineValue
    12 editableValue - добавить showValue
    13 - подобавлять Comp в createByName
    14 - метод load-save в EditorScreen(нужно лучше убирать мусор после загрузки лвла)
    15 - чето не  так с select
     */
    @Override
    public void create() {
        setScreen(new EditorScreen());
    }
}
