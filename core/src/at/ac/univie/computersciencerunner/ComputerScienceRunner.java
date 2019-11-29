package at.ac.univie.computersciencerunner;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;

import at.ac.univie.computersciencerunner.screens.GameOverScreen;
import at.ac.univie.computersciencerunner.screens.LevelSelectionScreen;
import at.ac.univie.computersciencerunner.screens.MainMenuScreen;
import at.ac.univie.computersciencerunner.screens.OptionsScreen;
import at.ac.univie.computersciencerunner.screens.PlayScreen;
import at.ac.univie.computersciencerunner.screens.QuestionScreen;

public class ComputerScienceRunner extends Game {

	public static SpriteBatch batch;

	public static final int WIDTH = 1280;
	public static final int HEIGHT = 720;

	public static final float PPM = 100; //pixels per meter

	public static final short PLAYER_HEAD_BIT = 2;
	public static final short PLAYER_BIT = 1;
	public static final short PLAYER_FEET_BIT = 4;
	public static final short GROUND_BIT = 8;
	public static final short BRICK_BIT = 16;
	public static final short ECTS_BIT = 32;
	public static final short ECTS_BRICK_BIT = 64;
	public static final short HEART_BIT = 128;
	public static final short HEART_BRICK_BIT = 256;
	public static final short INFO_BRICK_BIT = 512;
	public static final short WALL_BIT = 1024;
    public static final short COIN_BIT = 2048;
    public static final short COIN_BRICK_BIT = 4096;
    public static final short GOAL_BIT = 8192;

	public static MainMenuScreen mainMenuScreen;
	public static LevelSelectionScreen levelSelectionScreen;
	public static OptionsScreen optionsScreen;
	public static PlayScreen playScreen;
	public static GameOverScreen gameOverScreen;
    public static QuestionScreen questionScreen;

	@Override
	public void create () {

		batch = new SpriteBatch();

		mainMenuScreen = new MainMenuScreen(this);
		levelSelectionScreen = new LevelSelectionScreen(this);
		optionsScreen = new OptionsScreen(this);
		playScreen = new PlayScreen(this, 1);
		gameOverScreen = new GameOverScreen(this);

        questionScreen = new QuestionScreen(this);

		setScreen(mainMenuScreen);
	}

	@Override
	public void render () {
		super.render();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
	}

	public void setMainMenuScreen() {
		setScreen(mainMenuScreen);
	}

	public void setLevelSelectionScreen() {
		setScreen(levelSelectionScreen);
	}

	public void setOptionsScreen() {
		setScreen(optionsScreen);
	}

	public void setPlayScreen(int semester) {
		playScreen = new PlayScreen(this, semester);
		setScreen(playScreen);
	}

	public void setGameOverScreen() {
		setScreen(gameOverScreen);
	}

    public void setQuestionScreen() {
        setScreen(questionScreen);
    }

}