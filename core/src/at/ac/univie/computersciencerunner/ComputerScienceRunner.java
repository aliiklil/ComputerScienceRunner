package at.ac.univie.computersciencerunner;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import at.ac.univie.computersciencerunner.screens.PlayScreen;

public class ComputerScienceRunner extends Game {

	public static SpriteBatch batch;

	public static final int WIDTH = 1000;
	public static final int HEIGHT = 500;

	public static final float PPM = 100; //pixels per meter

	public static final short PLAYER_BIT = 1;
	public static final short GROUND_BIT = 2;
	public static final short ECTS_BIT = 4;
	public static final short BRICK_BIT = 8;

	public static PlayScreen playScreen;

	@Override
	public void create () {
		batch = new SpriteBatch();
		playScreen = new PlayScreen();
		setScreen(playScreen);
	}

	@Override
	public void render () {
		super.render();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
	}

}