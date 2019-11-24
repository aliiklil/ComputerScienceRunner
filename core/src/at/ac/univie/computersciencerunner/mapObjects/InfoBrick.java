package at.ac.univie.computersciencerunner.mapObjects;

import com.badlogic.gdx.maps.tiled.TiledMap;

import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileSet;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;

public class InfoBrick extends InteractiveObject {

    protected boolean toDisplay;
    private static TiledMapTileSet tileSet;

    public InfoBrick(World world, TiledMap map, Rectangle bounds) {
        super(world, map, bounds);
        tileSet = map.getTileSets().getTileSet("tileset");
        toDisplay = false;
    }

    public void update(float dt) {
        if(toDisplay) {

        }
    }

    public void displayInfo() {
        toDisplay = true;
    }

}
