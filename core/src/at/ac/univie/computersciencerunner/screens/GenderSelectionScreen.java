package at.ac.univie.computersciencerunner.screens;

import com.badlogic.gdx.Gdx;
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

public class GenderSelectionScreen implements Screen {

    private Viewport viewport;
    private Stage stage;

    private FreeTypeFontGenerator freeTypeFontGenerator;
    private FreeTypeFontGenerator.FreeTypeFontParameter fontParameter;
    private BitmapFont font;

    private Skin levelSelectButtonSkin = new Skin(Gdx.files.internal("skins/level/glassy-ui.json"));
    private Skin blueSkin = new Skin(Gdx.files.internal("skins/button/glassy-ui.json"));

    private Skin greySkin = new Skin(Gdx.files.internal("skins/greybutton/glassy-ui.json"));

    private TextButton semesterButtons[] = new TextButton[10];

    private TextButton backButton;
    private TextButton maleButton;
    private TextButton femaleButton;

    private Image malePlayerImage = new Image(new Texture(Gdx.files.internal("male_player_selection_image.png")));
    private Image femalePlayerImage = new Image(new Texture(Gdx.files.internal("female_player_selection_image.png")));

    private final ComputerScienceRunner game;

    private Screen callingScreen; //Screen which was before this screen. Needed to know for back button

    public GenderSelectionScreen(ComputerScienceRunner computerScienceRunner) {

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

        Label optionsLabel  = new Label("Wähle Charakter", new Label.LabelStyle(font, new Color(150f/255, 220f/255, 255f/255, 1)));
        table.add(optionsLabel).expandX().top().padTop(10).colspan(2);
        table.row();


        maleButton = new TextButton("Mann", blueSkin.get("small", TextButton.TextButtonStyle.class));

        maleButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {

                Preferences prefs = Gdx.app.getPreferences("ComputerScienceRunnerPrefs");
                prefs.putInteger("selectedGender", 1); //1 is for male player
                prefs.flush();

                if(callingScreen == ComputerScienceRunner.optionsScreen) {
                    game.setOptionsScreen();
                } else {
                    game.setLevelSelectionScreen();
                }

                dispose();
                return true;
            }
        });

        femaleButton = new TextButton("Frau", blueSkin.get("small", TextButton.TextButtonStyle.class));

        femaleButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {

                Preferences prefs = Gdx.app.getPreferences("ComputerScienceRunnerPrefs");
                prefs.putInteger("selectedGender", 2); //2 is for female player
                prefs.flush();

                if(callingScreen == ComputerScienceRunner.optionsScreen) {
                    game.setOptionsScreen();
                } else {
                    game.setLevelSelectionScreen();
                }

                dispose();
                return true;
            }
        });

        table.add(maleButton).left().top().padLeft(300).padTop(50).padRight(0);
        table.add(femaleButton).right().top().padRight(300).padTop(50);

        table.row();

        table.add(malePlayerImage).left().top().padLeft(330).padTop(10).padRight(0);
        table.add(femalePlayerImage).right().top().padRight(330).padTop(10);


        table.row();

        backButton = new TextButton("Zur#ck", blueSkin.get("small", TextButton.TextButtonStyle.class));

        backButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {

                if(callingScreen == ComputerScienceRunner.optionsScreen) {
                    game.setOptionsScreen();
                } else {
                    game.setMainMenuScreen();
                }

                dispose();
                return true;
            }
        });


        table.add(backButton).colspan(2).bottom().right().expandY().expandX().padBottom(20).padRight(50);

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

    public Screen getCallingScreen() {
        return  callingScreen;
    }

    public void setCallingScreen(Screen callingScreen) {
        this.callingScreen = callingScreen;
    }
}
