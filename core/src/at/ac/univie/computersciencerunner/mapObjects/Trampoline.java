package at.ac.univie.computersciencerunner.mapObjects;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileSet;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;

import at.ac.univie.computersciencerunner.ComputerScienceRunner;

public class Trampoline extends InteractiveObject {

    private boolean pressDown; //True when player currently is on the trampoline

    private final int TRAMPOLINE_UP_ID = 25;
    private final int TRAMPOLINE_DOWN_ID = 26;
    private static TiledMapTileSet tileSet;

    public Trampoline(World world, TiledMap map, Rectangle bounds) {
        super(world, map, bounds);
        tileSet = map.getTileSets().getTileSet("tileset");
    }

    public void update(float dt) {
        if(pressDown) {

            TiledMapTileLayer layer = (TiledMapTileLayer) map.getLayers().get(1);
            layer.getCell((int) (body.getPosition().x * ComputerScienceRunner.PPM / 32), (int) (body.getPosition().y * ComputerScienceRunner.PPM / 32)).setTile(tileSet.getTile(TRAMPOLINE_DOWN_ID));

        } else {

            TiledMapTileLayer layer = (TiledMapTileLayer) map.getLayers().get(1);
            layer.getCell((int) (body.getPosition().x * ComputerScienceRunner.PPM / 32), (int) (body.getPosition().y * ComputerScienceRunner.PPM / 32)).setTile(tileSet.getTile(TRAMPOLINE_UP_ID));

        }
    }

    public boolean isPressDown() {
        return pressDown;
    }

    public void setPressDown(boolean pressDown) {
        this.pressDown = pressDown;
    }
}
