package at.ac.univie.computersciencerunner.mapObjects;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileSet;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;

import at.ac.univie.computersciencerunner.ComputerScienceRunner;

public class ItemBrick extends InteractiveObject {

    protected boolean toDestroy;
    protected boolean destroyed;

    private final int DESTROYED_ITEM_BRICK_ID = 8; //Id taken from Tiled,
    private static TiledMapTileSet tileSet;

    public ItemBrick(World world, TiledMap map, Rectangle bounds) {
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
            ComputerScienceRunner.playScreen.getHud().setEctsCount(ComputerScienceRunner.playScreen.getHud().getEctsCount() + 1);
        }
    }

    public void setToDestroy() {
        toDestroy = true;
    }

}
