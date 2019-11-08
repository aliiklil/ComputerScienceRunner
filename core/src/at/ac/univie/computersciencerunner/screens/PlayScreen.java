package at.ac.univie.computersciencerunner.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import at.ac.univie.computersciencerunner.ComputerScienceRunner;
import at.ac.univie.computersciencerunner.hud.Hud;
import at.ac.univie.computersciencerunner.mobs.Player;

public class PlayScreen implements Screen {

    public OrthographicCamera orthographicCamera;
    private Viewport viewPort;
    private Hud hud;

    private TmxMapLoader tmxMapLoader;
    private TiledMap tiledMap;
    private OrthogonalTiledMapRenderer orthogonalTiledMapRenderer;

    private Player player;

    public PlayScreen() {

        orthographicCamera = new OrthographicCamera();
        viewPort = new FitViewport(ComputerScienceRunner.WIDTH, ComputerScienceRunner.HEIGHT, orthographicCamera);
        hud = new Hud(ComputerScienceRunner.batch);

        tmxMapLoader = new TmxMapLoader();
        tiledMap = tmxMapLoader.load("Semester1.tmx");
        orthogonalTiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap);

        orthographicCamera.position.set(viewPort.getWorldWidth() / 2, viewPort.getWorldHeight() / 2, 0);

        player = new Player();
    }

    public void update(float dt) {
        orthographicCamera.update();
        orthogonalTiledMapRenderer.setView(orthographicCamera);

        player.update(dt);
    }

    @Override
    public void render(float delta) {
        update(delta);

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        orthogonalTiledMapRenderer.render();

        ComputerScienceRunner.batch.setProjectionMatrix(hud.stage.getCamera().combined);
        hud.stage.draw();

        ComputerScienceRunner.batch.begin();
        player.draw(ComputerScienceRunner.batch);
        ComputerScienceRunner.batch.end();
    }

    @Override
    public void resize(int width, int height) {
        viewPort.update(width, height);
    }

    @Override
    public void show() { }

    @Override
    public void pause() { }

    @Override
    public void resume() { }

    @Override
    public void hide() { }

    @Override
    public void dispose() { }

    public OrthographicCamera getOrthographicCamera() {
        return orthographicCamera;
    }

}
