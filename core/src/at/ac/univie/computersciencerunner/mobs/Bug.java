package at.ac.univie.computersciencerunner.mobs;

import com.badlogic.gdx.Gdx;
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
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

import org.omg.CORBA.COMM_FAILURE;

import at.ac.univie.computersciencerunner.ComputerScienceRunner;

public class Bug {

    private float x;
    private float y;

    private int spriteWidth = 64;
    private int spriteHeight = 64;

    private Texture texture = new Texture(Gdx.files.internal("bug.png"));

    private Animation<TextureRegion> runLeftAnimation;
    private Animation<TextureRegion> runRightAnimation;

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

    private boolean leftSensorCollides = true;
    private boolean rightSensorCollides = true;

    private boolean setToDestroy;
    private boolean destroyed;

    public Bug(ComputerScienceRunner computerScienceRunner, World world, float x, float y) {

        this.game = computerScienceRunner;

        createRunLeftAnimation();
        createRunRightAnimation();

        createStompedLeftAnimation();
        createStompedRightAnimation();

        currentAnimation = runRightAnimation;

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(x / ComputerScienceRunner.PPM, y / ComputerScienceRunner.PPM);

        body = world.createBody(bodyDef);
        FixtureDef fixtureDef = new FixtureDef();







        CircleShape circle = new CircleShape();
        circle.setRadius(8 / ComputerScienceRunner.PPM);
        circle.setPosition(new Vector2(0, -16 / ComputerScienceRunner.PPM));
        fixtureDef.shape = circle;
        fixtureDef.friction = 0;

        fixtureDef.filter.categoryBits = ComputerScienceRunner.BUG_BODY_BIT;
        fixtureDef.filter.maskBits = ComputerScienceRunner.GROUND_BIT | ComputerScienceRunner.BRICK_BIT | ComputerScienceRunner.ECTS_BIT | ComputerScienceRunner.ECTS_BRICK_BIT | ComputerScienceRunner.HEART_BIT | ComputerScienceRunner.HEART_BRICK_BIT | ComputerScienceRunner.INFO_BRICK_BIT | ComputerScienceRunner.COIN_BIT | ComputerScienceRunner.COIN_BRICK_BIT | ComputerScienceRunner.GOAL_BIT | ComputerScienceRunner.ONEWAY_PLATFORM_BIT | ComputerScienceRunner.PLAYER_BIT;

        body.createFixture(fixtureDef);





        EdgeShape head = new EdgeShape();
        head.set(new Vector2(-20 / ComputerScienceRunner.PPM, 0 / ComputerScienceRunner.PPM), new Vector2(20 / ComputerScienceRunner.PPM, 0 / ComputerScienceRunner.PPM));

        fixtureDef.shape = head;
        fixtureDef.friction = 0;
        fixtureDef.restitution = 0.8f;

        fixtureDef.filter.categoryBits = ComputerScienceRunner.BUG_HEAD_BIT;
        fixtureDef.filter.maskBits = ComputerScienceRunner.GROUND_BIT | ComputerScienceRunner.BRICK_BIT | ComputerScienceRunner.ECTS_BRICK_BIT | ComputerScienceRunner.HEART_BRICK_BIT | ComputerScienceRunner.INFO_BRICK_BIT | ComputerScienceRunner.COIN_BIT | ComputerScienceRunner.COIN_BRICK_BIT | ComputerScienceRunner.GOAL_BIT | ComputerScienceRunner.ONEWAY_PLATFORM_BIT | ComputerScienceRunner.PLAYER_BIT | ComputerScienceRunner.PLAYER_FEET_BIT;

        body.createFixture(fixtureDef).setUserData(this);



        leftSensor = new PolygonShape();
        leftSensor.setAsBox(4 / ComputerScienceRunner.PPM, 16 / ComputerScienceRunner.PPM, new Vector2(-16 / ComputerScienceRunner.PPM, -24 / ComputerScienceRunner.PPM), 0);
        fixtureDef.shape = leftSensor;
        fixtureDef.friction = 0;
        fixtureDef.isSensor = true;

        fixtureDef.filter.categoryBits = ComputerScienceRunner.BUG_LEFT_SENSOR_BIT;
        fixtureDef.filter.maskBits = ComputerScienceRunner.GROUND_BIT | ComputerScienceRunner.BRICK_BIT | ComputerScienceRunner.ECTS_BIT | ComputerScienceRunner.ECTS_BRICK_BIT | ComputerScienceRunner.HEART_BIT | ComputerScienceRunner.HEART_BRICK_BIT | ComputerScienceRunner.INFO_BRICK_BIT | ComputerScienceRunner.WALL_BIT | ComputerScienceRunner.COIN_BIT | ComputerScienceRunner.COIN_BRICK_BIT | ComputerScienceRunner.GOAL_BIT | ComputerScienceRunner.PLAYER_BIT| ComputerScienceRunner.ONEWAY_PLATFORM_BIT;

        body.createFixture(fixtureDef).setUserData(this);



        rightSensor = new PolygonShape();
        rightSensor.setAsBox(4 / ComputerScienceRunner.PPM, 16 / ComputerScienceRunner.PPM, new Vector2(16 / ComputerScienceRunner.PPM, -24 / ComputerScienceRunner.PPM), 0);
        fixtureDef.shape = rightSensor;
        fixtureDef.friction = 0;
        fixtureDef.isSensor = true;

        fixtureDef.filter.categoryBits = ComputerScienceRunner.BUG_RIGHT_SENSOR_BIT;
        fixtureDef.filter.maskBits = ComputerScienceRunner.GROUND_BIT | ComputerScienceRunner.BRICK_BIT | ComputerScienceRunner.ECTS_BIT | ComputerScienceRunner.ECTS_BRICK_BIT | ComputerScienceRunner.HEART_BIT | ComputerScienceRunner.HEART_BRICK_BIT | ComputerScienceRunner.INFO_BRICK_BIT | ComputerScienceRunner.WALL_BIT | ComputerScienceRunner.COIN_BIT | ComputerScienceRunner.COIN_BRICK_BIT | ComputerScienceRunner.GOAL_BIT | ComputerScienceRunner.PLAYER_BIT | ComputerScienceRunner.ONEWAY_PLATFORM_BIT;

        body.createFixture(fixtureDef).setUserData(this);


    }

