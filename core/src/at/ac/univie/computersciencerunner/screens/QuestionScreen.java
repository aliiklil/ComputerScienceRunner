package at.ac.univie.computersciencerunner.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.MapProperties;
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

public class QuestionScreen implements Screen {

    private Viewport viewport;
    private Stage stage;

    private FreeTypeFontGenerator freeTypeFontGenerator;
    private FreeTypeFontGenerator.FreeTypeFontParameter fontParameter;
    private BitmapFont font;

    private Skin buttonSkin;

    private String question1;
    private String[] question1Answers =  new String[4];

    private String question2;
    private String[] question2Answers =  new String[4];

    private String question3;
    private String[] question3Answers =  new String[4];


    Label questionLabel; //Changes depending on which question the player is currentl

    private TextButton answer1Button; //Also changes depending on which question the player is currentl
    private TextButton answer2Button;
    private TextButton answer3Button;
    private TextButton answer4Button;

    private final ComputerScienceRunner game;

    private int questionCount = 0; //Can either be 0 or 1 or 2, depending on which question the player is currently

    public QuestionScreen(ComputerScienceRunner computerScienceRunner) {

        this.game = computerScienceRunner;

        viewport = new FitViewport(ComputerScienceRunner.WIDTH, ComputerScienceRunner.HEIGHT, new OrthographicCamera());

        freeTypeFontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("basicFont.ttf"));
        fontParameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        fontParameter.size = 36;
        fontParameter.borderColor = Color.BLACK;
        fontParameter.borderWidth = 3;

        fontParameter.color = Color.WHITE;
        font = freeTypeFontGenerator.generateFont(fontParameter);

    }

    @Override
    public void show() {

        MapObjects mapObjects = ComputerScienceRunner.playScreen.getTiledMap().getLayers().get(12).getObjects();
        MapObject mapObject = mapObjects.get(0);
        MapProperties mapProperties = mapObject.getProperties();

        question1 = (String) mapProperties.get("Question1");
        question1Answers[0] = (String) mapProperties.get("Question1Answer1");
        question1Answers[1] = (String) mapProperties.get("Question1Answer2");
        question1Answers[2] = (String) mapProperties.get("Question1Answer3");
        question1Answers[3] = (String) mapProperties.get("Question1Answer4");

        question2 = (String) mapProperties.get("Question2");
        question2Answers[0] = (String) mapProperties.get("Question2Answer1");
        question2Answers[1] = (String) mapProperties.get("Question2Answer2");
        question2Answers[2] = (String) mapProperties.get("Question2Answer3");
        question2Answers[3] = (String) mapProperties.get("Question2Answer4");

        question3 = (String) mapProperties.get("Question3");
        question3Answers[0] = (String) mapProperties.get("Question3Answer1");
        question3Answers[1] = (String) mapProperties.get("Question3Answer2");
        question3Answers[2] = (String) mapProperties.get("Question3Answer3");
        question3Answers[3] = (String) mapProperties.get("Question3Answer4");



        stage = new Stage(viewport, ComputerScienceRunner.batch);

        Gdx.input.setInputProcessor(stage);

        Table table = new Table();
        table.center();
        table.setFillParent(true);

        questionLabel  = new Label(question1, new Label.LabelStyle(font, new Color(150f/255, 220f/255, 255f/255, 1)));
        questionLabel.setWrap(true);
        table.add(questionLabel).width(700f).colspan(2);
        table.row();

        buttonSkin = new Skin(Gdx.files.internal("skins/button/glassy-ui.json"));

        answer1Button = new TextButton(question1Answers[0], buttonSkin);

        answer1Button.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                game.setPlayScreen(ComputerScienceRunner.playScreen.getCurrentSemester());
                dispose();
                return true;
            }
        });


        table.add(answer1Button).padTop(100);




        answer2Button = new TextButton(question1Answers[1], buttonSkin);

        answer2Button.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                game.setPlayScreen(ComputerScienceRunner.playScreen.getCurrentSemester());
                dispose();
                return true;
            }
        });


        table.add(answer2Button).padTop(100);



        table.row();



        answer3Button = new TextButton(question1Answers[2], buttonSkin);

        answer3Button.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                game.setPlayScreen(ComputerScienceRunner.playScreen.getCurrentSemester());
                dispose();
                return true;
            }
        });


        table.add(answer3Button).padTop(100);




        answer4Button = new TextButton(question1Answers[3], buttonSkin);

        answer4Button.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                game.setPlayScreen(ComputerScienceRunner.playScreen.getCurrentSemester());
                dispose();
                return true;
            }
        });


        table.add(answer4Button).padTop(100);




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
