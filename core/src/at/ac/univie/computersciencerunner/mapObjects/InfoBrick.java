package at.ac.univie.computersciencerunner.mapObjects;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;

import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileSet;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;

import at.ac.univie.computersciencerunner.ComputerScienceRunner;

public class InfoBrick extends InteractiveObject {

    private static TiledMapTileSet tileSet;


    public InfoBrick(World world, TiledMap map, Rectangle bounds) {
        super(world, map, bounds);
        tileSet = map.getTileSets().getTileSet("tileset");

    }

    public void displayInfo() {
        MapObjects mapObjects = map.getLayers().get(8).getObjects();

        for (MapObject mapObject : mapObjects) {
            MapProperties mapProperties = mapObject.getProperties();

            String title = "";
            String description = "";

            if (((int)ComputerScienceRunner.playScreen.getPlayer().body.getPosition().x) == ((int)(( float) mapProperties.get("x") / ComputerScienceRunner.PPM))) {
                if (mapProperties.containsKey("Title") && mapProperties.containsKey("Description")) {
                    title = (String) mapProperties.get("Title");
                    description = (String) mapProperties.get("Description");
                }
                ComputerScienceRunner.playScreen.getInfoWidget().setTitleAndDescription(title, description);
                ComputerScienceRunner.assetManager.get("audio/sounds/brickDestroyed.mp3", Sound.class).play();
                break;
            }
        }

    }

}
