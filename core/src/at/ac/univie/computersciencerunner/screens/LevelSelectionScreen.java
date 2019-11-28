package at.ac.univie.computersciencerunner.screens;

import com.badlogic.gdx.Gdx;
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

    private Skin levelSelectButtonSkin;
    private Skin buttonSkin;

    private TextButton semester1Button;
    private TextButton semester2Button;
    private TextButton semester3Button;
    private TextButton semester4Button;
    private TextButton semester5Button;
    private TextButton semester6Button;

    private TextButton semester7Button;
    private TextButton semester8Button;
    private TextButton semester9Button;
    private TextButton semester10Button;

    private TextButton backButton;

    private final ComputerScienceRunner game;

    public LevelSelectionScreen(ComputerScienceRunner computerScienceRunner) {

        this.game = computerScienceRunner;

        viewport = new FitViewport(ComputerScienceRunner.WIDTH, ComputerScienceRunner.HEIGHT, new OrthographicCamera());



        freeTypeFontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("basicFont.ttf"));
        fontParameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        fontParameter.size = 80;
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



        Label selectSemesterLabel  = new Label("Select Semester", new Label.LabelStyle(font, new Color(150f/255, 220f/255, 255f/255, 1)));
        table.add(selectSemesterLabel).fillX();
        table.row();


        stage.addActor(table);


        table = new Table();
        table.top();
        table.setFillParent(true);





        levelSelectButtonSkin = new Skin(Gdx.files.internal("skins/level/glassy-ui.json"));



        semester1Button = new TextButton("1", levelSelectButtonSkin);

        semester1Button.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                System.out.println("Semester 1");
                dispose();
                return true;
            }
        });

        table.padTop(100);
        table.add(semester1Button);




        semester2Button = new TextButton("2", levelSelectButtonSkin);

        semester2Button.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                System.out.println("Semester 2");
                dispose();
                return true;
            }
        });


        table.add(semester2Button);


        table.row();

        semester3Button = new TextButton("3", levelSelectButtonSkin);

        semester3Button.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                System.out.println("Semester 3");
                dispose();
                return true;
            }
        });


        table.add(semester3Button);





        semester4Button = new TextButton("4", levelSelectButtonSkin);

        semester4Button.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                System.out.println("Semester 4");
                dispose();
                return true;
            }
        });


        table.add(semester4Button);

table.row();



        semester5Button = new TextButton("5", levelSelectButtonSkin);

        semester5Button.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                System.out.println("Semester 5");
                dispose();
                return true;
            }
        });


        table.add(semester5Button);



        semester6Button = new TextButton("6", levelSelectButtonSkin);

        semester6Button.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                System.out.println("Semester 6");
                dispose();
                return true;
            }
        });


        table.add(semester6Button);



        table.row();







        semester7Button = new TextButton("7", levelSelectButtonSkin);

        semester7Button.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                System.out.println("Semester 7");
                dispose();
                return true;
            }
        });


        table.add(semester7Button);




        semester8Button = new TextButton("8", levelSelectButtonSkin);

        semester8Button.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                System.out.println("Semester 8");
                dispose();
                return true;
            }
        });


        table.add(semester8Button);


        table.row();

        semester9Button = new TextButton("9", levelSelectButtonSkin);

        semester9Button.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                System.out.println("Semester 9");
                dispose();
                return true;
            }
        });


        table.add(semester9Button);



        semester10Button = new TextButton("10", levelSelectButtonSkin);

        semester10Button.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                System.out.println("Semester 10");
                dispose();
                return true;
            }
        });

        table.add(semester10Button);


table.row();










        buttonSkin = new Skin(Gdx.files.internal("skins/button/glassy-ui.json"));

        backButton = new TextButton("BACK", buttonSkin);

        backButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                game.setMainMenuScreen();
                dispose();
                return true;
            }
        });


        table.add(backButton).colspan(2);


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
