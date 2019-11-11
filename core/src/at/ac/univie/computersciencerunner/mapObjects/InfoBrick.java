package at.ac.univie.computersciencerunner.mapObjects;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileSet;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;

public class InfoBrick extends InteractiveObject {

    protected boolean toDestroy;
    protected boolean destroyed;

    private final int DESTROYED_ITEM_BRICK_ID = 8; //Id taken from Tiled, but we always need to take the id from tiled and add one to it
    private final int ECTS_ID = 17;
    private static TiledMapTileSet tileSet;

    private String text; //Text which should be shown when the player hits this infobrick with his head

    public InfoBrick(World world, TiledMap map, Rectangle bounds) {
        super(world, map, bounds);
        tileSet = map.getTileSets().getTileSet("tileset");
        toDestroy = false;
        destroyed = false;
    }

    public void update(float dt) {
        if(toDestroy && !destroyed) {
            destroyed = true;
            TiledMapTileLayer layer = (TiledMapTileLayer) map.getLayers().get(1);
        }
    }

    public void setToDestroy() {
        toDestroy = true;
    }

}
