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
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import at.ac.univie.computersciencerunner.ComputerScienceRunner;

public class CreditsScreen implements Screen {

    private Viewport viewport;
    private Stage stage;

    private FreeTypeFontGenerator freeTypeFontGenerator;
    private FreeTypeFontGenerator.FreeTypeFontParameter fontParameter;
    private BitmapFont font;

    private Skin blueSkin = new Skin(Gdx.files.internal("skins/button/glassy-ui.json"));

    private TextButton backButton;

    private final ComputerScienceRunner game;

    public CreditsScreen(ComputerScienceRunner computerScienceRunner) {

        this.game = computerScienceRunner;

        viewport = new FitViewport(ComputerScienceRunner.WIDTH, ComputerScienceRunner.HEIGHT, new OrthographicCamera());



        freeTypeFontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("basicFont.ttf"));
        fontParameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        fontParameter.size = 16;
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
        table.padLeft(70);
        table.setFillParent(true);



        Label developerLabel  = new Label("Entwickler: Ali Iklil", new Label.LabelStyle(font, new Color(150f/255, 220f/255, 255f/255, 1)));
        table.add(developerLabel).expandX().top().padTop(10).colspan(2).padLeft(80);
        table.row();

        Label supervisorLabel  = new Label("Betreuer: Helmut Hlavacs", new Label.LabelStyle(font, new Color(150f/255, 220f/255, 255f/255, 1)));
        table.add(supervisorLabel).expandX().top().padTop(10).colspan(2).padLeft(140);
        table.row();

        Label musicLabel  = new Label("Musik:", new Label.LabelStyle(font, new Color(150f/255, 220f/255, 255f/255, 1)));
        table.add(musicLabel).expandX().top().padTop(20).colspan(2);
        table.row();

        Label label1  = new Label("CodeManu", new Label.LabelStyle(font, new Color(150f/255, 220f/255, 255f/255, 1)));

