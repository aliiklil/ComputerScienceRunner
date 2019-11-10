package at.ac.univie.computersciencerunner.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.ArrayList;

import at.ac.univie.computersciencerunner.ComputerScienceRunner;
import at.ac.univie.computersciencerunner.WorldContactListener;
import at.ac.univie.computersciencerunner.hud.Hud;
import at.ac.univie.computersciencerunner.mapObjects.Brick;
import at.ac.univie.computersciencerunner.mapObjects.ECTS;
import at.ac.univie.computersciencerunner.mobs.Player;

public class PlayScreen implements Screen {

    public OrthographicCamera camera;
    private Viewport viewPort;
    private Hud hud;

    private TmxMapLoader tmxMapLoader;
    private TiledMap tiledMap;
    private OrthogonalTiledMapRenderer orthogonalTiledMapRenderer;

    private Player player;

    private World world;
    private Box2DDebugRenderer b2dr;

    private ArrayList<ECTS> ECTSList = new ArrayList<ECTS>();

    public PlayScreen() {

        camera = new OrthographicCamera();
        viewPort = new FitViewport(ComputerScienceRunner.WIDTH / ComputerScienceRunner.PPM, ComputerScienceRunner.HEIGHT / ComputerScienceRunner.PPM, camera);
        hud = new Hud(ComputerScienceRunner.batch);

        tmxMapLoader = new TmxMapLoader();
        tiledMap = tmxMapLoader.load("Semester1.tmx");
        orthogonalTiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap, 1 / ComputerScienceRunner.PPM);

        camera.position.set(viewPort.getWorldWidth() / 2, viewPort.getWorldHeight() / 2, 0);

        world = new World(new Vector2(0, -9.81f), true);
        world.setContactListener(new WorldContactListener());
        b2dr = new Box2DDebugRenderer();

        player = new Player(world);

        BodyDef bodyDef = new BodyDef();
        PolygonShape shape = new PolygonShape();
        FixtureDef fixtureDef = new FixtureDef();
        Body body;

        //Ground
        for(MapObject object : tiledMap.getLayers().get(2).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            bodyDef.type = BodyDef.BodyType.StaticBody;
            bodyDef.position.set((rect.getX() + rect.getWidth() / 2) / ComputerScienceRunner.PPM, (rect.getY() + rect.getHeight() / 2) / ComputerScienceRunner.PPM);

            body = world.createBody(bodyDef);

            shape.setAsBox(rect.getWidth() / 2 / ComputerScienceRunner.PPM, rect.getHeight() / 2 / ComputerScienceRunner.PPM);
            fixtureDef.shape = shape;
            fixtureDef.friction = 0;
            Fixture fixture = body.createFixture(fixtureDef);

            Filter filter = new Filter();
            filter.categoryBits = ComputerScienceRunner.GROUND_BIT;
            fixture.setFilterData(filter);
        }

        //Bricks
        for(MapObject object : tiledMap.getLayers().get(3).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            new Brick(world, tiledMap, rect);
        }

        //ECTS
        for(MapObject object : tiledMap.getLayers().get(4).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            ECTSList.add(new ECTS(world, tiledMap, rect));
        }

    }

    public void update(float dt) {

        world.step(1/60f, 6, 5);

        camera.position.x = player.body.getPosition().x;

        camera.update();
        orthogonalTiledMapRenderer.setView(camera);

        player.update(dt);

        for(ECTS ects : ECTSList) {
            ects.update(dt);
        }

    }

    @Override
    public void render(float delta) {
        update(delta);

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        orthogonalTiledMapRenderer.render();

        b2dr.render(world, camera.combined);

        ComputerScienceRunner.batch.setProjectionMatrix(hud.stage.getCamera().combined);
        hud.stage.draw();

        ComputerScienceRunner.batch.begin();
        player.draw();
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
    public void dispose() {

        tiledMap.dispose();
        orthogonalTiledMapRenderer.dispose();
        b2dr.dispose();
        world.dispose();
        hud.dispose();

    }

    public OrthographicCamera getCamera() {
        return camera;
    }

    public Player getPlayer() {
        return player;
    }

}
