package at.ac.univie.computersciencerunner;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.maps.tiled.tiles.AnimatedTiledMapTile;

public class CustomOrthogonalTiledMapRenderer extends OrthogonalTiledMapRenderer {
    private boolean animate = true;

    public CustomOrthogonalTiledMapRenderer(TiledMap map, float unitScale, SpriteBatch spriteBatch) {
        super(map, unitScale, spriteBatch);
    }

    @Override
    protected void beginRender () {
        if (animate)
            AnimatedTiledMapTile.updateAnimationBaseTime();
        batch.begin();
    }

    public void setAnimate(boolean animate) {
        this.animate = animate;
    }

}