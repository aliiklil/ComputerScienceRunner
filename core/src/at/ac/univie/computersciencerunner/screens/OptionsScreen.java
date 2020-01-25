package at.ac.univie.computersciencerunner.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import at.ac.univie.computersciencerunner.ComputerScienceRunner;

public class OptionsScreen implements Screen {

    private Viewport viewport;
    private Stage stage;

    private FreeTypeFontGenerator freeTypeFontGenerator;
    private FreeTypeFontGenerator.FreeTypeFontParameter fontParameter;
    private BitmapFont font;

    private Skin blueSkin = new Skin(Gdx.files.internal("skins/button/glassy-ui.json"));

    private TextButton backButton;

    private final ComputerScienceRunner game;

    private TextButton deleteProgressButton;
    private TextButton selectGenderButton;
    private TextButton unlockAllLevelsButton;
    private TextButton toggleFullscreenButton; //For changing betweend windowed and fullscreen

    public OptionsScreen(ComputerScienceRunner computerScienceRunner) {

        this.game = computerScienceRunner;

        viewport = new FitViewport(ComputerScienceRunner.WIDTH, ComputerScienceRunner.HEIGHT, new OrthographicCamera());



        freeTypeFontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("basicFont.ttf"));
        fontParameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        fontParameter.size = 40;
        fontParameter.borderColor = Color.BLACK;
        fontParameter.borderWidth = 3;

        fontParameter.color = Color.WHITE;
        font = freeTypeFontGenerator.generateFont(fontParameter);









    }

    @Override
    public void show() {

        stage = new Stage(viewport, ComputerScienceRunner.batch);

        Gdx.input.setInputProcessor(stage);

        Table table = new Table();
        table.top();
        table.setFillParent(true);

        Label optionsLabel  = new Label("Optionen", new Label.LabelStyle(font, new Color(150f/255, 220f/255, 255f/255, 1)));
        table.add(optionsLabel).expandX().top().padTop(10).colspan(2);
        table.row();


        deleteProgressButton = new TextButton("Spielstand entfernen",  blueSkin.get("small", TextButton.TextButtonStyle.class));

        deleteProgressButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                Preferences prefs = Gdx.app.getPreferences("ComputerScienceRunnerPrefs");
                prefs.putInteger("highestCompletedSemester", 0);
                prefs.putInteger("selectedGender", 0);
                prefs.flush();
                return true;
            }
        });

        table.add(deleteProgressButton).left().top().padLeft(10).padTop(10);

        table.row();

        selectGenderButton = new TextButton("Charakter wechseln",  blueSkin.get("small", TextButton.TextButtonStyle.class));

        selectGenderButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                game.setGenderSelectionScreen();
                ComputerScienceRunner.genderSelectionScreen.setCallingScreen(ComputerScienceRunner.optionsScreen);
                dispose();
                return true;
            }
        });

        table.add(selectGenderButton).left().top().padLeft(10).padTop(10);

        table.row();




        unlockAllLevelsButton = new TextButton("Alle Level freischalten",  blueSkin.get("small", TextButton.TextButtonStyle.class));

        unlockAllLevelsButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                Preferences prefs = Gdx.app.getPreferences("ComputerScienceRunnerPrefs");
                prefs.putInteger("highestCompletedSemester",10);
                prefs.flush();
                return true;
            }
        });

        table.add(unlockAllLevelsButton).left().top().padLeft(10).padTop(10);

        table.row();




        Preferences prefs = Gdx.app.getPreferences("ComputerScienceRunnerPrefs");
        final boolean fullscreen = prefs.getBoolean("fullscreen", false);

        String fullscreenButtonText = "";

        if(fullscreen) {
            fullscreenButtonText = "Fenstermodus";
        } else {
            fullscreenButtonText = "Vollbildmodus";
        }

        toggleFullscreenButton = new TextButton(fullscreenButtonText,  blueSkin.get("small", TextButton.TextButtonStyle.class));

        toggleFullscreenButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {

                if(fullscreen) {

                    Preferences prefs = Gdx.app.getPreferences("ComputerScienceRunnerPrefs");
                    prefs.putBoolean("fullscreen", false);
                    prefs.flush();

                    toggleFullscreenButton.setText("Vollbildmodus");

                    Gdx.graphics.setWindowedMode(1280, 720);

                    dispose();
                    game.setOptionsScreen();
                    return true;

                } else {

                    Preferences prefs = Gdx.app.getPreferences("ComputerScienceRunnerPrefs");
                    prefs.putBoolean("fullscreen", true);
                    prefs.flush();

                    toggleFullscreenButton.setText("Fenstermodus");


                    Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());


                    dispose();
                    game.setOptionsScreen();
                    return true;

                }
            }
        });

        table.add(toggleFullscreenButton).left().top().padLeft(10).padTop(10);

        table.row();




        backButton = new TextButton("Zur#ck", blueSkin.get("small", TextButton.TextButtonStyle.class));

        backButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                game.setMainMenuScreen();
                dispose();
                return true;
            }
        });


        table.add(backButton).bottom().right().expandY().expandX().padBottom(20).padRight(10);

        stage.addActor(table);

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
