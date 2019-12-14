package at.ac.univie.computersciencerunner.hud;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import at.ac.univie.computersciencerunner.ComputerScienceRunner;

public class Hud implements Disposable {
    public Stage stage;
    private Viewport viewPort;


    private int semesterCount;
    private Label semesterLabel;
    private Label semesterValue;

    private int ectsCount;
    private Label ectsValue;
    private Texture ects = new Texture(Gdx.files.internal("ects_hud.png"));
    private Image ectsImage;

    private Texture hearts0 = new Texture(Gdx.files.internal("hearts0.png"));
    private Texture hearts1 = new Texture(Gdx.files.internal("hearts1.png"));
    private Texture hearts2 = new Texture(Gdx.files.internal("hearts2.png"));
    private Texture hearts3 = new Texture(Gdx.files.internal("hearts3.png"));

    private Image currentsHeartsImage;

    private int coinCount;
    private Label coinValue;
    private Texture coin = new Texture(Gdx.files.internal("coin_hud.png"));
    private Image coinImage;

    private TextButton pauseButton;

    private Skin blueSkin = new Skin(Gdx.files.internal("skins/button/glassy-ui.json"));

    private final ComputerScienceRunner game;

    public Hud(SpriteBatch spriteBatch, ComputerScienceRunner computerScienceRunner) {



        this.game = computerScienceRunner;

        ectsCount = 0;

        viewPort = new FitViewport(ComputerScienceRunner.WIDTH, ComputerScienceRunner.HEIGHT, new OrthographicCamera());
        stage = new Stage(viewPort, spriteBatch);

        //Gdx.input.setInputProcessor(stage);

        Table table = new Table();
        table.top();

        table.setFillParent(true);

        currentsHeartsImage = new Image(hearts3);

        table.add(currentsHeartsImage).expandX().padTop(10);

        semesterLabel = new Label("Semester", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        semesterValue = new Label("", new Label.LabelStyle(new BitmapFont(), Color.WHITE));

        ectsImage = new Image(ects);
        ectsValue = new Label(String.valueOf(ectsCount), new Label.LabelStyle(new BitmapFont(), Color.WHITE));

        coinImage = new Image(coin);
        coinValue = new Label(String.valueOf(coinCount), new Label.LabelStyle(new BitmapFont(), Color.WHITE));

        table.add(ectsImage).expandX().padTop(10);
        table.add(coinImage).expandX().padTop(10);
        table.add(semesterLabel).expandX().padTop(10);




        pauseButton = new TextButton("Pause", blueSkin.get("small", TextButton.TextButtonStyle.class));

        pauseButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                ComputerScienceRunner.playScreen.pause();
                game.setPauseScreen();
                return true;
            }
        });


        table.add(pauseButton).expandX().padTop(40);



        table.row();







        table.add().expandX().padTop(10);
        table.add(ectsValue).expandX().padTop(10);
        table.add(coinValue).expandX().padTop(10);
        table.add(semesterValue).expandX().padTop(10);










        stage.addActor(table);



    }

    @Override
    public void dispose() {
        stage.dispose();
    }

    public int getEctsCount() {
        return ectsCount;
    }

    public void setEctsCount(int ectsCount) {
        this.ectsCount = ectsCount;
        ectsValue.setText(String.valueOf(ectsCount));
    }

    public int getCoinCount() {
        return coinCount;
    }

    public void setCoinCount(int coinCount) {
        this.coinCount = coinCount;
        coinValue.setText(String.valueOf(coinCount));
    }

    public void setHeartsCount(int heartsCount) {
       if(heartsCount == 0) {
           currentsHeartsImage.setDrawable(new TextureRegionDrawable(hearts0));
       } else if (heartsCount == 1) {
           currentsHeartsImage.setDrawable(new TextureRegionDrawable(hearts1));
       } else if (heartsCount == 2) {
           currentsHeartsImage.setDrawable(new TextureRegionDrawable(hearts2));
       } else if (heartsCount == 3) {
           currentsHeartsImage.setDrawable(new TextureRegionDrawable(hearts3));
       }
    }

    public void setSemesterValue(int semester) {
        semesterValue.setText(String.valueOf(semester));
    }
}
