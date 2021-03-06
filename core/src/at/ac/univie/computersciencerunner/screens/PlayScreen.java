package at.ac.univie.computersciencerunner.screens;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.MathUtils;
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
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.ArrayList;

import at.ac.univie.computersciencerunner.ComputerScienceRunner;
import at.ac.univie.computersciencerunner.SmartphoneController;
import at.ac.univie.computersciencerunner.WorldContactListener;
import at.ac.univie.computersciencerunner.hud.Hud;
import at.ac.univie.computersciencerunner.hud.InfoWidget;
import at.ac.univie.computersciencerunner.mapObjects.Brick;
import at.ac.univie.computersciencerunner.mapObjects.Coin;
import at.ac.univie.computersciencerunner.mapObjects.CoinBrick;
import at.ac.univie.computersciencerunner.mapObjects.ECTS;
import at.ac.univie.computersciencerunner.mapObjects.ECTSBrick;
import at.ac.univie.computersciencerunner.mapObjects.Heart;
import at.ac.univie.computersciencerunner.mapObjects.HeartBrick;
import at.ac.univie.computersciencerunner.mapObjects.HorizontalPlatform;
import at.ac.univie.computersciencerunner.mapObjects.InfoBrick;
import at.ac.univie.computersciencerunner.mapObjects.Trampoline;
import at.ac.univie.computersciencerunner.mapObjects.VerticalPlatform;
import at.ac.univie.computersciencerunner.mobs.Spear;
import at.ac.univie.computersciencerunner.mobs.SpearBug;
import at.ac.univie.computersciencerunner.mobs.WalkingBug;
import at.ac.univie.computersciencerunner.mobs.FlyingBug;
import at.ac.univie.computersciencerunner.mobs.JumpingBug;
import at.ac.univie.computersciencerunner.mobs.Player;
import at.ac.univie.computersciencerunner.CustomOrthogonalTiledMapRenderer;

public class PlayScreen implements Screen {

    public OrthographicCamera camera;
    private Viewport viewPort;
    private Hud hud;

    private InfoWidget infoWidget;

    private TmxMapLoader tmxMapLoader;
    private TiledMap tiledMap;
    private CustomOrthogonalTiledMapRenderer customOrthogonalTiledMapRenderer;

    private Player player;

    private World world;
    private Box2DDebugRenderer b2dr;


    private ArrayList<Brick> brickList = new ArrayList<Brick>();

    private ArrayList<ECTS> ectsList = new ArrayList<ECTS>();
    private ArrayList<ECTSBrick> ectsBrickList = new ArrayList<ECTSBrick>();

    private ArrayList<HeartBrick> heartBrickList = new ArrayList<HeartBrick>();
    private ArrayList<Heart> heartList = new ArrayList<Heart>();

    private ArrayList<Coin> coinList = new ArrayList<Coin>();
    private ArrayList<CoinBrick> coinBrickList = new ArrayList<CoinBrick>();

    private boolean paused;

    private ComputerScienceRunner game;

    private int currentSemester;

    private ArrayList<WalkingBug> walkingBugList = new ArrayList<WalkingBug>();
    private ArrayList<JumpingBug> jumpingBugList = new ArrayList<JumpingBug>();
    private ArrayList<FlyingBug> flyingBugList = new ArrayList<FlyingBug>();
    private ArrayList<SpearBug> spearBugList = new ArrayList<SpearBug>();

    private ArrayList<Spear> spearList = new ArrayList<Spear>();
    private ArrayList<Spear> spearRemoveList = new ArrayList<Spear>(); //To this list are all spears added that shoud be removed. Needed to counter java.util.ConcurrentModificationException

    private ArrayList<Trampoline> trampolineList = new ArrayList<Trampoline>();

    private ArrayList<HorizontalPlatform> horizontalPlatformList = new ArrayList<HorizontalPlatform>();
    private ArrayList<VerticalPlatform> verticalPlatformList = new ArrayList<VerticalPlatform>();

    private SmartphoneController smartphoneController;

