package com.lemming.game.basic;

import box2dLight.RayHandler;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;

/**
 * Created by Alexander on 16.07.2015.
 */
public class View {

    public Rectangle viewport = new Rectangle();
    private Level level;
    public boolean drawBackground = true;

    public void setSpriteBatch(SpriteBatch spriteBatch) {
        this.spriteBatch = spriteBatch;
    }

    public void setCurrentRenderer(Renderers currentRenderer) {
        this.currentRenderer = currentRenderer;
    }

    public void resize(int width, int height) {
        camera.viewportWidth = width;
        camera.viewportHeight = height;
        viewport.width = width;
        viewport.height = height;
    }
    
    public void setViewport(float x, float y, float w, float h){
        resize(((int) w), ((int) h));
        viewport.x = x;
        viewport.y = y;
    }

    public ShapeRenderer getShapeRenderer() {
        return shapeRenderer;
    }

    public RayHandler getRayHandler() {
        return box2dLightRayHandler;
    }

    public OrthographicCamera getCamera() {
        return camera;
    }

    public enum Renderers{
        NONE, SPRITE_BATCH, POLYGON_SPRITE_BATCH, SHAPE_RENDERER
    }

    private Renderers currentRenderer = Renderers.NONE;
    private RayHandler box2dLightRayHandler;
    private SpriteBatch spriteBatch;
    private PolygonSpriteBatch polygonSpriteBatch;
    private ShapeRenderer shapeRenderer;
    private OrthographicCamera camera;
    private Box2DDebugRenderer box2DDebugRenderer;
    public Texture background;

    public Renderers getCurrentRenderer() {
        return currentRenderer;
    }
    public SpriteBatch getSpriteBatch() {
        return spriteBatch;
    }

    public View(Level level) {
        this.level = level;
        spriteBatch = new SpriteBatch();
        polygonSpriteBatch = new PolygonSpriteBatch();
        shapeRenderer = new ShapeRenderer();
        shapeRenderer.setAutoShapeType(true);
        camera = new OrthographicCamera();
        box2dLightRayHandler = new RayHandler(level.world);
        box2dLightRayHandler.setAmbientLight(1f);
        box2DDebugRenderer = new Box2DDebugRenderer();
        background = Assets.DEFAULT_BACKGROUND;
        level.view = this;
    }

    private boolean drawDebug = true;
    private boolean drawSprites = true;
    public void render(){
        prepare();

        camera.update();
        spriteBatch.setProjectionMatrix(camera.combined);
        polygonSpriteBatch.setProjectionMatrix(camera.combined);
        shapeRenderer.setProjectionMatrix(camera.combined);
        box2dLightRayHandler.setCombinedMatrix(camera);
        box2dLightRayHandler.useCustomViewport(((int) viewport.x), ((int) viewport.y), ((int) viewport.width), ((int) viewport.height));
        if(drawSprites) {
            currentRenderer = Renderers.SPRITE_BATCH;
            spriteBatch.begin();
            drawBackground();
            renderAll();
            spriteBatch.end();

            currentRenderer = Renderers.POLYGON_SPRITE_BATCH;
            polygonSpriteBatch.begin();
            renderAll();
            polygonSpriteBatch.end();

            box2dLightRayHandler.updateAndRender();
//            Gdx.gl.glViewport(((int) viewport.x), ((int) viewport.y), ((int) viewport.width), ((int) viewport.height));

        }

        if(drawDebug) {

            camera.update();
            currentRenderer = Renderers.SHAPE_RENDERER;
            shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
            renderAll();
            shapeRenderer.end();

            box2DDebugRenderer.render(level.world, camera.combined);
        }

        currentRenderer = Renderers.NONE;
    }
    private void drawBackground() {
        if(drawBackground)
            spriteBatch.draw(background, -500, -500, 1000,1000, 0,0,10,10);
    }

    public boolean disableClear = true;
    private void prepare() {
        Gdx.gl.glViewport(((int) viewport.x), ((int) viewport.y), ((int) viewport.width), ((int) viewport.height));
        if(!disableClear) {
            float howWhite = 0.5f;
            Gdx.gl.glClearColor(howWhite, howWhite, howWhite, 1);
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        }
    }

    private void renderAll(){
        for(GObject o : level.gObjects){
            o.render(this);
        }
    }
}
