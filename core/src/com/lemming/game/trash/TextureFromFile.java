package com.lemming.game.trash;

import com.badlogic.gdx.graphics.Texture;
import com.lemming.game.basic.Assets;

/**
 * Created by Alexander on 21.07.2015.
 */
public class TextureFromFile {
    public String fileName;
    public Texture texture;

    public TextureFromFile(String fileName) {
        set(fileName);
    }

    public void set(String fileName){
        this.fileName = fileName;
        if(fileName.equals(""))
            return;
        texture = Assets.get(fileName, Texture.class);
    }

}
