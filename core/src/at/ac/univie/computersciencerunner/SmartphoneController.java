package at.ac.univie.computersciencerunner;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;


public class SmartphoneController {

    private boolean upPressed;

    private boolean leftPressed;
    private boolean rightPressed;

    private Viewport viewPort;
    private Stage stage;

    private TextButton pauseButton;

    private Skin blueSkin = new Skin(Gdx.files.internal("skins/button/glassy-ui.json"));

    private final ComputerScienceRunner game;

    public SmartphoneController(ComputerScienceRunner computerScienceRunner){

        this.game = computerScienceRunner;

        OrthographicCamera camera = new OrthographicCamera();
        viewPort = new FitViewport(ComputerScienceRunner.WIDTH, ComputerScienceRunner.HEIGHT, camera);
        stage = new Stage(this.viewPort, ComputerScienceRunner.batch);

        Gdx.input.setInputProcessor(stage);

        stage.addListener(new InputListener(){

            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                switch(keycode){
                    case Input.Keys.UP:
                        upPressed = true;
                        break;
                    case Input.Keys.LEFT:
                        leftPressed = true;
                        break;
                    case Input.Keys.RIGHT:
                        rightPressed = true;
                        break;
                }
                return true;
            }

            @Override
            public boolean keyUp(InputEvent event, int keycode) {
                switch(keycode){
                    case Input.Keys.UP:
                        upPressed = false;
                        break;
                    case Input.Keys.LEFT:
                        leftPressed = false;
                        break;
                    case Input.Keys.RIGHT:
                        rightPressed = false;
                        break;
                }
                return true;
            }
        });

        pauseButton = new TextButton("Pause", blueSkin.get("small", TextButton.TextButtonStyle.class));

        pauseButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if(!ComputerScienceRunner.playScreen.getInfoWidget().isCurrentlyDisplayed()) { //Player shouldn't be able to pause while reading information from an infoBrick
                    ComputerScienceRunner.playScreen.pause();
                    game.setPauseScreen();
                }
                return true;
            }
        });


/*
        Image upImage = new Image(new Texture("controls/up.png"));
        upImage.setSize(80, 80);
        upImage.addListener(new InputListener() {

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                upPressed = true;
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                upPressed = false;
            }
        });

        Image rightImage = new Image(new Texture("controls/right.png"));
        rightImage.setSize(80, 80);
        rightImage.addListener(new InputListener() {

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                rightPressed = true;
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                rightPressed = false;
            }
        });

        Image leftImage = new Image(new Texture("controls/left.png"));
        leftImage.setSize(80, 80);
        leftImage.addListener(new InputListener() {

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                leftPressed = true;
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                leftPressed = false;
            }
        });
*/



        Table table = new Table();


        table.top().right();
        table.setFillParent(true);

        table.add(pauseButton).padTop(40).padRight(50).colspan(3).right();




        table.row();


/*
        table.add(leftImage).size(leftImage.getWidth(), leftImage.getHeight()).expandX().padLeft(10).padTop(290);
        table.add(rightImage).size(rightImage.getWidth(), rightImage.getHeight()).expandX().padLeft(10).padTop(290);
        table.add(upImage).size(upImage.getWidth(), upImage.getHeight()).expandX().padLeft(340).padTop(290);
*/

        stage.addActor(table);
    }

    public void setInputProcessorToStage() {
        Gdx.input.setInputProcessor(stage);
    }

    public void draw(){
        stage.draw();
    }

    public boolean isUpPressed() {
        return upPressed;
    }

    public boolean isUpJustPressed() {
        if(upPressed) {
            upPressed = false; //Because after checking if up was pressed, the key event should be "consumed"
            return true;
        } else {
            return false;
        }
    }

    public boolean isLeftPressed() {
        return leftPressed;
    }

    public boolean isRightPressed() {
        return rightPressed;
    }

    public void resize(int width, int height){
        viewPort.update(width, height);
    }
}