package at.ac.univie.computersciencerunner.desktop;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import at.ac.univie.computersciencerunner.ComputerScienceRunner;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();

		config.width = 1280;
		config.height = 720;
		config.fullscreen = false;

		config.addIcon("icon.png", Files.FileType.Internal);

		new LwjglApplication(new ComputerScienceRunner(), config);
	}
}
