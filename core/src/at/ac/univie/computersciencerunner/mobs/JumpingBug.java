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

public class JumpingBug {

    private float x;
    private float y;

    private int spriteWidth = 64;
    private int spriteHeight = 64;

    private Texture texture = new Texture(Gdx.files.internal("enemies/jumpingBug.png"));

    private Animation<TextureRegion> standAnimation;
    private Animation<TextureRegion> jumpAnimation;
    private Animation<TextureRegion> stompedAnimation;

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

    private boolean jumping;
    private long timestampLastJump;
    private int durationBetweenJumps = 3000; //Jumps up all 5 seconds

    private float startPositionX;

    public JumpingBug(ComputerScienceRunner computerScienceRunner, World world, float x, float y) {

        startPositionX = x;

        this.game = computerScienceRunner;

        createStandAnimation();
        createJumpAnimation();
        createStompedAnimation();

        currentAnimation = standAnimation;

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




        CircleShape circle = new CircleShape();
        circle.setRadius(8 / ComputerScienceRunner.PPM);
        circle.setPosition(new Vector2(0, -16 / ComputerScienceRunner.PPM));
        fixtureDef.shape = circle;
        fixtureDef.friction = 0;

        fixtureDef.filter.categoryBits = ComputerScienceRunner.BUG_BODY_BIT;
        fixtureDef.filter.maskBits = ComputerScienceRunner.GROUND_BIT |  ComputerScienceRunner.GOAL_BIT | ComputerScienceRunner.ONEWAY_PLATFORM_BIT | ComputerScienceRunner.PLAYER_BIT;

        body.createFixture(fixtureDef);





        EdgeShape head = new EdgeShape();
        head.set(new Vector2(-12 / ComputerScienceRunner.PPM, 30 / ComputerScienceRunner.PPM), new Vector2(12 / ComputerScienceRunner.PPM, 30 / ComputerScienceRunner.PPM));

        fixtureDef.shape = head;
        fixtureDef.friction = 0;
        fixtureDef.restitution = 0.8f;

        fixtureDef.filter.categoryBits = ComputerScienceRunner.BUG_HEAD_BIT;
        fixtureDef.filter.maskBits = ComputerScienceRunner.GROUND_BIT | ComputerScienceRunner.GOAL_BIT | ComputerScienceRunner.ONEWAY_PLATFORM_BIT | ComputerScienceRunner.PLAYER_FEET_BIT | ComputerScienceRunner.PLAYER_BIT | ComputerScienceRunner.PLAYER_HEAD_BIT;

        body.createFixture(fixtureDef).setUserData(this);

    }

    public void createStandAnimation() {
        Array<TextureRegion> frames = new Array<TextureRegion>();
        for(int i = 0; i < 1; i++) {
            frames.add(new TextureRegion(texture, 0, 0, spriteWidth, spriteHeight));
        }
        standAnimation = new Animation(0.1f, frames);
        frames.clear();
    }

    public void createJumpAnimation() {
        Array<TextureRegion> frames = new Array<TextureRegion>();
        for(int i = 0; i < 1; i++) {
            frames.add(new TextureRegion(texture, 0, 64,  spriteWidth, spriteHeight));
        }
        jumpAnimation = new Animation(0.1f, frames);
        frames.clear();
    }

    public void createStompedAnimation() {
        Array<TextureRegion> frames = new Array<TextureRegion>();
        for(int i = 0; i < 1; i++) {
            frames.add(new TextureRegion(texture, 0, 128,  spriteWidth, spriteHeight));
        }
        stompedAnimation = new Animation(0.1f, frames);
        frames.clear();
    }


    public void update(float dt) {
        stateTime = stateTime + dt;

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

        if(!setToDestroy) {
            if (!jumping && System.currentTimeMillis() - timestampLastJump > durationBetweenJumps) {
                jumping = true;
                body.applyLinearImpulse(new Vector2(0, 6000f), body.getWorldCenter(), true);
                currentAnimation = jumpAnimation;
                timestampLastJump = System.currentTimeMillis();
            }

            if (body.getLinearVelocity().y == 0) {
                jumping = false;
                currentAnimation = standAnimation;
            }
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
        currentAnimation = stompedAnimation;
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

    public boolean isJumping() {
        return jumping;
    }
}
