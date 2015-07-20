package com.lemming.game.basic;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

/**
 * Created by Alexander on 16.07.2015.
 */
public class Assets {
    public static final Texture DEFAULT_TEXTURE = new Texture("default texture.jpg");
    public static final Texture DEFAULT_TEXTURE2 = new Texture("default texture 2.jpg");
    public static final Skin DEFAULT_SKIN = new Skin(new FileHandle("data/uiskin.json"));
    public static final Pixmap DEFAULT_CURSOR = new Pixmap(new FileHandle("data/pre_particle.png"));

    public static final Texture DEFAULT_BACKGROUND = new Texture(new FileHandle("background.png"));
    static{
        DEFAULT_BACKGROUND.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
    }
}
