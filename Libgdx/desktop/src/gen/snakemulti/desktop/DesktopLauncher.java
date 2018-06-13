package gen.snakemulti.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import gen.snakemulti.SnakeMulti;

public class DesktopLauncher {
	public static void main (String[] arg) {
		if(arg.length >= 2) {
            LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();

            config.title = SnakeMulti.TITLE;
            config.width = SnakeMulti.WIDTH;
            config.height = SnakeMulti.HEIGHT;

            new LwjglApplication(new SnakeMulti(arg[0], arg[1]), config);
		}
	}
}
