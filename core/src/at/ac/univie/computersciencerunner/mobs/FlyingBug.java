package at.ac.univie.computersciencerunner.mobs;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
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

import at.ac.univie.computersciencerunner.ComputerScienceRunner;

public class FlyingBug {

    private float x;
    private float y;

    private int spriteWidth = 64;
    private int spriteHeight = 64;

    private Texture texture = new Texture(Gdx.files.internal("enemies/flyingBug.png"));

    private Animation<TextureRegion> flyLeftAnimation;
    private Animation<TextureRegion> flyRightAnimation;

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

    private float startingPositionX;
    private float startingPositionY;

    public FlyingBug(ComputerScienceRunner computerScienceRunner, World world, float x, float y) {

        startingPositionX = x / ComputerScienceRunner.PPM;
        startingPositionY = y / ComputerScienceRunner.PPM;

        this.game = computerScienceRunner;

        createRunLeftAnimation();
        createRunRightAnimation();

        createStompedLeftAnimation();
        createStompedRightAnimation();

        currentAnimation = flyRightAnimation;

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.KinematicBody;
        bodyDef.position.set(x / ComputerScienceRunner.PPM, y / ComputerScienceRunner.PPM);
        bodyDef.linearDamping = 1.0f;

        body = world.createBody(bodyDef);
        FixtureDef fixtureDef = new FixtureDef();







        PolygonShape bugBody = new PolygonShape();
        bugBody.setAsBox(20 / ComputerScienceRunner.PPM, 8 / ComputerScienceRunner.PPM, new Vector2(0 / ComputerScienceRunner.PPM, -16 / ComputerScienceRunner.PPM), 0);
        fixtureDef.shape = bugBody;
        fixtureDef.friction = 0;

        fixtureDef.filter.categoryBits = ComputerScienceRunner.BUG_BODY_BIT;
        fixtureDef.filter.maskBits = ComputerScienceRunner.GROUND_BIT | ComputerScienceRunner.WALL_BIT | ComputerScienceRunner.GOAL_BIT | ComputerScienceRunner.ONEWAY_PLATFORM_BIT | ComputerScienceRunner.PLAYER_BIT;

        body.createFixture(fixtureDef).setUserData(this);





        EdgeShape head = new EdgeShape();
        head.set(new Vector2(-20 / ComputerScienceRunner.PPM, 0 / ComputerScienceRunner.PPM), new Vector2(20 / ComputerScienceRunner.PPM, 0 / ComputerScienceRunner.PPM));

        fixtureDef.shape = head;
        fixtureDef.friction = 0;
        fixtureDef.restitution = 0.8f;

        fixtureDef.filter.categoryBits = ComputerScienceRunner.BUG_HEAD_BIT;
        fixtureDef.filter.maskBits = ComputerScienceRunner.GROUND_BIT | ComputerScienceRunner.GOAL_BIT | ComputerScienceRunner.ONEWAY_PLATFORM_BIT | ComputerScienceRunner.PLAYER_BIT | ComputerScienceRunner.PLAYER_FEET_BIT;

        body.createFixture(fixtureDef).setUserData(this);


    }

    public void createRunLeftAnimation() {
        Array<TextureRegion> frames = new Array<TextureRegion>();
        for(int i = 0; i < 4; i++) {
            frames.add(new TextureRegion(texture, i * 64, 0, spriteWidth, spriteHeight));
        }
        flyLeftAnimation = new Animation(0.1f, frames);
        frames.clear();
    }

    public void createRunRightAnimation() {
        Array<TextureRegion> frames = new Array<TextureRegion>();
        for(int i = 0; i < 4; i++) {
            frames.add(new TextureRegion(texture, i * 64, 64,  spriteWidth, spriteHeight));
        }
        flyRightAnimation = new Animation(0.1f, frames);
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
            currentAnimation = flyLeftAnimation;
        }

        if(!leftSensorCollides && rightSensorCollides) {
            currentAnimation = flyRightAnimation;
        }

        if(currentAnimation == flyRightAnimation && body.getPosition().x - startingPositionX >= 256 / ComputerScienceRunner.PPM) {
            currentAnimation = flyLeftAnimation;
        } else if(currentAnimation == flyLeftAnimation && startingPositionX == body.getPosition().x) {
            currentAnimation = flyRightAnimation;
        }

        if(currentAnimation == flyLeftAnimation) {
            body.setLinearVelocity(-1, 0);
        }

        if(currentAnimation == flyRightAnimation) {
            body.setLinearVelocity(1, 0);
        }

        currentFrame = currentAnimation.getKeyFrame(stateTime, true);

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

    }

    public void draw() {

        if(!destroyed || stateTime < 1) {
            Sprite sprite = new Sprite(currentFrame);

            Camera camera = ComputerScienceRunner.playScreen.getCamera();
            System.out.println(camera.position.x);

            sprite.setPosition(body.getPosition().x * ComputerScienceRunner.PPM - 32 - camera.position.x * ComputerScienceRunner.PPM + ComputerScienceRunner.WIDTH/2, body.getPosition().y * ComputerScienceRunner.PPM - 24);

            sprite.draw(ComputerScienceRunner.batch);
        }
    }

    public void hitOnHead() {
        setToDestroy = true;
        if(currentAnimation == flyLeftAnimation) {
            currentAnimation = stompedLeftAnimation;
        }

        if(currentAnimation == flyRightAnimation) {
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
