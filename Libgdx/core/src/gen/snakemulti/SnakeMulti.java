package gen.snakemulti;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import gen.snakemulti.gen.states.GameStateManager;
import gen.snakemulti.gen.states.LoginState;

public class SnakeMulti extends ApplicationAdapter {

    public static final int WIDTH  = 960;
    public static final int HEIGHT = 640;

    public static final String TITLE = "Snake Multi";

    private GameStateManager gsm;
    private SpriteBatch batch; //heavy, should be only one

    private String playerNum;
    private String serverIP;

	public SnakeMulti(String playerNum, String serverIP) {
	    this.playerNum = playerNum;
	    this.serverIP = serverIP;
    }

	@Override
	public void create() {
        batch = new SpriteBatch();
        gsm = new GameStateManager();
        Gdx.gl.glClearColor(1, 0, 0, 1);

        gsm.push(new LoginState(gsm, playerNum, serverIP));
	}

	// render method all the time in loop
	@Override
	public void render() {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        gsm.update(1/60);
        gsm.render(batch);
	}
}
