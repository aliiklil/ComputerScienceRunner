package at.ac.univie.computersciencerunner.mobs;

import com.badlogic.gdx.Game;
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
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
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

    private Animation<TextureRegion> blankAnimation; //Shows nothing, this is needed for blinking when the player loses a heart
    private Animation<TextureRegion> beforeBlankAnimation; //Save which animation was before

    private Animation<TextureRegion> currentAnimation;

    private float stateTime;

    private TextureRegion currentFrame;

    public Body body;

    private boolean jumping; //True if player pressed jump button until he is grounded again, Not true if player falls down from somewhere
    private boolean grounded; //True, if the bottom of the player is currently touching something (ground, bricks etc.)
    private long timestampUngrounded; //Needed, because player should still be able to jump a short duration after he is ungrounded
    private long durationJumpPossible = 200; //Duration in millisceonds of how long player still can jump after ungrounded

    private int hearts; //How many hearts the player has. If he has 0 hearts, player is dead. Variable can be 0, 1, 2, 3

    private boolean dead; //True if player died

    private ComputerScienceRunner game;

    private boolean reachedGoal; //True when player reaches end of semester. Needed to know when to switch from PlayScreen to QuestionScreen

    private boolean blinking; //True when player just lost a heart because of touching a monster, he will blink
    private long blinkingStartTimestamp;
    private int blinkingDuration = 2000; //In milliseconds

    private boolean isTouchingEnemy;

    private boolean deathJumpStarted; //True, when the player has died and now does the death jump and falls down

    public Player(ComputerScienceRunner computerScienceRunner, World world) {

        this.game = computerScienceRunner;

        hearts = 3;

        createStandLeftAnimation();
        createStandRightAnimation();

        createRunLeftAnimation();
        createRunRightAnimation();

        createJumpLeftAnimation();
        createJumpRightAnimation();

        createBlankAnimation();

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
        fixtureDef.filter.maskBits = ComputerScienceRunner.GROUND_BIT | ComputerScienceRunner.BRICK_BIT | ComputerScienceRunner.ECTS_BIT | ComputerScienceRunner.ECTS_BRICK_BIT | ComputerScienceRunner.HEART_BIT | ComputerScienceRunner.HEART_BRICK_BIT | ComputerScienceRunner.INFO_BRICK_BIT | ComputerScienceRunner.WALL_BIT | ComputerScienceRunner.COIN_BIT | ComputerScienceRunner.COIN_BRICK_BIT | ComputerScienceRunner.GOAL_BIT | ComputerScienceRunner.BUG_HEAD_BIT | ComputerScienceRunner.BUG_BODY_BIT | ComputerScienceRunner.BUG_LEFT_SENSOR_BIT | ComputerScienceRunner.BUG_RIGHT_SENSOR_BIT;

        body.createFixture(fixtureDef).setUserData("head");




        CircleShape circle = new CircleShape();
        circle.setRadius(8 / ComputerScienceRunner.PPM);
        circle.setPosition(new Vector2(0, -16 / ComputerScienceRunner.PPM));
        fixtureDef.shape = circle;
        fixtureDef.friction = 0;

        fixtureDef.filter.categoryBits = ComputerScienceRunner.PLAYER_FEET_BIT;
        fixtureDef.filter.maskBits = ComputerScienceRunner.GROUND_BIT | ComputerScienceRunner.BRICK_BIT | ComputerScienceRunner.ECTS_BIT | ComputerScienceRunner.ECTS_BRICK_BIT | ComputerScienceRunner.HEART_BIT | ComputerScienceRunner.HEART_BRICK_BIT | ComputerScienceRunner.INFO_BRICK_BIT | ComputerScienceRunner.COIN_BIT | ComputerScienceRunner.COIN_BRICK_BIT | ComputerScienceRunner.GOAL_BIT | ComputerScienceRunner.ONEWAY_PLATFORM_BIT | ComputerScienceRunner.BUG_HEAD_BIT | ComputerScienceRunner.BUG_BODY_BIT;

        body.createFixture(fixtureDef).setUserData("feet");





        EdgeShape head = new EdgeShape();
        head.set(new Vector2(-7 / ComputerScienceRunner.PPM, 20 / ComputerScienceRunner.PPM), new Vector2(7 / ComputerScienceRunner.PPM, 20 / ComputerScienceRunner.PPM));

        fixtureDef.shape = head;
        fixtureDef.friction = 0;
        fixtureDef.isSensor = true;

        fixtureDef.filter.categoryBits = ComputerScienceRunner.PLAYER_HEAD_BIT;
        fixtureDef.filter.maskBits = ComputerScienceRunner.GROUND_BIT | ComputerScienceRunner.BRICK_BIT | ComputerScienceRunner.ECTS_BRICK_BIT | ComputerScienceRunner.HEART_BRICK_BIT | ComputerScienceRunner.INFO_BRICK_BIT | ComputerScienceRunner.COIN_BIT | ComputerScienceRunner.COIN_BRICK_BIT | ComputerScienceRunner.GOAL_BIT | ComputerScienceRunner.BUG_HEAD_BIT | ComputerScienceRunner.BUG_BODY_BIT;

        body.createFixture(fixtureDef).setUserData("head");


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

    public void createBlankAnimation() {
        Array<TextureRegion> frames = new Array<TextureRegion>();
        for(int i = 0; i < 1; i++) {
            frames.add(new TextureRegion(texture, 0, 448, 64, 64));
        }
        blankAnimation = new Animation(0.1f, frames);
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

        if(body.getPosition().y < -5) {
            dead = true;
        }


        if(isTouchingEnemy) {
            if (!blinking) {
                System.out.println("aaa");
                hearts--;
                ComputerScienceRunner.playScreen.getHud().setHeartsCount(hearts);
                blinking = true;
                blinkingStartTimestamp = System.currentTimeMillis();
                if (hearts == 0) {
                    currentAnimation = deathAnimation;
                }
            }
        }

        if(blinking) {
            if (System.currentTimeMillis() - blinkingStartTimestamp < blinkingDuration) {
                if ((System.currentTimeMillis() - blinkingStartTimestamp) % 400 > 200) {
                    currentFrame = blankAnimation.getKeyFrame(stateTime, false);
                } else {
                    if (currentAnimation == jumpLeftAnimation || currentAnimation == jumpRightAnimation || currentAnimation == deathAnimation) {
                        currentFrame = currentAnimation.getKeyFrame(stateTime, false);
                    } else {
                        currentFrame = currentAnimation.getKeyFrame(stateTime, true);
                    }
                }
            } else {
                blinking = false;
            }
        }

        if(currentAnimation == deathAnimation && !deathJumpStarted) {
            deathJumpStarted = true;
            Filter filter = new Filter();
            filter.maskBits = 0; //Nothing
            for(Fixture fixture : body.getFixtureList()) {
                fixture.setFilterData(filter);
            }
            body.setLinearVelocity(0, 0);
            body.applyLinearImpulse(new Vector2(0f, 6f), body.getWorldCenter(), true);
        }
    }

    public void handleInput(float dt) {

        if(!ComputerScienceRunner.playScreen.isPaused() && currentAnimation != deathAnimation) {

            if (Gdx.input.isKeyJustPressed(Input.Keys.UP) && !jumping) {
                if (grounded || System.currentTimeMillis() - timestampUngrounded < durationJumpPossible) {
                    body.setLinearVelocity(body.getLinearVelocity().x, 0);
                    body.applyLinearImpulse(new Vector2(0, 5.5f), body.getWorldCenter(), true); //5.5f normally
                    grounded = false;
                    jumping = true;
                    if (currentAnimation == runLeftAnimation || currentAnimation == standLeftAnimation) {
                        currentAnimation = jumpLeftAnimation;
                    }

                    if (currentAnimation == runRightAnimation || currentAnimation == standRightAnimation) {
                        currentAnimation = jumpRightAnimation;
                    }
                }
            }


            if (Gdx.input.isKeyPressed(Input.Keys.LEFT) && !Gdx.input.isKeyPressed(Input.Keys.RIGHT) && body.getLinearVelocity().x >= -3f) { // 2f normally
                body.applyLinearImpulse(new Vector2(-0.5f, 0), body.getWorldCenter(), true);
                if (currentAnimation != runLeftAnimation) {
                    stateTime = 0; //Starts animation from beginning again, not from where it left before
                }
                if (currentAnimation != jumpLeftAnimation) {
                    currentAnimation = runLeftAnimation;
                }
            }

            if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) && !Gdx.input.isKeyPressed(Input.Keys.LEFT) && body.getLinearVelocity().x <= 3f) {// 2f normally
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

        Vector3 worldCoordinates = new Vector3(body.getPosition().x, body.getPosition().y, 0);
        Vector3 screenCoordinates = camera.project(worldCoordinates);

        ComputerScienceRunner.batch.draw(currentFrame, screenCoordinates.x - 32, screenCoordinates.y - 24);

    }

    public void setGrounded(boolean grounded) {
        this.grounded = grounded;
    }

    public int getHearts() {
        return hearts;
    }

    public void setHearts(int hearts) {
        this.hearts = hearts;
    }

    public boolean isDead() {
        return dead;
    }

    public boolean isReachedGoal() {
        return reachedGoal;
    }

    public void setReachedGoal(boolean reachedGoal) {
        this.reachedGoal = reachedGoal;
    }

    public boolean isJumping() {
        return jumping;
    }

    public void setDead(boolean dead) {
        this.dead = dead;
    }

    public boolean isBlinking() {
        return blinking;
    }

    public void setBlinking(boolean blinking) {
        this.blinking = blinking;
    }

    public Animation<TextureRegion> getCurrentAnimation() {
        return currentAnimation;
    }

    public void setCurrentAnimation(Animation<TextureRegion> currentAnimation) {
        this.currentAnimation = currentAnimation;
    }

    public Animation<TextureRegion> getDeathAnimation() {
        return deathAnimation;
    }

    public boolean isTouchingEnemy() {
        return isTouchingEnemy;
    }

    public void setTouchingEnemy(boolean touchingEnemy) {
        isTouchingEnemy = touchingEnemy;
    }

    public long getTimestampUngrounded() {
        return timestampUngrounded;
    }

    public void setTimestampUngrounded(long timestampUngrounded) {
        this.timestampUngrounded = timestampUngrounded;
    }
}
