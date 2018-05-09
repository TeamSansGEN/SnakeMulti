package com.mygdx.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.mygdx.game.MyGdxGame;
import server.*;

public class DesktopLauncher {
	public static void main (String[] arg) {
		//LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		//new LwjglApplication(new MyGdxGame(), config);

		System.setProperty("java.util.logging.SimpleFormatter.format", "%5$s %n");

		MTSnakeServer server = new MTSnakeServer(2828, 2829);
		server.serveClients();

	}
}
