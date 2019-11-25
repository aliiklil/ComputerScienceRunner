package at.ac.univie.computersciencerunner.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import at.ac.univie.computersciencerunner.ComputerScienceRunner;

public class MainMenuScreen implements Screen {

    private Viewport viewport;
    private Stage stage;

    private static FreeTypeFontGenerator freeTypeFontGenerator;
    private static FreeTypeFontGenerator.FreeTypeFontParameter fontParameter;
    private static BitmapFont font;



    TextButton playButton;
    TextButton.TextButtonStyle playButtonStyle;

    TextButton optionsButton;
    TextButton.TextButtonStyle optionsButtonStyle;

    TextButton endButton;
    TextButton.TextButtonStyle endButtonStyle;


    final ComputerScienceRunner game;

    public MainMenuScreen(ComputerScienceRunner gsame) {

        this.game = gsame;

        viewport = new FitViewport(ComputerScienceRunner.WIDTH, ComputerScienceRunner.HEIGHT, new OrthographicCamera());
        stage = new Stage(viewport, ComputerScienceRunner.batch);


        freeTypeFontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("basicFont.ttf"));
        fontParameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        fontParameter.size = 80;
        fontParameter.borderColor = Color.BLACK;
        fontParameter.borderWidth = 3;

        fontParameter.color = Color.WHITE;
        font = freeTypeFontGenerator.generateFont(fontParameter);







        stage = new Stage();

        Table table = new Table();
        table.center();
        table.setFillParent(true);

        Label computerScienceRunnerLabel  = new Label("COMPUTER SCIENCE RUNNER", new Label.LabelStyle(font, new Color(150f/255, 220f/255, 255f/255, 1)));
        table.add(computerScienceRunnerLabel).expandX();
        table.row();


        Gdx.input.setInputProcessor(stage);

        playButtonStyle = new TextButton.TextButtonStyle();
        playButtonStyle.font = font;
        playButton = new TextButton("PLAY", playButtonStyle);

        playButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                game.setPlayScreen();
                dispose();
                return true;
            }
        });

        table.add(playButton).expandX().padTop(50);
        table.row();


        optionsButtonStyle = new TextButton.TextButtonStyle();
        optionsButtonStyle.font = font;
        optionsButton = new TextButton("OPTIONS", optionsButtonStyle);

        optionsButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                game.setOptionsScreen();
                dispose();
                return true;
            }
        });

        table.add(optionsButton).expandX().padTop(50);
        table.row();



        endButtonStyle = new TextButton.TextButtonStyle();
        endButtonStyle.font = font;
        endButton = new TextButton("END", endButtonStyle);

        endButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                dispose();
                return true;
            }
        });


        table.add(endButton).expandX().padTop(50);


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
