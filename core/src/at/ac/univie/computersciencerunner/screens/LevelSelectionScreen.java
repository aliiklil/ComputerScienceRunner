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
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import at.ac.univie.computersciencerunner.ComputerScienceRunner;

public class LevelSelectionScreen implements Screen {

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

    private final ComputerScienceRunner game;

    public LevelSelectionScreen(ComputerScienceRunner computerScienceRunner) {

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

        Label selectSemesterLabel  = new Label("Wähle Semester", new Label.LabelStyle(font, new Color(150f/255, 220f/255, 255f/255, 1)));
        table.add(selectSemesterLabel).fillX();
        table.row();

        stage.addActor(table);

        table = new Table();
        table.top();
        table.setFillParent(true);




        table.padTop(50);

        Preferences prefs = Gdx.app.getPreferences("ComputerScienceRunnerPrefs");
        final int highestCompletedSemester = prefs.getInteger("highestCompletedSemester", 0);

        for(int i = 0; i < semesterButtons.length; i++) {

            if(highestCompletedSemester >= i) {
                semesterButtons[i] = new TextButton(String.valueOf(i + 1), blueSkin.get("small", TextButton.TextButtonStyle.class));
            } else {
                semesterButtons[i] = new TextButton(String.valueOf(i + 1), greySkin.get("small", TextButton.TextButtonStyle.class));
            }

            semesterButtons[i].setDisabled(true);
            final int index = i;

            semesterButtons[i].addListener(new InputListener() {
                @Override
                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {

                    if(highestCompletedSemester >= index) {
                        game.setPlayScreen(index + 1);
                        dispose();
                    }
                    return true;
                }
            });

            table.add(semesterButtons[i]);

            if((i-1) % 2 == 0 && i != 0) {
                table.row();
            }

        }



        backButton = new TextButton("Zur#ck", blueSkin.get("small", TextButton.TextButtonStyle.class));

        backButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                game.setMainMenuScreen();
                dispose();
                return true;
            }
        });


        table.add(backButton).colspan(2).padTop(10);


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
