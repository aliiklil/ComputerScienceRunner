package at.ac.univie.computersciencerunner.mobs;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

import at.ac.univie.computersciencerunner.ComputerScienceRunner;

public class Spear {

    private int spriteWidth = 64;
    private int spriteHeight = 64;

    private float x;
    private float y;

    private Texture texture = new Texture(Gdx.files.internal("spear.png"));

    private Animation<TextureRegion> flyLeftAnimation;
    private Animation<TextureRegion> flyRightAnimation;

    private Animation<TextureRegion> currentAnimation;

    private float stateTime;

    private TextureRegion currentFrame;

    public Body body;

    private ComputerScienceRunner game;

    private boolean setToDestroy;
    private boolean destroyed;

    private long timestampThrowAnimationBegins;
    private long durationBetweenThrows = 2000;

    private boolean throwLeft; //True if player should throw spear to the left (because player is there)
    private boolean throwRight; //True if player should throw spear to the right (because player is there)

    public Spear(ComputerScienceRunner computerScienceRunner, World world, float x, float y, int direction) {

        this.game = computerScienceRunner;

        createFlyLeftAnimation();
        createFlyRightAnimation();

        if(direction == 0) {
            currentAnimation = flyLeftAnimation;
        } else {
            currentAnimation = flyRightAnimation;
        }

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.KinematicBody;
        bodyDef.position.set(x, y);
        bodyDef.linearDamping = 1.0f;

        body = world.createBody(bodyDef);
        FixtureDef fixtureDef = new FixtureDef();



        PolygonShape spearBody = new PolygonShape();
        spearBody.setAsBox(32 / ComputerScienceRunner.PPM, 5 / ComputerScienceRunner.PPM, new Vector2(0 / ComputerScienceRunner.PPM, 8/ ComputerScienceRunner.PPM), 0);
        fixtureDef.shape = spearBody;
        fixtureDef.friction = 0;

        fixtureDef.filter.categoryBits = ComputerScienceRunner.BUG_BODY_BIT; //Can be seen like a bug in collision filtering, so we don't need to use an extra variable only for spears
        fixtureDef.filter.maskBits = ComputerScienceRunner.GROUND_BIT | ComputerScienceRunner.WALL_BIT | ComputerScienceRunner.GOAL_BIT | ComputerScienceRunner.ONEWAY_PLATFORM_BIT | ComputerScienceRunner.PLAYER_BIT;

        body.createFixture(fixtureDef).setUserData(this);



        EdgeShape head = new EdgeShape();
        head.set(new Vector2(-28 / ComputerScienceRunner.PPM, 16 / ComputerScienceRunner.PPM), new Vector2(28 / ComputerScienceRunner.PPM, 16 / ComputerScienceRunner.PPM));

        fixtureDef.shape = head;
        fixtureDef.friction = 0;
        fixtureDef.restitution = 0.8f;

        fixtureDef.filter.categoryBits = ComputerScienceRunner.BUG_HEAD_BIT;
        fixtureDef.filter.maskBits = ComputerScienceRunner.GROUND_BIT | ComputerScienceRunner.GOAL_BIT | ComputerScienceRunner.ONEWAY_PLATFORM_BIT | ComputerScienceRunner.PLAYER_FEET_BIT | ComputerScienceRunner.PLAYER_BIT | ComputerScienceRunner.PLAYER_HEAD_BIT;

        body.createFixture(fixtureDef).setUserData(this);


    }

    public void createFlyLeftAnimation() {
        Array<TextureRegion> frames = new Array<TextureRegion>();
        for(int i = 0; i < 1; i++) {
            frames.add(new TextureRegion(texture, 0, 0, spriteWidth, spriteHeight));
        }
        flyLeftAnimation = new Animation(0.1f, frames);
        frames.clear();
    }

    public void createFlyRightAnimation() {
        Array<TextureRegion> frames = new Array<TextureRegion>();
        for(int i = 0; i < 1; i++) {
            frames.add(new TextureRegion(texture, 0, 64, spriteWidth, spriteHeight));
        }
        flyRightAnimation = new Animation(0.1f, frames);
        frames.clear();
    }

    public void update(float dt) {
        stateTime = stateTime + dt;

        currentFrame = currentAnimation.getKeyFrame(stateTime, true);


        if(!setToDestroy) {
            x = body.getPosition().x;
            y = body.getPosition().y;
        }

        if(currentAnimation == flyLeftAnimation) {
            body.setLinearVelocity(-2, 0);
        }

        if(currentAnimation == flyRightAnimation) {
            body.setLinearVelocity(2, 0);
        }

        if(setToDestroy && !destroyed) {
            ComputerScienceRunner.playScreen.getWorld().destroyBody(body);
            ComputerScienceRunner.playScreen.getSpearRemoveList().add(this);
            destroyed = true;
            stateTime = 0;
        }

    }

    public void draw() {

        if(!destroyed || stateTime < 0.1) {
            Sprite sprite = new Sprite(currentFrame);

            Camera camera = ComputerScienceRunner.playScreen.getCamera();

            sprite.setPosition(body.getPosition().x * ComputerScienceRunner.PPM - 32 - camera.position.x * ComputerScienceRunner.PPM + ComputerScienceRunner.WIDTH/2, body.getPosition().y * ComputerScienceRunner.PPM - 24);

            sprite.draw(ComputerScienceRunner.batch);
        }
    }

    public void hitOnHead() {
        setToDestroy = true;
    }

    public void setCurrentAnimation(Animation<TextureRegion> currentAnimation) {
        this.currentAnimation = currentAnimation;
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

}