    public PlayScreen(ComputerScienceRunner game, int semester) {

        this.currentSemester = semester;

        this.game = game;

        camera = new OrthographicCamera( );
        viewPort = new FitViewport(ComputerScienceRunner.WIDTH / ComputerScienceRunner.PPM, ComputerScienceRunner.HEIGHT / ComputerScienceRunner.PPM, camera);
        viewPort.apply();

        tmxMapLoader = new TmxMapLoader();
        tiledMap = tmxMapLoader.load("levels/Semester" + semester + ".tmx");
        customOrthogonalTiledMapRenderer = new CustomOrthogonalTiledMapRenderer(tiledMap, 1 / ComputerScienceRunner.PPM, ComputerScienceRunner.batch);

        camera.position.set(viewPort.getWorldWidth() / 2, viewPort.getWorldHeight() / 2, 0);

        world = new World(new Vector2(0, -9.81f), true);
        world.setContactListener(new WorldContactListener(game));
        b2dr = new Box2DDebugRenderer();

        smartphoneController = new SmartphoneController(game);






        player = new Player(game, world);
        hud = new Hud(ComputerScienceRunner.batch, game);
        hud.setSemesterValue(semester);
        infoWidget  = new InfoWidget(ComputerScienceRunner.batch);

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
            brickList.add(new Brick(world, tiledMap, rect));
        }

        //ECTS
        for(MapObject object : tiledMap.getLayers().get(4).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            ectsList.add(new ECTS(world, tiledMap, rect));
        }

        //ECTSBricks
        for(MapObject object : tiledMap.getLayers().get(5).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            ectsBrickList.add(new ECTSBrick(world, tiledMap, rect));
        }

        //Hearts
        for(MapObject object : tiledMap.getLayers().get(6).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            heartList.add(new Heart(world, tiledMap, rect));
        }

        //HeartBricks
        for(MapObject object : tiledMap.getLayers().get(7).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            heartBrickList.add(new HeartBrick(world, tiledMap, rect));
        }

        //InfoBricks
        for(MapObject object : tiledMap.getLayers().get(8).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            new InfoBrick(world, tiledMap, rect);
        }

        //Walls
        for(MapObject object : tiledMap.getLayers().get(9).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            bodyDef.type = BodyDef.BodyType.StaticBody;
            bodyDef.position.set((rect.getX() + rect.getWidth() / 2) / ComputerScienceRunner.PPM, (rect.getY() + rect.getHeight() / 2) / ComputerScienceRunner.PPM);

            body = world.createBody(bodyDef);

            shape.setAsBox(rect.getWidth() / 2 / ComputerScienceRunner.PPM, rect.getHeight() / 2 / ComputerScienceRunner.PPM);
            fixtureDef.shape = shape;
            fixtureDef.friction = 0;
            Fixture fixture = body.createFixture(fixtureDef);

            Filter filter = new Filter();
            filter.categoryBits = ComputerScienceRunner.WALL_BIT;
            fixture.setFilterData(filter);
        }

        //Coins
        for(MapObject object : tiledMap.getLayers().get(10).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            coinList.add(new Coin(world, tiledMap, rect));
        }

        //CoinBricks
        for(MapObject object : tiledMap.getLayers().get(11).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            coinBrickList.add(new CoinBrick(world, tiledMap, rect));
        }

        //Goal (The two poles at the end of each semester/level)
        for(MapObject object : tiledMap.getLayers().get(12).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            bodyDef.type = BodyDef.BodyType.StaticBody;
            bodyDef.position.set((rect.getX() + rect.getWidth() / 2) / ComputerScienceRunner.PPM, (rect.getY() + rect.getHeight() / 2) / ComputerScienceRunner.PPM);

            body = world.createBody(bodyDef);

            shape.setAsBox(rect.getWidth() / 2 / ComputerScienceRunner.PPM, rect.getHeight() / 2 / ComputerScienceRunner.PPM);
            fixtureDef.shape = shape;
            fixtureDef.friction = 0;
            Fixture fixture = body.createFixture(fixtureDef);

            Filter filter = new Filter();
            filter.categoryBits = ComputerScienceRunner.GOAL_BIT;
            fixture.setFilterData(filter);
        }

        //Oneway Platforms
        for(MapObject object : tiledMap.getLayers().get(13).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            bodyDef.type = BodyDef.BodyType.StaticBody;
            bodyDef.position.set((rect.getX() + rect.getWidth() / 2) / ComputerScienceRunner.PPM, (rect.getY() + rect.getHeight() / 2) / ComputerScienceRunner.PPM);

            body = world.createBody(bodyDef);

            shape.setAsBox(rect.getWidth() / 2 / ComputerScienceRunner.PPM, rect.getHeight() / 2 / ComputerScienceRunner.PPM);
            fixtureDef.shape = shape;
            fixtureDef.friction = 0;
            Fixture fixture = body.createFixture(fixtureDef);

            Filter filter = new Filter();
            filter.categoryBits = ComputerScienceRunner.ONEWAY_PLATFORM_BIT;
            filter.maskBits = ComputerScienceRunner.PLAYER_FEET_BIT | ComputerScienceRunner.BUG_RIGHT_SENSOR_BIT | ComputerScienceRunner.BUG_LEFT_SENSOR_BIT | ComputerScienceRunner.BUG_BODY_BIT;
            fixture.setFilterData(filter);
        }



