package at.ac.univie.computersciencerunner.mobs;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
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

    private boolean doubleJumpUsed; //True if player already has double-jumped

    private boolean jumping; //True if player pressed jump button until he is grounded again, Not true if player falls down from somewhere
    private boolean grounded; //True, if the bottom of the player is currently touching something (ground, bricks etc.)

    private long ungroundedTimestamp; //Timestamp when grounded goes from true to false, needed because player should still be able to jump even when he walked off a cliff

    private int hearts; //How many hearts the player has. If he has 0 hearts, player is dead. Variable can be 0, 1, 2, 3

    public Player(World world) {

        hearts = 3;

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
        bodyDef.position.set(64 / ComputerScienceRunner.PPM, 32 / ComputerScienceRunner.PPM);

        body = world.createBody(bodyDef);
        FixtureDef fixtureDef = new FixtureDef();

        PolygonShape rectangle = new PolygonShape();
        rectangle.setAsBox(8 / ComputerScienceRunner.PPM, 20 / ComputerScienceRunner.PPM);
        fixtureDef.shape = rectangle;
        fixtureDef.friction = 0;

        fixtureDef.filter.categoryBits = ComputerScienceRunner.PLAYER_BIT;
        fixtureDef.filter.maskBits = ComputerScienceRunner.GROUND_BIT | ComputerScienceRunner.BRICK_BIT | ComputerScienceRunner.ECTS_BIT | ComputerScienceRunner.ECTS_BRICK_BIT | ComputerScienceRunner.HEART_BIT | ComputerScienceRunner.HEART_BRICK_BIT | ComputerScienceRunner.INFO_BRICK_BIT | ComputerScienceRunner.WALL_BIT;

        body.createFixture(fixtureDef).setUserData("head");




        CircleShape circle = new CircleShape();
        circle.setRadius(8 / ComputerScienceRunner.PPM);
        circle.setPosition(new Vector2(0, -16 / ComputerScienceRunner.PPM));
        fixtureDef.shape = circle;
        fixtureDef.friction = 0;

        fixtureDef.filter.categoryBits = ComputerScienceRunner.PLAYER_BIT;
        fixtureDef.filter.maskBits = ComputerScienceRunner.GROUND_BIT | ComputerScienceRunner.BRICK_BIT | ComputerScienceRunner.ECTS_BIT | ComputerScienceRunner.ECTS_BRICK_BIT | ComputerScienceRunner.HEART_BIT | ComputerScienceRunner.HEART_BRICK_BIT | ComputerScienceRunner.INFO_BRICK_BIT;

        body.createFixture(fixtureDef);





        EdgeShape head = new EdgeShape();
        head.set(new Vector2(-7 / ComputerScienceRunner.PPM, 20 / ComputerScienceRunner.PPM), new Vector2(7 / ComputerScienceRunner.PPM, 20 / ComputerScienceRunner.PPM));

        fixtureDef.shape = head;
        fixtureDef.friction = 0;
        fixtureDef.isSensor = true;

        fixtureDef.filter.categoryBits = ComputerScienceRunner.PLAYER_HEAD_BIT;
        fixtureDef.filter.maskBits = ComputerScienceRunner.GROUND_BIT | ComputerScienceRunner.BRICK_BIT | ComputerScienceRunner.ECTS_BRICK_BIT | ComputerScienceRunner.HEART_BRICK_BIT | ComputerScienceRunner.INFO_BRICK_BIT;

        body.createFixture(fixtureDef).setUserData("head");






        EdgeShape feet = new EdgeShape();
        feet.set(new Vector2(-6 / ComputerScienceRunner.PPM, -24 / ComputerScienceRunner.PPM), new Vector2(6 / ComputerScienceRunner.PPM, -24 / ComputerScienceRunner.PPM));

        fixtureDef.shape = feet;
        fixtureDef.friction = 0;
        fixtureDef.isSensor = true;

        fixtureDef.filter.categoryBits = ComputerScienceRunner.PLAYER_FEET_BIT;
        fixtureDef.filter.maskBits = ComputerScienceRunner.GROUND_BIT | ComputerScienceRunner.BRICK_BIT | ComputerScienceRunner.ECTS_BRICK_BIT | ComputerScienceRunner.HEART_BRICK_BIT | ComputerScienceRunner.INFO_BRICK_BIT;

        body.createFixture(fixtureDef).setUserData("feet");



    }

    public void createStandLeftAnimation() {
        Array<TextureRegion> frames = new Array<TextureRegion>();
        for(int i = 0; i < 1; i++) {
            frames.add(new TextureRegion(texture, i * 64, 0, 64, 64));
        }
        standLeftAnimation = new Animation(5f, frames);
        frames.clear();
    }

    public void createStandRightAnimation() {
        Array<TextureRegion> frames = new Array<TextureRegion>();
        for(int i = 0; i < 1; i++) {
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
        for(int i = 0; i < 1; i++) {
            frames.add(new TextureRegion(texture, i * 64, 256, 64, 64));
        }
        jumpLeftAnimation = new Animation(0.1f, frames);
        frames.clear();
    }

    public void createJumpRightAnimation() {
        Array<TextureRegion> frames = new Array<TextureRegion>();
        for(int i = 0; i < 1; i++) {
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

        if(!ComputerScienceRunner.playScreen.isPaused()) {

            if (Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
                if (grounded) {
                    body.setLinearVelocity(body.getLinearVelocity().x, 0);
                    body.applyLinearImpulse(new Vector2(0, 5.5f), body.getWorldCenter(), true);
                    grounded = false;
                    jumping = true;
                    if (currentAnimation == runLeftAnimation || currentAnimation == standLeftAnimation) {
                        currentAnimation = jumpLeftAnimation;
                    }

                    if (currentAnimation == runRightAnimation || currentAnimation == standRightAnimation) {
                        currentAnimation = jumpRightAnimation;
                    }
                } else if (!grounded && !doubleJumpUsed) {
                    body.setLinearVelocity(body.getLinearVelocity().x, 0);
                    body.applyLinearImpulse(new Vector2(0, 4f), body.getWorldCenter(), true);
                    if (jumping) {
                        doubleJumpUsed = true;
                    }
                    jumping = true;
                    if (currentAnimation == runLeftAnimation || currentAnimation == standLeftAnimation) {
                        currentAnimation = jumpLeftAnimation;
                    }
                    if (currentAnimation == runRightAnimation || currentAnimation == standRightAnimation) {
                        currentAnimation = jumpRightAnimation;
                    }
                }
            }


            if (Gdx.input.isKeyPressed(Input.Keys.LEFT) && !Gdx.input.isKeyPressed(Input.Keys.RIGHT) && body.getLinearVelocity().x >= -2) {
                body.applyLinearImpulse(new Vector2(-0.5f, 0), body.getWorldCenter(), true);
                if (currentAnimation != runLeftAnimation) {
                    stateTime = 0; //Starts animation from beginning again, not from where it left before
                }
                if (currentAnimation != jumpLeftAnimation) {
                    currentAnimation = runLeftAnimation;
                }
            }

            if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) && !Gdx.input.isKeyPressed(Input.Keys.LEFT) && body.getLinearVelocity().x <= 2) {
                body.applyLinearImpulse(new Vector2(0.5f, 0), body.getWorldCenter(), true);
                if (currentAnimation != runRightAnimation) {
                    stateTime = 0;
                }
                if (currentAnimation != jumpRightAnimation)
                    currentAnimation = runRightAnimation;
            }


            if ((Gdx.input.isKeyPressed(Input.Keys.LEFT) && Gdx.input.isKeyPressed(Input.Keys.RIGHT))) {
                body.setLinearVelocity(0f, body.getLinearVelocity().y);

                if (currentAnimation == runRightAnimation) {
                    currentAnimation = standRightAnimation;
                }

                if (currentAnimation == runLeftAnimation) {
                    currentAnimation = standLeftAnimation;
                }

            }

        }

        if ((!Gdx.input.isKeyPressed(Input.Keys.LEFT) && body.getLinearVelocity().x <= 0f) || ComputerScienceRunner.playScreen.isPaused()) {
            body.setLinearVelocity(0f, body.getLinearVelocity().y);
        }

        if ((!Gdx.input.isKeyPressed(Input.Keys.RIGHT) && body.getLinearVelocity().x >= 0f) || ComputerScienceRunner.playScreen.isPaused()) {
            body.setLinearVelocity(0f, body.getLinearVelocity().y);
        }

        if ((!Gdx.input.isKeyPressed(Input.Keys.LEFT) && !Gdx.input.isKeyPressed(Input.Keys.RIGHT)) || ComputerScienceRunner.playScreen.isPaused()) {
            if (currentAnimation == runLeftAnimation) {
                currentAnimation = standLeftAnimation;
            }
            if (currentAnimation == runRightAnimation) {
                currentAnimation = standRightAnimation;
            }
        }

        if(grounded) {
            doubleJumpUsed = false;
            jumping = false;
            if(body.getLinearVelocity().x == 0) {
                if (currentAnimation == jumpLeftAnimation) {
                    currentAnimation = standLeftAnimation;
                }
                if (currentAnimation == jumpRightAnimation) {
                    currentAnimation = standRightAnimation;
                }
            } else {
                if (currentAnimation == jumpLeftAnimation) {
                    currentAnimation = runLeftAnimation;
                    stateTime = 0;
                }
                if (currentAnimation == jumpRightAnimation) {
                    currentAnimation = runRightAnimation;
                    stateTime = 0;
                }
            }
        } else {
            if(jumping) {
                if (body.getLinearVelocity().x > 0) {
                    currentAnimation = jumpRightAnimation;
                } else if (body.getLinearVelocity().x < 0) {
                    currentAnimation = jumpLeftAnimation;
                } else {
                    if (currentAnimation == standRightAnimation) {
                        currentAnimation = jumpRightAnimation;
                    }
                    if (currentAnimation == standLeftAnimation) {
                        currentAnimation = jumpLeftAnimation;
                    }
                }
            }
        }

        if(ComputerScienceRunner.playScreen.getInfoWidget().isCurrentlyDisplayed() && Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
            ComputerScienceRunner.playScreen.getInfoWidget().setCurrentlyDisplayed(false);
            ComputerScienceRunner.playScreen.resume();
            ComputerScienceRunner.playScreen.getCustomOrthogonalTiledMapRenderer().setAnimate(true);
        }

    }

    public void draw() {

        Camera camera = ComputerScienceRunner.playScreen.getCamera();
        if(camera.position.x == 6.4f) {
            ComputerScienceRunner.batch.draw(currentFrame, (body.getPosition().x * ComputerScienceRunner.PPM) - 32, (body.getPosition().y * ComputerScienceRunner.PPM) - 24);
        } else {
            ComputerScienceRunner.batch.draw(currentFrame, ComputerScienceRunner.WIDTH / 2 - spriteWidth / 2, body.getPosition().y * ComputerScienceRunner.PPM - 24);
        }
    }

    public void setGrounded(boolean grounded) {
        this.grounded = grounded;
        ungroundedTimestamp = System.currentTimeMillis();
    }

    public int getHearts() {
        return hearts;
    }

    public void setHearts(int hearts) {
        this.hearts = hearts;
        ComputerScienceRunner.playScreen.getHud().setHeartsCount(hearts);
    }
}
