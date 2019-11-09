package at.ac.univie.computersciencerunner.mobs;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

import at.ac.univie.computersciencerunner.ComputerScienceRunner;

public class Player {

    private int spriteWidth = 64;
    private int spriteHeight = 64;

    private Texture texture = new Texture(Gdx.files.internal("player.png"));

    private Animation<TextureRegion> standLeftAnimation;
    private Animation<TextureRegion> standRightAnimation;

    private Animation<TextureRegion> runLeftAnimation;
    private Animation<TextureRegion> runRightAnimation;

    private Animation<TextureRegion> jumpLeftAnimation;
    private Animation<TextureRegion> jumpRightAnimation;

    private Animation<TextureRegion> deathAnimation;

    private Animation<TextureRegion> currentAnimation;

    private float stateTime;

    private TextureRegion currentFrame;

    public Body body;

    public Player(World world) {

        createStandLeftAnimation();
        createStandRightAnimation();

        createRunLeftAnimation();
        createRunRightAnimation();

        createJumpLeftAnimation();
        createJumpRightAnimation();

        createDeathAnimation();

        currentAnimation = standRightAnimation;

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(32 / ComputerScienceRunner.PPM, 32 / ComputerScienceRunner.PPM);

        body = world.createBody(bodyDef);
        FixtureDef fixtureDef = new FixtureDef();

        PolygonShape rectangle = new PolygonShape();
        rectangle.setAsBox(8 / ComputerScienceRunner.PPM, 20 / ComputerScienceRunner.PPM);
        fixtureDef.shape = rectangle;
        body.createFixture(fixtureDef);

        CircleShape circle = new CircleShape();

        circle.setRadius(8 / ComputerScienceRunner.PPM);
        circle.setPosition(new Vector2(0, -16 / ComputerScienceRunner.PPM));
        fixtureDef.shape = circle;
        body.createFixture(fixtureDef);

    }

    public void createStandLeftAnimation() {
        Array<TextureRegion> frames = new Array<TextureRegion>();
        for(int i = 0; i < 2; i++) {
            frames.add(new TextureRegion(texture, i * 64, 0, 64, 64));
        }
        standLeftAnimation = new Animation(5f, frames);
        frames.clear();
    }

    public void createStandRightAnimation() {
        Array<TextureRegion> frames = new Array<TextureRegion>();
        for(int i = 0; i < 2; i++) {
            frames.add(new TextureRegion(texture, i * 64, 64, 64, 64));
        }
        standRightAnimation = new Animation(5f, frames);
        frames.clear();
    }

    public void createRunLeftAnimation() {
        Array<TextureRegion> frames = new Array<TextureRegion>();
        for(int i = 0; i < 4; i++) {
            frames.add(new TextureRegion(texture, i * 64, 128, 64, 64));
        }
        runLeftAnimation = new Animation(0.1f, frames);
        frames.clear();
    }

    public void createRunRightAnimation() {
        Array<TextureRegion> frames = new Array<TextureRegion>();
        for(int i = 0; i < 4; i++) {
            frames.add(new TextureRegion(texture, i * 64, 192, 64, 64));
        }
        runRightAnimation = new Animation(0.1f, frames);
        frames.clear();
    }

    public void createJumpLeftAnimation() {
        Array<TextureRegion> frames = new Array<TextureRegion>();
        for(int i = 0; i < 4; i++) {
            frames.add(new TextureRegion(texture, i * 64, 256, 64, 64));
        }
        jumpLeftAnimation = new Animation(0.1f, frames);
        frames.clear();
    }

    public void createJumpRightAnimation() {
        Array<TextureRegion> frames = new Array<TextureRegion>();
        for(int i = 0; i < 4; i++) {
            frames.add(new TextureRegion(texture, i * 64, 320, 64, 64));
        }
        jumpRightAnimation = new Animation(0.1f, frames);
        frames.clear();
    }

    public void createDeathAnimation() {
        Array<TextureRegion> frames = new Array<TextureRegion>();
        for(int i = 0; i < 4; i++) {
            frames.add(new TextureRegion(texture, i * 64, 384, 64, 64));
        }
        deathAnimation = new Animation(0.1f, frames);
        frames.clear();
    }

    public void update(float dt) {
        stateTime = stateTime + dt;

        if(currentAnimation == jumpLeftAnimation || currentAnimation == jumpRightAnimation || currentAnimation == deathAnimation) {
            currentFrame = currentAnimation.getKeyFrame(stateTime, false);
        } else {
            currentFrame = currentAnimation.getKeyFrame(stateTime, true);
        }

        handleInput(dt);
    }

    public void handleInput(float dt) {

        if(Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
            body.applyLinearImpulse(new Vector2(0, 5f), body.getWorldCenter(), true);
        }

        if(Gdx.input.isKeyPressed(Input.Keys.LEFT) && body.getLinearVelocity().x >= -2) {
            body.applyLinearImpulse(new Vector2(-0.5f, 0), body.getWorldCenter(), true);
            if(currentAnimation != runLeftAnimation) {
                stateTime = 0; //Starts animation from beginning again, not from where it left before
            }
            currentAnimation = runLeftAnimation;
        }

        if(Gdx.input.isKeyPressed(Input.Keys.RIGHT) && body.getLinearVelocity().x <= 2) {
            body.applyLinearImpulse(new Vector2(0.5f, 0), body.getWorldCenter(), true);
            if(currentAnimation != runRightAnimation) {
                stateTime = 0;
            }
            currentAnimation = runRightAnimation;
        }


        if(!Gdx.input.isKeyPressed(Input.Keys.LEFT) && body.getLinearVelocity().x <= 0f) {
            body.setLinearVelocity(0f, body.getLinearVelocity().y);
        }

        if(!Gdx.input.isKeyPressed(Input.Keys.RIGHT) && body.getLinearVelocity().x >= 0f) {
            body.setLinearVelocity(0f, body.getLinearVelocity().y);
        }



        if(!Gdx.input.isKeyPressed(Input.Keys.LEFT) && !Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            if(currentAnimation == runLeftAnimation) {
                currentAnimation = standLeftAnimation;
                stateTime = 0;
            }
            if(currentAnimation == runRightAnimation) {
                currentAnimation = standRightAnimation;
                stateTime = 0;
            }
        }

    }

    public void draw() {
        ComputerScienceRunner.batch.draw(currentFrame, ComputerScienceRunner.WIDTH / 2 - spriteWidth / 2, body.getPosition().y * ComputerScienceRunner.PPM - 24);
    }

}
