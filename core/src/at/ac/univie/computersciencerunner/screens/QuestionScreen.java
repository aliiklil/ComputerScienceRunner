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
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import at.ac.univie.computersciencerunner.ComputerScienceRunner;
import at.ac.univie.computersciencerunner.Question;

public class QuestionScreen implements Screen {

    private Viewport viewport;
    private Stage stage;

    private FreeTypeFontGenerator freeTypeFontGenerator;
    private FreeTypeFontGenerator.FreeTypeFontParameter fontParameter;
    private BitmapFont font;

    private Skin blueSkin = new Skin(Gdx.files.internal("skins/button/glassy-ui.json")); //Blue skin for answer buttons, which are not pressed
    private Skin greenSkin = new Skin(Gdx.files.internal("skins/greenbutton/glassy-ui.json"));//Green skin for answer buttons, which are pressed, which are the correct answer
    private Skin redSkin = new Skin(Gdx.files.internal("skins/redbutton/glassy-ui.json")); //Red skin for answer buttons, which are pressed, which a wrong answer

    private Question[] questions = new Question[3];

    Label questionLabel; //Changes depending on which question the player is currentl

    private final TextButton[] answerButton = new TextButton[4]; //Also changes depending on which question the player is currentl

    private final ComputerScienceRunner game;

    private int currentQuestionIndex = 0; //Can either be 0 or 1 or 2, depending on which question the player is currently

    private int rightAnswersCount; //How many questions were answered correctly by the player

    private long timestampAnswerSelected; //Needed to know, after player pressed an answer, how long correct and right answer should be displayed
    private int timeUntilNextQuestion = 3000; //Time in seconds, after player pressed an answer, until next questions should be displayed

    private boolean changeQuestion; //True when next questions should come

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

        for(int i = 0; i < questions.length; i++) { //The labels of the properties in Tiled start with Question1 and so on

            questions[i]  = new Question();

            questions[i].setQuestion((String) mapProperties.get("Question" + i));

            String[] questionAnswers = new String[4];

            questionAnswers[0] = (String) mapProperties.get("Question" + i + "Answer0");
            questionAnswers[1] = (String) mapProperties.get("Question" + i + "Answer1");
            questionAnswers[2] = (String) mapProperties.get("Question" + i + "Answer2");
            questionAnswers[3] = (String) mapProperties.get("Question" + i + "Answer3");

            questions[i].setAnswers(questionAnswers);

            questions[i].setRightAnswerIndex(Integer.valueOf( (String) mapProperties.get("Question" + i + "RightAnswer")));


        }

        stage = new Stage(viewport, ComputerScienceRunner.batch);

        Gdx.input.setInputProcessor(stage);

        Table table = new Table();
        table.center();
        table.setFillParent(true);

        questionLabel  = new Label(questions[currentQuestionIndex].getQuestion(), new Label.LabelStyle(font, new Color(150f/255, 220f/255, 255f/255, 1)));
        questionLabel.setWrap(true);
        questionLabel.setAlignment(Align.center);
        table.add(questionLabel).colspan(2);
        table.row();

table.debug();


        for(int i = 0; i < 4; i++) {

            answerButton[i] = new TextButton(questions[currentQuestionIndex].getAnswers()[i], blueSkin.get("small", TextButton.TextButtonStyle.class));

            final int index = i; //Needed because in the anonymous function, its not possible to access i (because it needs to be final)

            answerButton[i].addListener(new InputListener() {
                @Override
                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                    int rightAnswerIndex = questions[currentQuestionIndex].getRightAnswerIndex();
                    if(rightAnswerIndex == index) {
                        answerButton[index].setStyle(greenSkin.get("small", TextButton.TextButtonStyle.class));
                        rightAnswersCount++;
                    } else {
                        answerButton[index].setStyle(redSkin.get("small", TextButton.TextButtonStyle.class));
                        answerButton[rightAnswerIndex].setStyle(greenSkin.get("small", TextButton.TextButtonStyle.class));
                    }
                    timestampAnswerSelected = System.currentTimeMillis();

                    if(currentQuestionIndex < 2) {
                        currentQuestionIndex++;
                    }
                    changeQuestion = true;
                    return true;
                }
            });

            table.add(answerButton[i]).width(600).padTop(100);

            if(i == 1) {
                table.row();
            }

        }

        stage.addActor(table);

    }

    @Override
    public void render(float delta) {

        update(delta);

        Gdx.gl.glClearColor(21.0f/255, 80.0f/255, 80.0f/255, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.draw();

    }

    private void update(float delta) {

        if(changeQuestion && System.currentTimeMillis() - timestampAnswerSelected > timeUntilNextQuestion) {
            changeQuestion = false;

            questionLabel.setText(questions[currentQuestionIndex].getQuestion());

            for(int i = 0; i < 4; i++) {
                answerButton[i].setText(questions[currentQuestionIndex].getAnswers()[i]);
                answerButton[i].setStyle(blueSkin.get("small", TextButton.TextButtonStyle.class));
            }

        }

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
