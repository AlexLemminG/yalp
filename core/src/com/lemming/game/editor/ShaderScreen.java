package com.lemming.game.editor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;

import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * Created by Alexander on 27.07.2015.
 */
public class ShaderScreen extends ScreenAdapter{
    ShaderProgram sp;
    ShaderProgram justDraw;
    ShaderProgram blurSP;
    Mesh mesh;
    Mesh fullScreenMesh;
    Texture texture;
    Texture textureRed;
    OrthographicCamera camera;
    FPSLogger fpsLogger = new FPSLogger();
    SpriteBatch batch = new SpriteBatch();
    ShaderProgram velSP;
    @Override
    public void resize(int width, int height) {
        camera.viewportWidth = width;
        camera.viewportHeight = height;
        size[0] = width;
        size[1] = height;
        velocityBuffer = new FrameBuffer(Pixmap.Format.RGBA8888, ((int) camera.viewportWidth), ((int) camera.viewportHeight), true);
        diffuseBuffer = new FrameBuffer(Pixmap.Format.RGBA8888, ((int) camera.viewportWidth), ((int) camera.viewportHeight), true);
        blurBuffer = new FrameBuffer(Pixmap.Format.RGBA8888, ((int) camera.viewportWidth), ((int) camera.viewportHeight), true);

    }

    public ShaderScreen() {
        ShaderProgram.pedantic = false;

        checkUpdate();
        camera = new OrthographicCamera();
        camera.zoom = 0.001f;


        float c = 0.5f;
        mesh = new Mesh(false, 4, 6, VertexAttribute.Position(), new VertexAttribute(512, 2, "a_velocity"), VertexAttribute.TexCoords(0));
        mesh.setVertices(new float[]
                {-c, -c, 0, 0, 0, 0, 0,
                        c, -c, 0, 0, 0,1, 0,
                        c, c, 0, 0, 0, 1, 1,
                        -c, c, 0, 0, 0, 0, 1});

        mesh.setIndices(new short[]{0, 1, 2, 2, 3, 0});

        c = 1f;
        fullScreenMesh = new Mesh(false, 4, 6, VertexAttribute.Position(), new VertexAttribute(512, 2, "a_velocity"), VertexAttribute.TexCoords(0));
        fullScreenMesh.setVertices(new float[]
                {-c, -c, 0, 0, 0, 0, 0,
                        c, -c, 0, 0, 0,1, 0,
                        c, c, 0, 0, 0, 1, 1,
                        -c, c, 0, 0, 0, 0, 1});

        fullScreenMesh.setIndices(new short[]{0, 1, 2, 2, 3, 0});


        texture = new Texture(Gdx.files.internal("cross.png"), Pixmap.Format.RGBA8888, false);
        textureRed = new Texture(Gdx.files.internal("data\\g3d\\environment\\debug_PX.png"));
        Gdx.input.setInputProcessor(new InputAdapter(){
            @Override
            public boolean mouseMoved(int screenX, int screenY) {
                angularVel = screenX * 1f / camera.viewportWidth;
                return super.mouseMoved(screenX, screenY);
            }
        });

        velSP = new ShaderProgram(sp.getVertexShaderSource(), fileToString("shaders/velocity.frag"));
        justDraw = new ShaderProgram(sp.getVertexShaderSource(), fileToString("shaders/justDraw.frag"));
        blurSP = new ShaderProgram(sp.getVertexShaderSource(), fileToString("shaders/blur.frag"));
        System.out.println(blurSP.isCompiled());
        System.out.println(blurSP.getLog());

        for(float x = -1f; x <= 1f; x+= 0.5){
            for(float y = -1f; y <= 1f; y+= 0.5){
                System.out.print(x + ", " + y + ", \n");
            }
        }

        for(float x = -1f; x <= 1f; x+= 0.5){
            for(float y = -1f; y <= 1f; y+= 0.5){
                System.out.print(Math.sqrt(1/(2*Math.PI)) * Math.exp(-(x*x + y*y)/2)*0.25f + ",\n");
            }
        }
    }

    public void updateShader(){
        ShaderProgram old = sp;


        sp = new ShaderProgram(vertexShader, fragmentShader);
        System.out.println(sp.getLog());
        Gdx.graphics.setTitle(sp.isCompiled() ? "compilled" : "error");
        if(!sp.isCompiled()){
            sp = old;
        }else{
            time = 0;
            sp.enableVertexAttribute("vel");

        }
    }

    String vertexShader = "";
    String fragmentShader = "";

