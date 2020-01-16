package at.ac.univie.computersciencerunner.mobs;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
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
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

import at.ac.univie.computersciencerunner.ComputerScienceRunner;
import at.ac.univie.computersciencerunner.SmartphoneController;

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

    private boolean brickDestroyed; //True if player just destroyed a brick. Needed so he can't destroy a brick and hold the jump button and get over the brick

    private boolean upKeyReleased; //Needed because when player is reading something because he hit an infoBrick before, he shouldnt jump immediatly when cancelling the infoWidget

    private boolean trampolineJump; //True when player jumps on a trampoline

    private boolean onMovingHorizontalPlatform;
    private boolean onMovingVerticalPlatform;

    private SmartphoneController smartphoneController;

    public Player(ComputerScienceRunner computerScienceRunner, World world) {

        this.game = computerScienceRunner;

        this.smartphoneController = smartphoneController;

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
        bodyDef.position.set(64 / ComputerScienceRunner.PPM, 200 / ComputerScienceRunner.PPM);

        body = world.createBody(bodyDef);
        FixtureDef fixtureDef = new FixtureDef();

        PolygonShape rectangle = new PolygonShape();
        rectangle.setAsBox(8 / ComputerScienceRunner.PPM, 20 / ComputerScienceRunner.PPM);
        fixtureDef.shape = rectangle;

        fixtureDef.filter.categoryBits = ComputerScienceRunner.PLAYER_BIT;
        fixtureDef.filter.maskBits = ComputerScienceRunner.GROUND_BIT | ComputerScienceRunner.BRICK_BIT | ComputerScienceRunner.COLLECTIBLE_BIT | ComputerScienceRunner.WALL_BIT | ComputerScienceRunner.GOAL_BIT | ComputerScienceRunner.BUG_HEAD_BIT | ComputerScienceRunner.BUG_BODY_BIT | ComputerScienceRunner.BUG_LEFT_SENSOR_BIT | ComputerScienceRunner.BUG_RIGHT_SENSOR_BIT | ComputerScienceRunner.SPIKES_BIT | ComputerScienceRunner.TRAMPOLINE_BIT | ComputerScienceRunner.SPIKES_BIT;

        body.createFixture(fixtureDef);




        CircleShape circle = new CircleShape();
        circle.setRadius(7 / ComputerScienceRunner.PPM);
        circle.setPosition(new Vector2(0, -16 / ComputerScienceRunner.PPM));
        fixtureDef.shape = circle;


        fixtureDef.filter.categoryBits = ComputerScienceRunner.PLAYER_FEET_BIT;
        fixtureDef.filter.maskBits = ComputerScienceRunner.GROUND_BIT | ComputerScienceRunner.BRICK_BIT | ComputerScienceRunner.GOAL_BIT | ComputerScienceRunner.ONEWAY_PLATFORM_BIT | ComputerScienceRunner.BUG_HEAD_BIT | ComputerScienceRunner.BUG_BODY_BIT | ComputerScienceRunner.SPIKES_BIT;

        body.createFixture(fixtureDef).setUserData("feet");





        EdgeShape head = new EdgeShape();
        head.set(new Vector2(-7 / ComputerScienceRunner.PPM, 20 / ComputerScienceRunner.PPM), new Vector2(7 / ComputerScienceRunner.PPM, 20 / ComputerScienceRunner.PPM));

        fixtureDef.shape = head;
        fixtureDef.isSensor = true;

        fixtureDef.filter.categoryBits = ComputerScienceRunner.PLAYER_HEAD_BIT;
        fixtureDef.filter.maskBits = ComputerScienceRunner.GROUND_BIT | ComputerScienceRunner.BRICK_BIT | ComputerScienceRunner.GOAL_BIT | ComputerScienceRunner.BUG_HEAD_BIT | ComputerScienceRunner.BUG_BODY_BIT | ComputerScienceRunner.SPIKES_BIT;

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


        //if(Gdx.app.getType() == Application.ApplicationType.Android) {
            handleSmartphoneInput(dt);
        //} else if (Gdx.app.getType() == Application.ApplicationType.Desktop) {
            //handleKeyboardInput(dt);
        //}

        if(body.getPosition().y < -5) {
            dead = true;
            ComputerScienceRunner.assetManager.get("audio/sounds/gameOver.mp3", Sound.class).play();
            ComputerScienceRunner.playScreen.getLevelMusic().stop();
        }


        if(isTouchingEnemy) {
            if (!blinking) {

                hearts--;
                ComputerScienceRunner.playScreen.getHud().setHeartsCount(hearts);
                blinking = true;
                blinkingStartTimestamp = System.currentTimeMillis();
                if (hearts == 0) {
                    currentAnimation = deathAnimation;
                }
                ComputerScienceRunner.assetManager.get("audio/sounds/damageTaken.mp3", Sound.class).play();
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
            ComputerScienceRunner.assetManager.get("audio/sounds/gameOver.mp3", Sound.class).play();
            ComputerScienceRunner.playScreen.getLevelMusic().stop();
        }

        if(trampolineJump) {
            body.applyLinearImpulse(new Vector2(0, 8.0f), body.getWorldCenter(), true);
            trampolineJump = false;

            if (currentAnimation == runLeftAnimation || currentAnimation == standLeftAnimation) {
                currentAnimation = jumpLeftAnimation;
            }
            if (currentAnimation == runRightAnimation || currentAnimation == standRightAnimation) {
                currentAnimation = jumpRightAnimation;
            }
            ComputerScienceRunner.assetManager.get("audio/sounds/trampoline.mp3", Sound.class).play();
        }
    }

    public void handleKeyboardInput(float dt) {

        if(ComputerScienceRunner.playScreen.getInfoWidget().isCurrentlyDisplayed() && Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
            ComputerScienceRunner.playScreen.getInfoWidget().setCurrentlyDisplayed(false);
            ComputerScienceRunner.playScreen.resume();
            ComputerScienceRunner.playScreen.getCustomOrthogonalTiledMapRenderer().setAnimate(true);
            upKeyReleased = false;
        }

        if(!Gdx.input.isKeyPressed(Input.Keys.UP)) {
            upKeyReleased = true;
        }


        if(!ComputerScienceRunner.playScreen.isPaused() && currentAnimation != deathAnimation) {

            if (upKeyReleased && Gdx.input.isKeyPressed(Input.Keys.UP) && !brickDestroyed && !jumping) {
                if (grounded || System.currentTimeMillis() - timestampUngrounded < durationJumpPossible) {
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
                    ComputerScienceRunner.assetManager.get("audio/sounds/jump.mp3", Sound.class).play();
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
            if(!onMovingHorizontalPlatform) {
                body.setLinearVelocity(0f, body.getLinearVelocity().y);
            }
        }

        if ((!Gdx.input.isKeyPressed(Input.Keys.RIGHT) && body.getLinearVelocity().x >= 0f) || ComputerScienceRunner.playScreen.isPaused()) {
            if(!onMovingHorizontalPlatform) {
                body.setLinearVelocity(0f, body.getLinearVelocity().y);
            }
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

    }

    public void handleSmartphoneInput(float dt) {

        smartphoneController = ComputerScienceRunner.playScreen.getSmartphoneController();

        if(ComputerScienceRunner.playScreen.getInfoWidget().isCurrentlyDisplayed() && smartphoneController.isUpJustPressed() && System.currentTimeMillis() - ComputerScienceRunner.playScreen.getInfoWidget().getInfoWidgetOpenedTimestamp() > 100) { //Player needs to look at infoWiget at least 0.1sec before closing. Otherwise he closes it unintentionally

            ComputerScienceRunner.playScreen.getInfoWidget().setCurrentlyDisplayed(false);
            ComputerScienceRunner.playScreen.resume();
            ComputerScienceRunner.playScreen.getCustomOrthogonalTiledMapRenderer().setAnimate(true);
            upKeyReleased = false;
        }

        if(smartphoneController.isUpPressed()) {
            upKeyReleased = true;
        }


        if(!ComputerScienceRunner.playScreen.isPaused() && currentAnimation != deathAnimation) {

            if (upKeyReleased && smartphoneController.isUpPressed() && !brickDestroyed && !jumping) {
                if (grounded || System.currentTimeMillis() - timestampUngrounded < durationJumpPossible) {
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
                    ComputerScienceRunner.assetManager.get("audio/sounds/jump.mp3", Sound.class).play();
                }
            }

            System.out.println(body.getPosition().y);

            if (smartphoneController.isLeftPressed() && !smartphoneController.isRightPressed() && body.getLinearVelocity().x >= -3f) {
                body.applyLinearImpulse(new Vector2(-0.5f, 0), body.getWorldCenter(), true);
                if (currentAnimation != runLeftAnimation) {
                    stateTime = 0; //Starts animation from beginning again, not from where it left before
                }
                if (currentAnimation != jumpLeftAnimation) {
                    currentAnimation = runLeftAnimation;
                }
            }

            if (smartphoneController.isRightPressed() && !smartphoneController.isLeftPressed() && body.getLinearVelocity().x <= 3f) {
                body.applyLinearImpulse(new Vector2(0.5f, 0), body.getWorldCenter(), true);
                if (currentAnimation != runRightAnimation) {
                    stateTime = 0;
                }
                if (currentAnimation != jumpRightAnimation)
                    currentAnimation = runRightAnimation;
            }

            if(body.getPosition().y < 0) {
                body.setLinearVelocity(0f, body.getLinearVelocity().y);
            }

            if (smartphoneController.isLeftPressed() && smartphoneController.isRightPressed()) {
                body.setLinearVelocity(0f, body.getLinearVelocity().y);

                if (currentAnimation == runRightAnimation) {
                    currentAnimation = standRightAnimation;
                }

                if (currentAnimation == runLeftAnimation) {
                    currentAnimation = standLeftAnimation;
                }

            }

        }

        if ((!smartphoneController.isLeftPressed() && body.getLinearVelocity().x <= 0f) || ComputerScienceRunner.playScreen.isPaused()) {
            if(!onMovingHorizontalPlatform) {
                body.setLinearVelocity(0f, body.getLinearVelocity().y);
            }
        }

        if ((!smartphoneController.isRightPressed() && body.getLinearVelocity().x >= 0f) || ComputerScienceRunner.playScreen.isPaused()) {
            if(!onMovingHorizontalPlatform) {
                body.setLinearVelocity(0f, body.getLinearVelocity().y);
            }
        }

        if ((!smartphoneController.isLeftPressed() && !smartphoneController.isRightPressed()) || ComputerScienceRunner.playScreen.isPaused()) {
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

    public void setBrickDestroyed(boolean brickDestroyed) {
        this.brickDestroyed = brickDestroyed;
    }

    public void setBlinkingStartTimestamp(long blinkingStartTimestamp) {
        this.blinkingStartTimestamp = blinkingStartTimestamp;
    }

    public int getSpriteWidth() {
        return spriteWidth;
    }

    public Animation<TextureRegion> getJumpLeftAnimation() {
        return jumpLeftAnimation;
    }

    public Animation<TextureRegion> getJumpRightAnimation() {
        return jumpRightAnimation;
    }

    public boolean isGrounded() {
        return grounded;
    }

    public boolean isTrampolineJump() {
        return trampolineJump;
    }

    public void setTrampolineJump(boolean trampolineJump) {
        this.trampolineJump = trampolineJump;
    }


    public boolean isOnMovingHorizontalPlatform() {
        return onMovingHorizontalPlatform;
    }

    public void setOnMovingHorizontalPlatform(boolean onMovingHorizontalPlatform) {
        this.onMovingHorizontalPlatform = onMovingHorizontalPlatform;
    }


    public boolean isOnMovingVerticalPlatform() {
        return onMovingVerticalPlatform;
    }

    public void setOnMovingVerticalPlatform(boolean onMovingVerticalPlatform) {
        this.onMovingVerticalPlatform = onMovingVerticalPlatform;
    }
}