    public void createRunLeftAnimation() {
        Array<TextureRegion> frames = new Array<TextureRegion>();
        for(int i = 0; i < 4; i++) {
            frames.add(new TextureRegion(texture, i * 64, 0, spriteWidth, spriteHeight));
        }
        runLeftAnimation = new Animation(0.1f, frames);
        frames.clear();
    }

    public void createRunRightAnimation() {
        Array<TextureRegion> frames = new Array<TextureRegion>();
        for(int i = 0; i < 4; i++) {
            frames.add(new TextureRegion(texture, i * 64, 64,  spriteWidth, spriteHeight));
        }
        runRightAnimation = new Animation(0.1f, frames);
        frames.clear();
    }

    public void createStompedLeftAnimation() {
        Array<TextureRegion> frames = new Array<TextureRegion>();
        for(int i = 0; i < 1; i++) {
            frames.add(new TextureRegion(texture, i * 64, 128,  spriteWidth, spriteHeight));
        }
        stompedLeftAnimation = new Animation(0.1f, frames);
        frames.clear();
    }

    public void createStompedRightAnimation() {
        Array<TextureRegion> frames = new Array<TextureRegion>();
        for(int i = 0; i < 1; i++) {
            frames.add(new TextureRegion(texture, i * 64, 192,  spriteWidth, spriteHeight));
        }
        stompedRightAnimation = new Animation(0.1f, frames);
        frames.clear();
    }



    public void update(float dt) {
        stateTime = stateTime + dt;

        if(leftSensorCollides && !rightSensorCollides) {
            currentAnimation = runLeftAnimation;
        }

        if(!leftSensorCollides && rightSensorCollides) {
            currentAnimation = runRightAnimation;
        }

        if(currentAnimation == runLeftAnimation) {
            body.setLinearVelocity(-1, 0);
        }

        if(currentAnimation == runRightAnimation) {
            body.setLinearVelocity(1, 0);
        }

        currentFrame = currentAnimation.getKeyFrame(stateTime, true);

        x = body.getPosition().x;
        y = body.getPosition().y;

        if(setToDestroy && !destroyed) {
            ComputerScienceRunner.playScreen.getWorld().destroyBody(body);
            destroyed = true;
            stateTime = 0;
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
        if(currentAnimation == runLeftAnimation) {
            currentAnimation = stompedLeftAnimation;
        }

        if(currentAnimation == runRightAnimation) {
            currentAnimation = stompedRightAnimation;
        }
    }

    public boolean isDead() {
        return dead;
    }

    public void setCurrentAnimation(Animation<TextureRegion> currentAnimation) {
        this.currentAnimation = currentAnimation;
    }


    public PolygonShape getLeftSensor() {
        return leftSensor;
    }

    public PolygonShape getRightSensor() {
        return rightSensor;
    }

    public boolean isLeftSensorCollides() {
        return leftSensorCollides;
    }

    public boolean isRightSensorCollides() {
        return rightSensorCollides;
    }

    public void setLeftSensorCollides(boolean leftSensorCollides) {
        this.leftSensorCollides = leftSensorCollides;
    }

    public void setRightSensorCollides(boolean rightSensorCollides) {
        this.rightSensorCollides = rightSensorCollides;
    }
}
