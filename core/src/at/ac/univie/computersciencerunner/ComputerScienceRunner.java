package at.ac.univie.computersciencerunner;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import at.ac.univie.computersciencerunner.screens.PlayScreen;

public class ComputerScienceRunner extends Game {

	public static SpriteBatch batch;

	public static final int WIDTH = 400;
	public static final int HEIGHT = 208;

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