        label1.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.net.openURI("https://opengameart.org/content/platformer-game-music-pack");
            }
        });
        table.add(label1).expandX().top().padTop(10).colspan(2);

        Label label2  = new Label("CC-BY 3.0", new Label.LabelStyle(font, new Color(150f/255, 220f/255, 255f/255, 1)));
        label2.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.net.openURI("https://creativecommons.org/licenses/by/3.0/");
            }
        });
        table.add(label2).expandX().top().padTop(10).colspan(2);


        table.row();



        Label label3  = new Label("CodeManu", new Label.LabelStyle(font, new Color(150f/255, 220f/255, 255f/255, 1)));

        label3.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.net.openURI("https://opengameart.org/content/orchestral-adventure");
            }
        });
        table.add(label3).expandX().top().padTop(10).colspan(2);

        Label label4  = new Label("CC-BY 3.0", new Label.LabelStyle(font, new Color(150f/255, 220f/255, 255f/255, 1)));
        label4.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.net.openURI("https://creativecommons.org/licenses/by/3.0/");
            }
        });
        table.add(label4).expandX().top().padTop(10).colspan(2);



        table.row();




        Label label5  = new Label("CodeManu", new Label.LabelStyle(font, new Color(150f/255, 220f/255, 255f/255, 1)));

        label5.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.net.openURI("https://opengameart.org/content/8bit-adventure");
            }
        });
        table.add(label5).expandX().top().padTop(10).colspan(2);

        Label label6  = new Label("CC-BY 3.0", new Label.LabelStyle(font, new Color(150f/255, 220f/255, 255f/255, 1)));
        label6.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.net.openURI("https://creativecommons.org/licenses/by/3.0/");
            }
        });
        table.add(label6).expandX().top().padTop(10).colspan(2);







        table.row();




        Label label7  = new Label("Tom Peter", new Label.LabelStyle(font, new Color(150f/255, 220f/255, 255f/255, 1)));

        label7.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.net.openURI("https://opengameart.org/content/8bit-style-music");
            }
        });
        table.add(label7).expandX().top().padTop(10).colspan(2);

        Label label8  = new Label("CC-BY-SA 3.0", new Label.LabelStyle(font, new Color(150f/255, 220f/255, 255f/255, 1)));
        label8.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.net.openURI("https://creativecommons.org/licenses/by-sa/3.0/");
            }
        });
        table.add(label8).expandX().top().padTop(10).colspan(2);





        table.row();


        Label soundEffectsLabel  = new Label("Sounds:", new Label.LabelStyle(font, new Color(150f/255, 220f/255, 255f/255, 1)));
        table.add(soundEffectsLabel).expandX().top().padTop(20).colspan(2);
        table.row();




        table.row();


        Label label9  = new Label("dklon", new Label.LabelStyle(font, new Color(150f/255, 220f/255, 255f/255, 1)));

        label9.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.net.openURI("https://opengameart.org/content/platformer-jumping-sounds");
            }
        });
        table.add(label9).expandX().top().padTop(10).colspan(2);

        Label label10  = new Label("CC-BY 3.0", new Label.LabelStyle(font, new Color(150f/255, 220f/255, 255f/255, 1)));
        label10.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.net.openURI("https://creativecommons.org/licenses/by/3.0/");
            }
        });
        table.add(label10).expandX().top().padTop(10).colspan(2);




        table.row();


        Label label11  = new Label("bart", new Label.LabelStyle(font, new Color(150f/255, 220f/255, 255f/255, 1)));

        label11.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.net.openURI("https://opengameart.org/content/8-bit-platformer-sfx");
            }
        });
        table.add(label11).expandX().top().padTop(10).colspan(2);

        Label label12  = new Label("CC-BY 3.0", new Label.LabelStyle(font, new Color(150f/255, 220f/255, 255f/255, 1)));
        label12.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.net.openURI("https://creativecommons.org/licenses/by/3.0/");
            }
        });
        table.add(label12).expandX().top().padTop(10).colspan(2);




        table.row();


        Label label13  = new Label("jobromedia", new Label.LabelStyle(font, new Color(150f/255, 220f/255, 255f/255, 1)));

        label13.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.net.openURI("https://opengameart.org/content/level-finish-fanfares");
            }
        });
        table.add(label13).expandX().top().padTop(10).colspan(2);

        Label label14  = new Label("CC-BY-SA 3.0", new Label.LabelStyle(font, new Color(150f/255, 220f/255, 255f/255, 1)));
        label14.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.net.openURI("https://creativecommons.org/licenses/by-sa/3.0/");
            }
        });
        table.add(label14).expandX().top().padTop(10).colspan(2);


        table.row();


        Label label15  = new Label("remaxim", new Label.LabelStyle(font, new Color(150f/255, 220f/255, 255f/255, 1)));
        label15.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.net.openURI("https://opengameart.org/content/win-sound-1");
            }
        });
        table.add(label15).expandX().top().padTop(10).colspan(2);

        Label label16  = new Label("CC-BY-SA 3.0", new Label.LabelStyle(font, new Color(150f/255, 220f/255, 255f/255, 1)));
        label16.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.net.openURI("https://creativecommons.org/licenses/by-sa/3.0/");
            }
        });
        table.add(label16).expandX().top().padTop(10).colspan(2);

        Label label17  = new Label("GPL 3.0", new Label.LabelStyle(font, new Color(150f/255, 220f/255, 255f/255, 1)));
        label17.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.net.openURI("http://www.gnu.org/licenses/gpl-3.0.html");
            }
        });
        table.add(label17).expandX().top().padTop(10).colspan(2);

        Label label18  = new Label("GPL 2.0", new Label.LabelStyle(font, new Color(150f/255, 220f/255, 255f/255, 1)));
        label18.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.net.openURI("http://www.gnu.org/licenses/old-licenses/gpl-2.0.html");
            }
        });
        table.add(label18).expandX().top().padTop(10).colspan(2);






        table.row();


        Label label19  = new Label("remaxim", new Label.LabelStyle(font, new Color(150f/255, 220f/255, 255f/255, 1)));

        label19.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.net.openURI("https://opengameart.org/content/bad-sound-2");
            }
        });
        table.add(label19).expandX().top().padTop(10).colspan(2);

        Label label20 = new Label("CC-BY-SA 3.0", new Label.LabelStyle(font, new Color(150f/255, 220f/255, 255f/255, 1)));
        label20.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.net.openURI("https://creativecommons.org/licenses/by-sa/3.0/");
            }
        });
        table.add(label20).expandX().top().padTop(10).colspan(2);

        Label label21  = new Label("GPL 3.0", new Label.LabelStyle(font, new Color(150f/255, 220f/255, 255f/255, 1)));
        label21.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.net.openURI("http://www.gnu.org/licenses/gpl-3.0.html");
            }
        });
        table.add(label21).expandX().top().padTop(10).colspan(2);

        Label label22  = new Label("GPL 2.0", new Label.LabelStyle(font, new Color(150f/255, 220f/255, 255f/255, 1)));
        label22.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.net.openURI("http://www.gnu.org/licenses/old-licenses/gpl-2.0.html");
            }
        });
        table.add(label22).expandX().top().padTop(10).colspan(2);







        backButton = new TextButton("Zur#ck", blueSkin.get("small", TextButton.TextButtonStyle.class));

        backButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                game.setMainMenuScreen();
                dispose();
                return true;
            }
        });


        table.add(backButton).bottom().right().expandY().expandX().padBottom(20).padRight(180);

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
