package at.ac.univie.computersciencerunner.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import at.ac.univie.computersciencerunner.ComputerScienceRunner;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = 2340;
		config.height = 1080;
		new LwjglApplication(new ComputerScienceRunner(), config);
	}
}
