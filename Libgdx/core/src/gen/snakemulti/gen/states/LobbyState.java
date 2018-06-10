package gen.snakemulti.gen.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import gen.snakemulti.SnakeMulti;

public class LobbyState extends State {

    private Texture background;
    private Texture readyButton;

    private String username;

    public LobbyState(GameStateManager gsm, String username) {
        super(gsm);
        background  = new Texture("backgroundLobby.png");
        readyButton = new Texture("readyButton.png");
        this.username = username;
    }

    @Override
    public void handleInput() {
        if(Gdx.input.justTouched()) {
            gsm.set(new PlayState(gsm, 2));
            dispose(); //free memory
        }
    }

    @Override
    public void update(float dt) {
        handleInput(); //will always be checking for inputs
    }

    @Override
    public void render(SpriteBatch sb) {
        sb.begin(); //open the box

        //put everything to be rendered inside the box
        sb.draw(background,  0, 0, SnakeMulti.WIDTH, SnakeMulti.HEIGHT);
        sb.draw(readyButton, (SnakeMulti.WIDTH / 2) - (readyButton.getWidth() / 2), (SnakeMulti.HEIGHT / 4));

        sb.end(); // close the box

        //everything inside the box will be rendered
    }

    @Override
    public void dispose() {
        background.dispose();
        readyButton.dispose();
    }
}
