package at.ac.univie.computersciencerunner.hud;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import at.ac.univie.computersciencerunner.ComputerScienceRunner;

public class InfoWidget implements Disposable {
    public Stage stage;
    private Viewport viewPort;

    private Label title;
    private Label description;

    private boolean currentlyDisplayed = false;

    private Image infoWidget = new Image(new Texture(Gdx.files.internal("infoWidget.png")));

    public InfoWidget(SpriteBatch spriteBatch) {
        viewPort = new FitViewport(ComputerScienceRunner.WIDTH, ComputerScienceRunner.HEIGHT, new OrthographicCamera());
        stage = new Stage(viewPort, spriteBatch);

        Table table = new Table();
        table.top();

        table.setFillParent(true);

        table.add(infoWidget).expandX();

        stage.addActor(table);

    }

    @Override
    public void dispose() {
        stage.dispose();
    }

    public boolean isCurrentlyDisplayed() {
        return currentlyDisplayed;
    }

    public void setCurrentlyDisplayed(boolean currentlyDisplayed) {
        this.currentlyDisplayed = currentlyDisplayed;
    }

}
