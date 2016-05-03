package com.romppainen.aleksi.tetris.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.romppainen.aleksi.tetris.TetrisSquared;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = (int)TetrisSquared.SCREEN_WIDTH;
		config.height = (int)TetrisSquared.SCREEN_HEIGHT;
		new LwjglApplication(new TetrisSquared(), config);
	}
}
