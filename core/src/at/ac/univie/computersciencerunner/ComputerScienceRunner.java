package at.ac.univie.computersciencerunner;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import at.ac.univie.computersciencerunner.screens.GameOverScreen;
import at.ac.univie.computersciencerunner.screens.LevelSelectionScreen;
import at.ac.univie.computersciencerunner.screens.MainMenuScreen;
import at.ac.univie.computersciencerunner.screens.OptionsScreen;
import at.ac.univie.computersciencerunner.screens.PauseScreen;
import at.ac.univie.computersciencerunner.screens.PlayScreen;
import at.ac.univie.computersciencerunner.screens.QuestionScreen;
import at.ac.univie.computersciencerunner.screens.SemesterCompletedScreen;

public class ComputerScienceRunner extends Game {

	public static SpriteBatch batch;

	public static final int WIDTH = 800;
	public static final int HEIGHT = 480;

	public static final float PPM = 100; //pixels per meter

	public static final short PLAYER_BIT = 1;
	public static final short PLAYER_HEAD_BIT = 2;
	public static final short PLAYER_FEET_BIT = 4;
	public static final short GROUND_BIT = 8;
	public static final short BRICK_BIT = 16; //For Brick, ECTSBrick, HeartBrick, InfoBrick and CoinBrick
	public static final short COLLECTIBLE_BIT = 32; // ECTS, Coins, Hearts
	public static final short WALL_BIT = 64;
    public static final short GOAL_BIT = 128;
    public static final short ONEWAY_PLATFORM_BIT = 256;
	public static final short BUG_HEAD_BIT = 512;
	public static final short BUG_BODY_BIT = 1024;
	public static final short BUG_LEFT_SENSOR_BIT = 2048;
	public static final short BUG_RIGHT_SENSOR_BIT = 4096;
	public static final short SPIKES_BIT = 8192;
	public static final short TRAMPOLINE_BIT = 16384;

	public static MainMenuScreen mainMenuScreen;
	public static LevelSelectionScreen levelSelectionScreen;
	public static OptionsScreen optionsScreen;
	public static PlayScreen playScreen;
	public static GameOverScreen gameOverScreen;
    public static QuestionScreen questionScreen;
    public static SemesterCompletedScreen semesterCompletedScreen;
	public static PauseScreen pauseScreen;

	public static AssetManager assetManager;

	@Override
	public void create () {

		batch = new SpriteBatch();


		assetManager = new AssetManager();
		assetManager.load("audio/music/menu.mp3", Music.class);
		assetManager.load("audio/music/level1.mp3", Music.class);
		assetManager.load("audio/music/level2.mp3", Music.class);
		assetManager.load("audio/music/level3.mp3", Music.class);
		assetManager.load("audio/music/level4.mp3", Music.class);
		assetManager.load("audio/music/level5.mp3", Music.class);
		assetManager.load("audio/music/level6.mp3", Music.class);
		assetManager.load("audio/music/level7.mp3", Music.class);
		assetManager.load("audio/music/level8.mp3", Music.class);
		assetManager.load("audio/music/level9.mp3", Music.class);
		assetManager.load("audio/music/level10.mp3", Music.class);

		assetManager.load("audio/sounds/jump.mp3", Sound.class);
		assetManager.load("audio/sounds/coin.mp3", Sound.class);
		assetManager.load("audio/sounds/heart.mp3", Sound.class);
		assetManager.load("audio/sounds/ects.mp3", Sound.class);
        assetManager.load("audio/sounds/damageTaken.mp3", Sound.class);


		assetManager.finishLoading();


		mainMenuScreen = new MainMenuScreen(this);
		levelSelectionScreen = new LevelSelectionScreen(this);
		optionsScreen = new OptionsScreen(this);
		playScreen = new PlayScreen(this, 1);
		gameOverScreen = new GameOverScreen(this);

        questionScreen = new QuestionScreen(this);
        semesterCompletedScreen = new SemesterCompletedScreen(this);
		pauseScreen = new PauseScreen(this);

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

	public void setPlayScreen(int semester) { //Only used when new level/semester is started
		playScreen = new PlayScreen(this, semester);
		setScreen(playScreen);
	}

	public void unpauseScreen() { //Only used when unpausing from the pause menu
		setScreen(playScreen);
	}

	public void setGameOverScreen() {
		setScreen(gameOverScreen);
	}

    public void setQuestionScreen() {
        setScreen(questionScreen);
    }

    public void setSemesterCompletedScreen() {
        setScreen(semesterCompletedScreen);
    }

	public void setPauseScreen() {
		setScreen(pauseScreen);
	}

	/*
	public AssetManager getAssetManager() {
	    return assetManager;
    }
*/

}