    private String fileToString(String fileName)  {
        String result = "";
        try {
            Scanner sc = null;
            sc = new Scanner(Gdx.files.internal(fileName).file());
            while(sc.hasNextLine()) {
                String s = sc.nextLine() + "\n";
                result += s;
            }
            sc.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return result;
    }

    public void checkUpdate(){

        String fragmentShader = fileToString("shaders/shad.frag");

        String vertexShader = fileToString("shaders/shad.vert");

        if(!(vertexShader.equals(this.vertexShader) && fragmentShader.equals(this.fragmentShader))){
            this.vertexShader = vertexShader;
            this.fragmentShader = fragmentShader;
            updateShader();
        }

    }
    float[] mouse = new float[2];
    float[] size = new float[2];
    float[] speed = new float[]{-1, 1, -1, -1, 1, -1, 1, 1};
    float time = 0;
    FrameBuffer velocityBuffer;
    FrameBuffer diffuseBuffer;
    FrameBuffer blurBuffer;

    @Override
    public void dispose() {
        velocityBuffer.dispose();
    }
    float angularVel;
    float phi = 0;
    float[] currentSpeed = new float[8];
    @Override
    public void render(float delta) {
        mouse[0] = Gdx.input.getX() / size[0];
        mouse[1] = Gdx.input.getY() / size[1];
//        angularVel = 1;
        for(int i = 0; i < 4; i++){
            float[] temp = new float[2];
            mesh.getVertices(i * 7, 2, temp);

            currentSpeed[i*2] = temp[1] * angularVel;
            currentSpeed[i*2+1] = -temp[0] * angularVel;
        }


        for(int i = 0; i < 4; i++)
            mesh.updateVertices(i*7+3, currentSpeed, i*2, 2);
        phi+=angularVel*delta*10;
//        for(int i = 0; i < 4; i++) {
//            float[] temp = new float[2];
//            mesh.getVertices(i * 7, 2, temp);
//            temp[0] = ((float) (Math.cos(1.0 * i / 2 * Math.PI+ phi) * 0.75));
//            temp[1] = ((float) (Math.sin(1.0 * i / 2 * Math.PI + phi) * 0.75));
//            mesh.updateVertices(i * 7, temp);
//        }

        checkUpdate();
        time += delta;
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        camera.update();

        drawVel();
        drawDif();
        blurVel();




        sp.begin();
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        sp.setUniform2fv("resolution", size, 0, 2);
        sp.setUniform2fv("mouse", mouse, 0, 2);
        sp.setUniformf("time", time);
        blurBuffer.getColorBufferTexture().bind(2);
        sp.setUniformi("u_vel_blured_texture", 2);
        diffuseBuffer.getColorBufferTexture().bind(1);
        sp.setUniformi("u_texture", 1);
        velocityBuffer.getColorBufferTexture().bind(0);
        sp.setUniformi("u_vel_texture", 0);
        fullScreenMesh.render(sp, GL20.GL_TRIANGLES);
        sp.end();
        fpsLogger.log();
    }

    private void drawVel(){
        TextureRegion velocityBufferTR = new TextureRegion(velocityBuffer.getColorBufferTexture());
        velocityBufferTR.flip(false, true);
        velocityBuffer.begin();
        Gdx.gl.glClearColor(0.5f, 0.5f, 0.5f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        velSP.begin();
        texture.bind(0);
        velSP.setUniformi("u_texture", 0);
        mesh.render(velSP, GL20.GL_TRIANGLES);
        velSP.end();
        velocityBuffer.end();
    }

    private void drawDif(){
        TextureRegion diffuseBufferTR = new TextureRegion(velocityBuffer.getColorBufferTexture());
        diffuseBufferTR.flip(false, true);
        diffuseBuffer.begin();
        Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendEquation(GL20.GL_FUNC_ADD);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        justDraw.begin();
        texture.bind(0);
        justDraw.setUniformi("u_texture", 0);
        mesh.render(justDraw, GL20.GL_TRIANGLES);
        justDraw.end();
        diffuseBuffer.end();
    }

    private void blurVel(){
        TextureRegion blurTR = new TextureRegion(blurBuffer.getColorBufferTexture());
        blurTR.flip(false, true);
        blurBuffer.begin();
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        blurSP.begin();
        velocityBuffer.getColorBufferTexture().bind();
        blurSP.setUniformi("u_texture", 0);
        fullScreenMesh.render(blurSP, GL20.GL_TRIANGLES);
        blurSP.end();
        blurBuffer.end();
    }
}
