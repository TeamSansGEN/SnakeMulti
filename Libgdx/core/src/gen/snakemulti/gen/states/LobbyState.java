package gen.snakemulti.gen.states;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import gen.snakemulti.GameConstants;

public class LobbyState extends State {

    private Texture background;

    private String username;

    public LobbyState(GameStateManager gsm, String username) {
        super(gsm);
        background = new Texture("backgroundLobby3.png");
        this.username = username;
    }

    @Override
    protected void handleInput() {

    }

    @Override
    public void update(float dt) {

    }

    @Override
    public void render(SpriteBatch sb) {
        sb.begin();

        sb.draw(background, 0, 0, GameConstants.WIDTH, GameConstants.HEIGHT);

        sb.end();
    }

    @Override
    public void dispose() {

    }

    private void waitForGo() {

    }
}
