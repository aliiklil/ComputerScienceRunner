package at.ac.univie.computersciencerunner.hud;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import at.ac.univie.computersciencerunner.ComputerScienceRunner;

public class Hud implements Disposable {
    public Stage stage;
    private Viewport viewPort;

    private int ectsCount;
    private int semesterCount;

    Label ectsLabel;
    Label semesterLabel;

    Label ectsValue;
    Label semesterValue;

    public Hud(SpriteBatch spriteBatch) {
        ectsCount = 0;

        viewPort = new FitViewport(ComputerScienceRunner.WIDTH, ComputerScienceRunner.HEIGHT, new OrthographicCamera());
        stage = new Stage(viewPort, spriteBatch);

        Table table = new Table();
        table.top();

        table.setFillParent(true);

        ectsLabel = new Label("ECTS", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        semesterLabel = new Label("Semester", new Label.LabelStyle(new BitmapFont(), Color.WHITE));

        ectsValue = new Label(String.valueOf(ectsCount), new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        semesterValue = new Label(String.valueOf(semesterCount), new Label.LabelStyle(new BitmapFont(), Color.WHITE));

        table.add(ectsLabel).expandX().padTop(10);
        table.add(semesterLabel).expandX().padTop(10);
        table.row();

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
}