        //Bugs
        for(MapObject object : tiledMap.getLayers().get(14).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            WalkingBug walkingBug = new WalkingBug(game, world, rect.getX(),rect.getY() + 32);
            walkingBugList.add(walkingBug);
        }

        //JumpingBugs
        for(MapObject object : tiledMap.getLayers().get(15).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            JumpingBug jumpingBug = new JumpingBug(game, world, rect.getX(),rect.getY() + 32);
            jumpingBugList.add(jumpingBug);
        }

        //FlyingBugs
        for(MapObject object : tiledMap.getLayers().get(16).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            FlyingBug flyingBug = new FlyingBug(game, world, rect.getX(),rect.getY() + 32);
            flyingBugList.add(flyingBug);
        }

        //SpearBugs
        for(MapObject object : tiledMap.getLayers().get(17).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            SpearBug spearBug = new SpearBug(game, world, rect.getX(),rect.getY() + 32);
            spearBugList.add(spearBug);
        }

        //Spikes
        for(MapObject object : tiledMap.getLayers().get(18).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            bodyDef.type = BodyDef.BodyType.StaticBody;
            bodyDef.position.set((rect.getX() + rect.getWidth() / 2) / ComputerScienceRunner.PPM, (rect.getY() + rect.getHeight() / 2) / ComputerScienceRunner.PPM);

            body = world.createBody(bodyDef);

            shape.setAsBox(rect.getWidth() / 2 / ComputerScienceRunner.PPM, rect.getHeight() / 2 / ComputerScienceRunner.PPM);
            fixtureDef.shape = shape;
            fixtureDef.friction = 0;
            fixtureDef.restitution = 1;
            Fixture fixture = body.createFixture(fixtureDef);

            Filter filter = new Filter();
            filter.categoryBits = ComputerScienceRunner.SPIKES_BIT;
            fixture.setFilterData(filter);
        }

        //Trampoline
        for(MapObject object : tiledMap.getLayers().get(19).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            trampolineList.add(new Trampoline(world, tiledMap, rect));
        }

        //HorizontalPlatform
        for(MapObject object : tiledMap.getLayers().get(20).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            horizontalPlatformList.add(new HorizontalPlatform(game, world, rect.getX() + 32, rect.getY(), 0));
        }

        //VerticalPlatform
        for(MapObject object : tiledMap.getLayers().get(21).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            verticalPlatformList.add(new VerticalPlatform(game, world, rect.getX() + 32, rect.getY(), 0));
        }

        //Ground
        for(MapObject object : tiledMap.getLayers().get(22).getObjects().getByType(RectangleMapObject.class)) {
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



    }

    public void update(float dt) {

        if(!paused) {


            world.step(1 / 60f, 6, 5);

        }

        camera.position.x = MathUtils.clamp(player.body.getPosition().x, camera.viewportWidth / 2,608 - camera.viewportWidth / 2);

        camera.update();
        customOrthogonalTiledMapRenderer.setView(camera);




        player.update(dt);

        if(!paused) {
            for (ECTS ects : ectsList) {
                ects.update(dt);
            }

            for (Brick brick : brickList) {
                brick.update(dt);
            }

            for (ECTSBrick ectsBrick : ectsBrickList) {
                ectsBrick.update(dt);
            }

            for (Heart heart : heartList) {
                heart.update(dt);
            }

            for (HeartBrick heartBrick : heartBrickList) {
                heartBrick.update(dt);
            }

            for (Coin coin : coinList) {
                coin.update(dt);
            }

            for (CoinBrick coinBrick : coinBrickList) {
                coinBrick.update(dt);
            }

            for (WalkingBug walkingBug : walkingBugList) {
                walkingBug.update(dt);
            }

            for (JumpingBug jumpingBug : jumpingBugList) {
                jumpingBug.update(dt);
            }

            for (FlyingBug flyingBug : flyingBugList) {
                flyingBug.update(dt);
            }

            for (SpearBug spearBug : spearBugList) {
                spearBug.update(dt);
            }

            for (Spear spear : spearList) {
                spear.update(dt);
            }

            spearList.removeAll(spearRemoveList);

            for (Trampoline trampoline : trampolineList) {
                trampoline.update(dt);
            }
            for (HorizontalPlatform horizontalPlatform : horizontalPlatformList) {
                horizontalPlatform.update(dt);
            }
            for (VerticalPlatform verticalPlatform : verticalPlatformList) {
                verticalPlatform.update(dt);
            }

        }

    }

