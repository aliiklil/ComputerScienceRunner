package at.ac.univie.computersciencerunner.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
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

public class MainMenuScreen implements Screen {

    private Viewport viewport;
    private Stage stage;

    private FreeTypeFontGenerator freeTypeFontGenerator;
    private FreeTypeFontGenerator.FreeTypeFontParameter fontParameter;
    private BitmapFont font;

    private Skin buttonSkin;

    private TextButton startButton;
    private TextButton optionsButton;
    private TextButton endButton;

    final ComputerScienceRunner game;

    private Image universityImage = new Image(new Texture(Gdx.files.internal("universityImage.png")));

    private Music menuMusic;

    public MainMenuScreen(ComputerScienceRunner computerScienceRunner) {

        game = computerScienceRunner;

        viewport = new FitViewport(ComputerScienceRunner.WIDTH, ComputerScienceRunner.HEIGHT, new OrthographicCamera());



        freeTypeFontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("basicFont.ttf"));
        fontParameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        fontParameter.size = 40;
        fontParameter.borderColor = Color.BLACK;
        fontParameter.borderWidth = 3;

        fontParameter.color = Color.WHITE;
        font = freeTypeFontGenerator.generateFont(fontParameter);

        menuMusic = ComputerScienceRunner.assetManager.get("audio/music/menu.mp3", Music.class);
        menuMusic.setLooping(true);


    }

    @Override
    public void show() {
        stage = new Stage(viewport, ComputerScienceRunner.batch);

        Gdx.input.setInputProcessor(stage);

        Table table = new Table();
        table.center();
        table.setFillParent(true);

        Label computerScienceRunnerLabel  = new Label("COMPUTER SCIENCE RUNNER", new Label.LabelStyle(font, new Color(150f/255, 220f/255, 255f/255, 1)));
        table.add(computerScienceRunnerLabel).expandX();
        table.row();

        buttonSkin = new Skin(Gdx.files.internal("skins/button/glassy-ui.json"));


        startButton = new TextButton("START", buttonSkin.get("small", TextButton.TextButtonStyle.class));

        startButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {

                Preferences prefs = Gdx.app.getPreferences("ComputerScienceRunnerPrefs");
                final int selectedGender = prefs.getInteger("selectedGender", 0); //0 no gender selected, 1 male, 2 female

                if(selectedGender == 0) {
                    game.setGenderSelectionScreen();
                    ComputerScienceRunner.genderSelectionScreen.setCallingScreen(ComputerScienceRunner.mainMenuScreen);
                } else {
                    game.setLevelSelectionScreen();
                }

                dispose();
                return true;
            }
        });

        table.add(startButton).expandX().padTop(10);
        table.row();


        optionsButton = new TextButton("OPTIONEN", buttonSkin.get("small", TextButton.TextButtonStyle.class));
        optionsButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                game.setOptionsScreen();
                dispose();
                return true;
            }
        });

        table.add(optionsButton).expandX().padTop(10);
        table.row();

        optionsButton = new TextButton("CREDITS", buttonSkin.get("small", TextButton.TextButtonStyle.class));
        optionsButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                game.setCreditsScreen();
                dispose();
                return true;
            }
        });

        table.add(optionsButton).expandX().padTop(10);
        table.row();



        endButton = new TextButton("ENDE", buttonSkin.get("small", TextButton.TextButtonStyle.class));
        endButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                dispose();
                System.exit(0);
                return true;
            }
        });


        table.add(endButton).expandX().padTop(10);

        table.row();

        table.add(universityImage).right();

        stage.addActor(table);

        if(ComputerScienceRunner.playScreen.getCurrentSemester() != 0) { //If music is playing from the PlayScreen stop it
            Music levelMusic = ComputerScienceRunner.assetManager.get("audio/music/level" + ComputerScienceRunner.playScreen.getCurrentSemester() + ".mp3", Music.class);
            levelMusic.stop();
        }

        menuMusic.play();
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
