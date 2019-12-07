package at.ac.univie.computersciencerunner.mapObjects;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

import at.ac.univie.computersciencerunner.ComputerScienceRunner;

public abstract class InteractiveObject {
    protected World world;
    protected TiledMap map;
    protected TiledMapTile tile;
    protected Rectangle bounds;
    protected Body body;

    protected Fixture fixture;

    public InteractiveObject(World world, TiledMap map, Rectangle bounds) {
        this.world = world;
        this.map = map;
        this.bounds = bounds;

        BodyDef bdef = new BodyDef();
        FixtureDef fdef = new FixtureDef();
        PolygonShape shape = new PolygonShape();

        bdef.type = BodyDef.BodyType.StaticBody;
        bdef.position.set((bounds.getX() + bounds.getWidth() / 2) / ComputerScienceRunner.PPM, (bounds.getY() + bounds.getHeight() / 2) / ComputerScienceRunner.PPM);

        body = world.createBody(bdef);

        shape.setAsBox(bounds.getWidth() / 2 / ComputerScienceRunner.PPM, bounds.getHeight() / 2 / ComputerScienceRunner.PPM);
        fdef.shape = shape;
        fdef.friction = 0;



        Filter filter = new Filter();

        if (this instanceof Brick) {
            filter.categoryBits = ComputerScienceRunner.BRICK_BIT;
        }
        if (this instanceof ECTS) {
            filter.categoryBits = ComputerScienceRunner.COLLECTIBLE_BIT;
            filter.maskBits = ComputerScienceRunner.PLAYER_BIT;
            fdef.isSensor = true;
        }
        if (this instanceof ECTSBrick) {
            filter.categoryBits = ComputerScienceRunner.BRICK_BIT;
        }
        if (this instanceof Heart) {
            filter.categoryBits = ComputerScienceRunner.COLLECTIBLE_BIT;
            filter.maskBits = ComputerScienceRunner.PLAYER_BIT;

            fdef.isSensor = true;
        }
        if (this instanceof HeartBrick) {
            filter.categoryBits = ComputerScienceRunner.BRICK_BIT;
        }
        if (this instanceof InfoBrick) {
            filter.categoryBits = ComputerScienceRunner.BRICK_BIT;
        }
        if (this instanceof Coin) {
            filter.categoryBits = ComputerScienceRunner.COLLECTIBLE_BIT;
            filter.maskBits = ComputerScienceRunner.PLAYER_BIT;
            fdef.isSensor = true;
        }

        if (this instanceof CoinBrick) {
            filter.categoryBits = ComputerScienceRunner.BRICK_BIT;
        }

        fixture = body.createFixture(fdef);
        fixture.setUserData(this);
        fixture.setFilterData(filter);
    }

    public void setCategoryBits(short categoryBits){
        Filter filter = new Filter();
        filter.categoryBits = categoryBits;
        fixture.setFilterData(filter);
    }

}
