package at.ac.univie.computersciencerunner.hud;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import at.ac.univie.computersciencerunner.ComputerScienceRunner;

public class Hud implements Disposable {
    public Stage stage;
    private Viewport viewPort;

    private int ectsCount;
    private int semesterCount;

    private Label ectsLabel;
    private Label semesterLabel;

    private Label ectsValue;
    private Label semesterValue;

    private Texture hearts0 = new Texture(Gdx.files.internal("hearts0.png"));
    private Texture hearts1 = new Texture(Gdx.files.internal("hearts1.png"));
    private Texture hearts2 = new Texture(Gdx.files.internal("hearts2.png"));
    private Texture hearts3 = new Texture(Gdx.files.internal("hearts3.png"));

    private Image currentsHeartsImage;

    public Hud(SpriteBatch spriteBatch) {
        ectsCount = 0;

        viewPort = new FitViewport(ComputerScienceRunner.WIDTH, ComputerScienceRunner.HEIGHT, new OrthographicCamera());
        stage = new Stage(viewPort, spriteBatch);

        Table table = new Table();
        table.top();

        table.setFillParent(true);

        currentsHeartsImage = new Image(hearts0);

        table.add(currentsHeartsImage).expandX().padTop(10);

        ectsLabel = new Label("ECTS", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        semesterLabel = new Label("Semester", new Label.LabelStyle(new BitmapFont(), Color.WHITE));

        ectsValue = new Label(String.valueOf(ectsCount), new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        semesterValue = new Label(String.valueOf(semesterCount), new Label.LabelStyle(new BitmapFont(), Color.WHITE));

        table.add(ectsLabel).expandX().padTop(10);
        table.add(semesterLabel).expandX().padTop(10);
        table.row();

        table.add().expandX().padTop(10);
        table.add(ectsValue).expandX().padTop(10);
        table.add(semesterValue).expandX().padTop(10);

        stage.addActor(table);



    }

    @Override
    public void dispose() {
        stage.dispose();
    }

    public int getEctsCount() {
        return ectsCount;
    }

    public void setEctsCount(int ectsCount) {
        this.ectsCount = ectsCount;
        ectsValue.setText(String.valueOf(ectsCount));
    }

    public void setHeartsCount(int heartsCount) {
       if(heartsCount == 0) {
           currentsHeartsImage.setDrawable(new TextureRegionDrawable(hearts0));
       } else if (heartsCount == 1) {
           currentsHeartsImage.setDrawable(new TextureRegionDrawable(hearts1));
       } else if (heartsCount == 2) {
           currentsHeartsImage.setDrawable(new TextureRegionDrawable(hearts2));
       } else if (heartsCount == 3) {
           currentsHeartsImage.setDrawable(new TextureRegionDrawable(hearts3));
       }
    }
}
