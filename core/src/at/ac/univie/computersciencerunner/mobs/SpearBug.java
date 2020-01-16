package at.ac.univie.computersciencerunner.mobs;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.MassData;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

import at.ac.univie.computersciencerunner.ComputerScienceRunner;

public class SpearBug {

    private float x;
    private float y;

    private int spriteWidth = 64;
    private int spriteHeight = 64;

    private Texture texture = new Texture(Gdx.files.internal("enemies/spearBug.png"));

    private Animation<TextureRegion> standLeftAnimation;
    private Animation<TextureRegion> standRightAnimation;

    private Animation<TextureRegion> throwLeftAnimation;
    private Animation<TextureRegion> throwRightAnimation;

    private Animation<TextureRegion> stompedLeftAnimation;
    private Animation<TextureRegion> stompedRightAnimation;

    private Animation<TextureRegion> currentAnimation;

    private float stateTime;

    private TextureRegion currentFrame;

    public Body body;

    private boolean dead; //True if bug died

    private ComputerScienceRunner game;

    private PolygonShape leftSensor;
    private PolygonShape rightSensor;

    private boolean setToDestroy;
    private boolean destroyed;

    private long timestampThrowAnimationBegins;
    private long durationBetweenThrows = 2000;

    private boolean throwLeft; //True if player should throw spear to the left (because player is there)
    private boolean throwRight; //True if player should throw spear to the right (because player is there)

    public SpearBug(ComputerScienceRunner computerScienceRunner, World world, float x, float y) {

        this.game = computerScienceRunner;

        createStandLeftAnimation();
        createStandRightAnimation();

        createThrowLeftAnimation();
        createThrowRightAnimation();

        createStompedLeftAnimation();
        createStompedRightAnimation();

        currentAnimation = standLeftAnimation;

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(x / ComputerScienceRunner.PPM, y / ComputerScienceRunner.PPM);
        bodyDef.linearDamping = 1.0f;

        body = world.createBody(bodyDef);
        FixtureDef fixtureDef = new FixtureDef();


        MassData massData = body.getMassData();

        massData.center.set(0, 0);
        massData.mass = 1000; //Give it a lot of mass, so player cant move it

        body.setMassData(massData);




        PolygonShape bugBody = new PolygonShape();
        bugBody.setAsBox(16 / ComputerScienceRunner.PPM, 8 / ComputerScienceRunner.PPM, new Vector2(0 / ComputerScienceRunner.PPM, -16 / ComputerScienceRunner.PPM), 0);
        fixtureDef.shape = bugBody;
        fixtureDef.friction = 0;

        fixtureDef.filter.categoryBits = ComputerScienceRunner.BUG_BODY_BIT;
        fixtureDef.filter.maskBits = ComputerScienceRunner.GROUND_BIT | ComputerScienceRunner.WALL_BIT | ComputerScienceRunner.GOAL_BIT | ComputerScienceRunner.ONEWAY_PLATFORM_BIT | ComputerScienceRunner.PLAYER_BIT;

        body.createFixture(fixtureDef).setUserData(this);




        EdgeShape head = new EdgeShape();
        head.set(new Vector2(-16 / ComputerScienceRunner.PPM, 30 / ComputerScienceRunner.PPM), new Vector2(16 / ComputerScienceRunner.PPM, 30 / ComputerScienceRunner.PPM));

        fixtureDef.shape = head;
        fixtureDef.friction = 0;
        fixtureDef.restitution = 0.8f;

        fixtureDef.filter.categoryBits = ComputerScienceRunner.BUG_HEAD_BIT;
        fixtureDef.filter.maskBits = ComputerScienceRunner.GROUND_BIT | ComputerScienceRunner.GOAL_BIT | ComputerScienceRunner.ONEWAY_PLATFORM_BIT | ComputerScienceRunner.PLAYER_FEET_BIT | ComputerScienceRunner.PLAYER_BIT | ComputerScienceRunner.PLAYER_HEAD_BIT;

        body.createFixture(fixtureDef).setUserData(this);



        leftSensor = new PolygonShape();
        leftSensor.setAsBox(-200 / ComputerScienceRunner.PPM, 10 / ComputerScienceRunner.PPM, new Vector2(-200 / ComputerScienceRunner.PPM, 0 / ComputerScienceRunner.PPM), 0);
        fixtureDef.shape = leftSensor;
        fixtureDef.friction = 0;
        fixtureDef.isSensor = true;

        fixtureDef.filter.categoryBits = ComputerScienceRunner.BUG_LEFT_SENSOR_BIT;
        fixtureDef.filter.maskBits = ComputerScienceRunner.GROUND_BIT | ComputerScienceRunner.WALL_BIT | ComputerScienceRunner.GOAL_BIT | ComputerScienceRunner.PLAYER_BIT | ComputerScienceRunner.ONEWAY_PLATFORM_BIT | ComputerScienceRunner.PLAYER_HEAD_BIT;

        body.createFixture(fixtureDef).setUserData(this);



        rightSensor = new PolygonShape();
        rightSensor.setAsBox(200 / ComputerScienceRunner.PPM, 10 / ComputerScienceRunner.PPM, new Vector2(200 / ComputerScienceRunner.PPM, 0 / ComputerScienceRunner.PPM), 0);
        fixtureDef.shape = rightSensor;
        fixtureDef.friction = 0;
        fixtureDef.isSensor = true;

        fixtureDef.filter.categoryBits = ComputerScienceRunner.BUG_RIGHT_SENSOR_BIT;
        fixtureDef.filter.maskBits = ComputerScienceRunner.GROUND_BIT | ComputerScienceRunner.WALL_BIT | ComputerScienceRunner.GOAL_BIT | ComputerScienceRunner.PLAYER_BIT | ComputerScienceRunner.ONEWAY_PLATFORM_BIT | ComputerScienceRunner.PLAYER_HEAD_BIT;

        body.createFixture(fixtureDef).setUserData(this);

    }