    @Override
    public void render(float delta) {
        update(delta);

        Gdx.gl.glClearColor(0f, 0f, 0f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        customOrthogonalTiledMapRenderer.render();

        //b2dr.render(world, camera.combined); //Remove the comment to display the lines of box2d for debugging

        ComputerScienceRunner.batch.setProjectionMatrix(hud.stage.getCamera().combined);
        hud.stage.draw();





        ComputerScienceRunner.batch.begin();
        for(HorizontalPlatform horizontalPlatform : horizontalPlatformList) {
            horizontalPlatform.draw();
        }

        for(VerticalPlatform verticalPlatform : verticalPlatformList) {
            verticalPlatform.draw();
        }
        player.draw();
        for(WalkingBug walkingBug : walkingBugList) {
            walkingBug.draw();
        }
        for(JumpingBug jumpingBug : jumpingBugList) {
            jumpingBug.draw();
        }
        for(FlyingBug flyingBug : flyingBugList) {
            flyingBug.draw();
        }
        for(SpearBug spearBug : spearBugList) {
            spearBug.draw();
        }
        for(Spear spear : spearList) {
            spear.draw();
        }


        ComputerScienceRunner.batch.end();





        if(player.isDead()) {
            game.setGameOverScreen();
            dispose();
        }

        if(player.isReachedGoal()) {
            game.setQuestionScreen();
            dispose();
        }

        //if(Gdx.app.getType() == Application.ApplicationType.Android)
        smartphoneController.draw();

        if(infoWidget.isCurrentlyDisplayed()) {
            infoWidget.stage.draw();
        }

    }

    @Override
    public void resize(int width, int height) {
        viewPort.update(width, height);
        smartphoneController.resize(width, height);
    }

    @Override
    public void show() {

        ComputerScienceRunner.assetManager.get("audio/music/menu.mp3", Music.class).stop();

        Music levelMusic = ComputerScienceRunner.assetManager.get("audio/music/level" + currentSemester + ".mp3", Music.class);
        levelMusic.setLooping(true);
        levelMusic.play();
    }

    @Override
    public void pause() {

        paused = true;

    }

    @Override
    public void resume() {

        paused = false;

        int coinCount = hud.getCoinCount();
        int ectsCount = hud.getEctsCount();

        hud = new Hud(ComputerScienceRunner.batch, game);
        hud.setSemesterValue(currentSemester);
        hud.setEctsCount(ectsCount);
        hud.setCoinCount(coinCount);
        hud.setHeartsCount(player.getHearts());

        smartphoneController = new SmartphoneController(game);
        smartphoneController.setInputProcessorToStage();

    }

    @Override
    public void hide() { }

    @Override
    public void dispose() {

        tiledMap.dispose();
        customOrthogonalTiledMapRenderer.dispose();
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

    public Hud getHud() {
        return hud;
    }

    public TiledMap getTiledMap() {
        return tiledMap;
    }

    public World getWorld() {
        return world;
    }

    public ArrayList<ECTS> getEctsList() {
        return ectsList;
    }

    public ArrayList<Heart> getHeartList() {
        return heartList;
    }

    public InfoWidget getInfoWidget() { return infoWidget; }

    public boolean isPaused() { return paused; }

    public CustomOrthogonalTiledMapRenderer getCustomOrthogonalTiledMapRenderer() { return customOrthogonalTiledMapRenderer; }

    public int getCurrentSemester() { return currentSemester; }

    public ArrayList<Coin> getCoinList() { return coinList; }

    public ArrayList<Spear> getSpearList() {
        return spearList;
    }

    public ArrayList<Spear> getSpearRemoveList() {
        return spearRemoveList;
    }

    public Viewport getViewPort() {
        return viewPort;
    }

    public SmartphoneController getSmartphoneController() {
        return smartphoneController;
    }

    public Music getLevelMusic() {
        return ComputerScienceRunner.assetManager.get("audio/music/level" + currentSemester + ".mp3", Music.class);
    }
}
