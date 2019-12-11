package at.ac.univie.computersciencerunner.mapObjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

import at.ac.univie.computersciencerunner.ComputerScienceRunner;
import at.ac.univie.computersciencerunner.mobs.Player;

public class VerticalPlatform {

    private float x;
    private float y;

    private int spriteWidth = 96;
    private int spriteHeight = 32;

    private float startY; //Position at start
    private float maximumRange = 320 / ComputerScienceRunner.PPM; //Platform should move 320 up then back to 0

    private Texture texture = new Texture(Gdx.files.internal("horizontalPlatform.png"));

    private Animation<TextureRegion> flyAnimation;

    private Animation<TextureRegion> currentAnimation;

    private float stateTime;

    private TextureRegion currentFrame;

    public Body body;

    private ComputerScienceRunner game;

    private boolean flyUp;
    private boolean flyDown;

    private boolean playerContact; //True if player stands on it

    private boolean playerVelocityChanged;

    public VerticalPlatform(ComputerScienceRunner computerScienceRunner, World world, float x, float y, int direction) {

        this.x = x / ComputerScienceRunner.PPM;
        this.y = y / ComputerScienceRunner.PPM;

        startY = y / ComputerScienceRunner.PPM;

        if(direction == 0) {
            flyUp = true;
        } else if (direction == 1) {
            flyDown = true;
        }

        this.game = computerScienceRunner;

        createFlyAnimation();

        currentAnimation = flyAnimation;


        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.KinematicBody;
        bodyDef.position.set(this.x , this.y);

        body = world.createBody(bodyDef);
        FixtureDef fixtureDef = new FixtureDef();


        PolygonShape platformBody = new PolygonShape();
        platformBody.setAsBox(48 / ComputerScienceRunner.PPM, 5 / ComputerScienceRunner.PPM, new Vector2(16 / ComputerScienceRunner.PPM, -7/ ComputerScienceRunner.PPM), 0);
        fixtureDef.shape = platformBody;


        fixtureDef.filter.categoryBits = ComputerScienceRunner.ONEWAY_PLATFORM_BIT;
        fixtureDef.filter.maskBits = ComputerScienceRunner.PLAYER_BIT | ComputerScienceRunner.PLAYER_FEET_BIT | ComputerScienceRunner.PLAYER_HEAD_BIT;

        body.createFixture(fixtureDef).setUserData(this);

    }

    public void createFlyAnimation() {
        Array<TextureRegion> frames = new Array<TextureRegion>();
        for(int i = 0; i < 1; i++) {
            frames.add(new TextureRegion(texture, 0, 0, spriteWidth, spriteHeight));
        }
        flyAnimation = new Animation(0.1f, frames);
        frames.clear();
    }

    public void update(float dt) {
        stateTime = stateTime + dt;

        currentFrame = currentAnimation.getKeyFrame(stateTime, true);

        x = body.getPosition().x;
        y = body.getPosition().y;



        if(body.getPosition().y - startY >= maximumRange) {
            flyUp = false;
            flyDown = true;
        } else if(body.getPosition().y - startY <= 0) {
            flyUp = true;
            flyDown = false;
        }


        if(flyUp) {
            body.setLinearVelocity(0, 1);
        } else if(flyDown) {
            body.setLinearVelocity(0, -1);
        }


        Player player = ComputerScienceRunner.playScreen.getPlayer();

        if(player.isOnMovingVerticalPlatform()) {
            if ((!Gdx.input.isKeyPressed(Input.Keys.UP)) && flyUp) {
                player.body.setLinearVelocity(player.body.getLinearVelocity().x, 1.2f);
            }
        }

    }

    public void draw() {

        Camera camera = ComputerScienceRunner.playScreen.getCamera();

        Vector3 worldCoordinates = new Vector3(x, y, 0);
        Vector3 screenCoordinates = camera.project(worldCoordinates);

        ComputerScienceRunner.batch.draw(currentFrame, screenCoordinates.x - 32, screenCoordinates.y - 24);

    }

    public boolean isPlayerContact() {
        return playerContact;
    }

    public void setPlayerContact(boolean playerContact) {
        this.playerContact = playerContact;
    }
}
