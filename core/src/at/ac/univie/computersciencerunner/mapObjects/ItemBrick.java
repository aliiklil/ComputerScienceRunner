package at.ac.univie.computersciencerunner.mapObjects;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;

public class ItemBrick extends InteractiveObject {

    public ItemBrick(World world, TiledMap map, Rectangle bounds) {
        super(world, map, bounds);
    }

}
