package at.ac.univie.computersciencerunner.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import at.ac.univie.computersciencerunner.ComputerScienceRunner;

public class MainMenuScreen implements Screen {

    private Viewport viewport;
    private Stage stage;

    private static FreeTypeFontGenerator freeTypeFontGenerator;
    private static FreeTypeFontGenerator.FreeTypeFontParameter fontParameter;
    private static BitmapFont font;

    public MainMenuScreen() {

        viewport = new FitViewport(ComputerScienceRunner.WIDTH, ComputerScienceRunner.HEIGHT, new OrthographicCamera());
        stage = new Stage(viewport, ComputerScienceRunner.batch);


        freeTypeFontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("basicFont.ttf"));
        fontParameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        fontParameter.size = 80;
        fontParameter.borderColor = Color.BLACK;
        fontParameter.borderWidth = 3;

        fontParameter.color = Color.WHITE;
        font = freeTypeFontGenerator.generateFont(fontParameter);


        Table table = new Table();
        table.center();
        table.setFillParent(true);

        Label computerScienceRunnerLabel  = new Label("COMPUTER SCIENCE RUNNER", new Label.LabelStyle(font, new Color(150f/255, 220f/255, 255f/255, 1)));
        table.add(computerScienceRunnerLabel).expandX();
        table.row();

        Label playLabel  = new Label("PLAY", new Label.LabelStyle(font, new Color(1, 1, 1, 1)));
        table.add(playLabel).expandX().padTop(100);
        table.row();


        Label optionsLabel  = new Label("OPTIONS", new Label.LabelStyle(font, new Color(1, 1, 1, 1)));
        table.add(optionsLabel).expandX().padTop(50);
        table.row();

        Label endLabel  = new Label("END", new Label.LabelStyle(font, new Color(1, 1, 1, 1)));
        table.add(endLabel).expandX().padTop(50);
        table.row();

        stage.addActor(table);

    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {

        Gdx.gl.glClearColor(21.0f/255, 80.0f/255, 80.0f/255, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);



        stage.draw();

    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