    public void createStandLeftAnimation() {
        Array<TextureRegion> frames = new Array<TextureRegion>();
        for(int i = 0; i < 1; i++) {
            frames.add(new TextureRegion(texture, 0, 0, spriteWidth, spriteHeight));
        }
        standLeftAnimation = new Animation(0.1f, frames);
        frames.clear();
    }

    public void createStandRightAnimation() {
        Array<TextureRegion> frames = new Array<TextureRegion>();
        for(int i = 0; i < 1; i++) {
            frames.add(new TextureRegion(texture, 0, 64, spriteWidth, spriteHeight));
        }
        standRightAnimation = new Animation(0.1f, frames);
        frames.clear();
    }

    public void createThrowLeftAnimation() {
        Array<TextureRegion> frames = new Array<TextureRegion>();
        for(int i = 0; i < 4; i++) {
            frames.add(new TextureRegion(texture, i * 64, 128, spriteWidth, spriteHeight));
        }
        throwLeftAnimation = new Animation(0.1f, frames);
        frames.clear();
    }

    public void createThrowRightAnimation() {
        Array<TextureRegion> frames = new Array<TextureRegion>();
        for(int i = 0; i < 4; i++) {
            frames.add(new TextureRegion(texture, i * 64, 192, spriteWidth, spriteHeight));
        }
        throwRightAnimation = new Animation(0.1f, frames);
        frames.clear();
    }

    public void createStompedLeftAnimation() {
        Array<TextureRegion> frames = new Array<TextureRegion>();
        for(int i = 0; i < 1; i++) {
            frames.add(new TextureRegion(texture, 0, 256, spriteWidth, spriteHeight));
        }
        stompedLeftAnimation = new Animation(0.1f, frames);
        frames.clear();
    }

    public void createStompedRightAnimation() {
        Array<TextureRegion> frames = new Array<TextureRegion>();
        for(int i = 0; i < 1; i++) {
            frames.add(new TextureRegion(texture, 0, 320, spriteWidth, spriteHeight));
        }
        stompedRightAnimation = new Animation(0.1f, frames);
        frames.clear();
    }



    public void update(float dt) {
        stateTime = stateTime + dt;

        currentFrame = currentAnimation.getKeyFrame(stateTime, false);


        if(!setToDestroy) {
            x = body.getPosition().x;
            y = body.getPosition().y;
        }

        if(setToDestroy && !destroyed) {
            ComputerScienceRunner.playScreen.getWorld().destroyBody(body);
            destroyed = true;
            stateTime = 0;
            ComputerScienceRunner.assetManager.get("audio/sounds/bugKill.mp3", Sound.class).play();
        }

        if(throwLeft && currentAnimation != throwLeftAnimation && System.currentTimeMillis() - timestampThrowAnimationBegins > durationBetweenThrows) {
            currentAnimation = throwLeftAnimation;
            timestampThrowAnimationBegins = System.currentTimeMillis();
            ComputerScienceRunner.playScreen.getSpearList().add(new Spear(game, ComputerScienceRunner.playScreen.getWorld(), x, y, 0));
        }

        if(throwRight && currentAnimation != throwLeftAnimation && System.currentTimeMillis() - timestampThrowAnimationBegins > durationBetweenThrows) {
            currentAnimation = throwRightAnimation;
            timestampThrowAnimationBegins = System.currentTimeMillis();
            ComputerScienceRunner.playScreen.getSpearList().add(new Spear(game, ComputerScienceRunner.playScreen.getWorld(), x, y, 1));
        }

    }

    public void draw() {

        if(!destroyed || stateTime < 1) {
            Camera camera = ComputerScienceRunner.playScreen.getCamera();

            Vector3 worldCoordinates = new Vector3(x, y, 0);
            Vector3 screenCoordinates = camera.project(worldCoordinates);

           ComputerScienceRunner.batch.draw(currentFrame, screenCoordinates.x - 32, screenCoordinates.y - 24);
        }
    }

    public void hitOnHead() {
        setToDestroy = true;
        if(currentAnimation == standLeftAnimation || currentAnimation == throwLeftAnimation) {
            currentAnimation = stompedLeftAnimation;
        }

        if(currentAnimation == standRightAnimation || currentAnimation == throwRightAnimation) {
            currentAnimation = stompedRightAnimation;
        }
    }

    public boolean isDead() {
        return dead;
    }

    public void setCurrentAnimation(Animation<TextureRegion> currentAnimation) {
        this.currentAnimation = currentAnimation;
    }

    public Animation<TextureRegion> getThrowLeftAnimation() {
        return throwLeftAnimation;
    }

    public Animation<TextureRegion> getThrowRightAnimation() {
        return throwRightAnimation;
    }

    public PolygonShape getLeftSensor() {
        return leftSensor;
    }

    public PolygonShape getRightSensor() {
        return rightSensor;
    }

    public void setStateTime(float stateTime) {
        this.stateTime = stateTime;
    }

    public void setThrowLeft(boolean throwLeft) {
        this.throwLeft = throwLeft;
    }

    public void setThrowRight(boolean throwRight) {
        this.throwRight = throwRight;
    }

    public Animation<TextureRegion> getStandLeftAnimation() {
        return standLeftAnimation;
    }

    public Animation<TextureRegion> getStandRightAnimation() {
        return standRightAnimation;
    }
}
