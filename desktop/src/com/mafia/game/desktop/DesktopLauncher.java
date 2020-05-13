package com.mafia.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.mafia.game.Mafia;

public class DesktopLauncher {
	public static void main (String[] arg) {
		//System.setProperty("user.name","Tirro");
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = Mafia.WIDTH;
		config.height = Mafia.HEIGHT;
		config.title = Mafia.TITLE;
		new LwjglApplication(new Mafia(), config);
	}
}
