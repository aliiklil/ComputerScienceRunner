package at.ac.univie.computersciencerunner.mapObjects;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;

import at.ac.univie.computersciencerunner.ComputerScienceRunner;

public class ECTS extends InteractiveObject {

    protected boolean toDestroy;
    protected boolean destroyed;

    public ECTS(World world, TiledMap map, Rectangle bounds) {
        super(world, map, bounds);
        toDestroy = false;
        destroyed = false;
    }

    public void update(float dt) {
        if(toDestroy && !destroyed) {
            world.destroyBody(body);
            destroyed = true;
            TiledMapTileLayer layer = (TiledMapTileLayer) map.getLayers().get(1);
            layer.getCell((int) (body.getPosition().x * ComputerScienceRunner.PPM / 32), (int) (body.getPosition().y * ComputerScienceRunner.PPM / 32)).setTile(null);
            ComputerScienceRunner.playScreen.getHud().setEctsCount(ComputerScienceRunner.playScreen.getHud().getEctsCount() + 1);
            ComputerScienceRunner.assetManager.get("audio/sounds/ects.mp3", Sound.class).play();
        }
    }

    public void setToDestroy() {
        toDestroy = true;
    }

}