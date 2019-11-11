package at.ac.univie.computersciencerunner.mapObjects;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileSet;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;

import at.ac.univie.computersciencerunner.ComputerScienceRunner;

public class HeartBrick extends InteractiveObject {

    protected boolean toDestroy;
    protected boolean destroyed;

    private final int DESTROYED_ITEM_BRICK_ID = 8; //Taken from Tiled, but we always need to take the id from tiled and add one to it
    private final int HEART_ID = 6;
    private static TiledMapTileSet tileSet;

    public HeartBrick(World world, TiledMap map, Rectangle bounds) {
        super(world, map, bounds);
        tileSet = map.getTileSets().getTileSet("tileset");
        toDestroy = false;
        destroyed = false;
    }

    public void update(float dt) {
        if(toDestroy && !destroyed) {
            destroyed = true;
            TiledMapTileLayer layer = (TiledMapTileLayer) map.getLayers().get(1);
            layer.getCell((int) (body.getPosition().x * ComputerScienceRunner.PPM / 32), (int) (body.getPosition().y * ComputerScienceRunner.PPM / 32)).setTile(tileSet.getTile(DESTROYED_ITEM_BRICK_ID));

            layer.getCell((int) (body.getPosition().x * ComputerScienceRunner.PPM / 32), (int) (body.getPosition().y * ComputerScienceRunner.PPM / 32) + 1).setTile(tileSet.getTile(HEART_ID));

            Rectangle rect = new Rectangle((int) (body.getPosition().x * ComputerScienceRunner.PPM) - 16, (int) (body.getPosition().y * ComputerScienceRunner.PPM)  + 16, 32, 32);
            ComputerScienceRunner.playScreen.getEctsList().add(new ECTS(ComputerScienceRunner.playScreen.getWorld(), ComputerScienceRunner.playScreen.getTiledMap(), rect));

        }
    }

    public void setToDestroy() {
        toDestroy = true;
    }

}
