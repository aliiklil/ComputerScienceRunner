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

public class HorizontalPlatform {

    private float x;
    private float y;

    private int spriteWidth = 96;
    private int spriteHeight = 32;

    private float startX; //Position at start
    private float maximumRange = 320 / ComputerScienceRunner.PPM; //Platform should move 320 to right then back to 0

    private Texture texture = new Texture(Gdx.files.internal("horizontalPlatform.png"));

    private Animation<TextureRegion> flyAnimation;

    private Animation<TextureRegion> currentAnimation;

    private float stateTime;

    private TextureRegion currentFrame;

    public Body body;

    private ComputerScienceRunner game;

    private boolean flyRight;
    private boolean flyLeft;

    private boolean playerContact; //True if player stands on it

    private boolean playerVelocityChanged;

    public HorizontalPlatform(ComputerScienceRunner computerScienceRunner, World world, float x, float y, int direction) {

        this.x = x / ComputerScienceRunner.PPM;
        this.y = y / ComputerScienceRunner.PPM;

        startX = x / ComputerScienceRunner.PPM;

        if(direction == 0) {
            flyRight = true;
        } else if (direction == 1) {
            flyLeft = true;
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



        if(body.getPosition().x - startX >= maximumRange) {
            flyLeft = true;
            flyRight = false;
        } else if(body.getPosition().x - startX <= 0) {
            flyLeft = false;
            flyRight = true;
        }


        if(flyRight) {
            body.setLinearVelocity(1, 0);

        } else if(flyLeft) {
            body.setLinearVelocity(-1, 0);

        }

        Player player = ComputerScienceRunner.playScreen.getPlayer();

        if(player.isOnMovingHorizontalPlatform() && !(player.body.getLinearVelocity().y > 0) && !player.isPlayerMovedOnHorizontalPlatform()) {

            if ((!Gdx.input.isKeyPressed(Input.Keys.LEFT) && !Gdx.input.isKeyPressed(Input.Keys.RIGHT) && body.getLinearVelocity().x <= 1f) || ComputerScienceRunner.playScreen.isPaused()) {
                player.body.setLinearVelocity(body.getLinearVelocity().x, player.body.getLinearVelocity().y);
            }

            if ((!Gdx.input.isKeyPressed(Input.Keys.RIGHT) && !Gdx.input.isKeyPressed(Input.Keys.LEFT) && body.getLinearVelocity().x >= 1f) || ComputerScienceRunner.playScreen.isPaused()) {
                player.body.setLinearVelocity(body.getLinearVelocity().x, player.body.getLinearVelocity().y);
            }

            player.setPlayerMovedOnHorizontalPlatform(true);
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
