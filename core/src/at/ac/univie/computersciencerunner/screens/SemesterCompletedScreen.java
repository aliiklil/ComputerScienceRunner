package at.ac.univie.computersciencerunner.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import at.ac.univie.computersciencerunner.ComputerScienceRunner;

public class SemesterCompletedScreen implements Screen {

    private Viewport viewport;
    private Stage stage;

    private FreeTypeFontGenerator freeTypeFontGenerator;
    private FreeTypeFontGenerator.FreeTypeFontParameter fontParameter;
    private BitmapFont font;

    private Skin blueSkin = new Skin(Gdx.files.internal("skins/button/glassy-ui.json"));

    private Label semesterCompletedLabel;

    private Label ectsLabel;
    private Label ectsValue;

    private Label coinsLabel;
    private Label coinsValue;

    private Label correctQuestionsLabel;
    private Label correctQuestionsValue;

    private TextButton nextSemesterButton;
    private TextButton mainMenuButton;

    private Label bachelorMasterLabel; //Displays after 6 semester that bachelor is done and after 10 that master is done

    private final ComputerScienceRunner game;

    public SemesterCompletedScreen(ComputerScienceRunner computerScienceRunner) {

        this.game = computerScienceRunner;

        viewport = new FitViewport(ComputerScienceRunner.WIDTH, ComputerScienceRunner.HEIGHT, new OrthographicCamera());

        freeTypeFontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("basicFont.ttf"));
        fontParameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        fontParameter.size = 24;
        fontParameter.borderColor = Color.BLACK;
        fontParameter.borderWidth = 3;

        fontParameter.color = Color.WHITE;
        font = freeTypeFontGenerator.generateFont(fontParameter);

    }

    @Override
    public void show() {

        stage = new Stage(viewport, ComputerScienceRunner.batch);


        Table table = new Table();
        table.center();
        table.setFillParent(true);

        if(ComputerScienceRunner.playScreen.getCurrentSemester() == 6) {
            bachelorMasterLabel = new Label("Gut gemacht! Du hast jetzt den Bachelor.", new Label.LabelStyle(font, new Color(100 / 255, 180f / 255, 200f / 255, 1)));
            table.add(bachelorMasterLabel).colspan(2);
            table.row();
        } else if (ComputerScienceRunner.playScreen.getCurrentSemester() == 10) {
            bachelorMasterLabel = new Label("Super! Du hast jetzt den Master.", new Label.LabelStyle(font, new Color(100 / 255, 180f / 255, 200f / 255, 1)));
            table.add(bachelorMasterLabel).colspan(2);
            table.row();
        }

        semesterCompletedLabel  = new Label("Semester " + ComputerScienceRunner.playScreen.getCurrentSemester() + " abgeschlossen", new Label.LabelStyle(font, new Color(100/255, 180f/255, 200f/255, 1)));
        table.add(semesterCompletedLabel).colspan(2);
        table.row();


        ectsLabel  = new Label("ECTS:", new Label.LabelStyle(font, new Color(150f/255, 220f/255, 255f/255, 1)));
        table.add(ectsLabel).left().padTop(25);

        ectsValue  = new Label(String.valueOf(ComputerScienceRunner.playScreen.getHud().getEctsCount()), new Label.LabelStyle(font, new Color(150f/255, 220f/255, 255f/255, 1)));
        table.add(ectsValue).right().padTop(25);
        table.row();



        coinsLabel  = new Label("Coins:", new Label.LabelStyle(font, new Color(150f/255, 220f/255, 255f/255, 1)));
        table.add(coinsLabel).left();

        coinsValue  = new Label(String.valueOf(ComputerScienceRunner.playScreen.getHud().getCoinCount()), new Label.LabelStyle(font, new Color(150f/255, 220f/255, 255f/255, 1)));
        table.add(coinsValue).right();
        table.row();



        correctQuestionsLabel  = new Label("Richtige Fragen:", new Label.LabelStyle(font, new Color(150f/255, 220f/255, 255f/255, 1)));
        table.add(correctQuestionsLabel).left();

        correctQuestionsValue  = new Label(String.valueOf(ComputerScienceRunner.questionScreen.getRightAnswersCount()) + "/3", new Label.LabelStyle(font, new Color(150f/255, 220f/255, 255f/255, 1)));
        table.add(correctQuestionsValue).right();
        table.row();



        if(ComputerScienceRunner.playScreen.getCurrentSemester() < 10) {
            nextSemesterButton = new TextButton("N@chstes Semester", blueSkin.get("small", TextButton.TextButtonStyle.class));

            nextSemesterButton.addListener(new InputListener() {
                @Override
                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                    game.setPlayScreen(ComputerScienceRunner.playScreen.getCurrentSemester() + 1);

                    ComputerScienceRunner.assetManager.get("audio/music/level" + ComputerScienceRunner.playScreen.getCurrentSemester() + ".mp3", Music.class).play();

                    ComputerScienceRunner.assetManager.get("audio/sounds/levelCompleted.mp3", Sound.class).stop();
                    dispose();
                    return true;
                }
            });

            table.add(nextSemesterButton).padTop(25).colspan(2);

        }

        table.row();


        mainMenuButton = new TextButton("Hauptmen#", blueSkin.get("small", TextButton.TextButtonStyle.class));

        mainMenuButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                game.setMainMenuScreen();
                dispose();
                ComputerScienceRunner.assetManager.get("audio/sounds/levelCompleted.mp3", Sound.class).stop();
                return true;
            }
        });

        table.add(mainMenuButton).padTop(25).colspan(2);


        stage.addActor(table);
        Gdx.input.setInputProcessor(stage);

        ComputerScienceRunner.assetManager.get("audio/music/questions.mp3", Music.class).stop();
        ComputerScienceRunner.assetManager.get("audio/sounds/levelCompleted.mp3", Sound.class).play();

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
