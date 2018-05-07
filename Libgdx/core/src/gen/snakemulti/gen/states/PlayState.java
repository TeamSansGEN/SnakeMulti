package gen.snakemulti.gen.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import gen.snakemulti.SnakeMulti;
import gen.snakemulti.sprites.Snake;

import java.util.ArrayList;
import java.util.List;

public class PlayState extends State {

    private Texture background;

    private int numberOfPlayers;

    private List<Snake> snakes;

    public PlayState(GameStateManager gsm, int numberOfPlayers) {
        super(gsm);
        this.numberOfPlayers = numberOfPlayers;
        background = new Texture("backgroundLobby.png");
        snakes = new ArrayList<Snake>();

        for(int i = 0; i < numberOfPlayers; i++) {
            snakes.add(new Snake(200, 200, Snake.RIGHT));
        }
    }

    @Override
    protected void handleInput() {

        if(Gdx.input.isKeyJustPressed(Input.Keys.W)) { // Move up
            if(snakes.get(0).getDirection() != Snake.DOWN)
                snakes.get(0).setDirection(Snake.UP);
        }

        if(Gdx.input.isKeyJustPressed(Input.Keys.A)) { // Move left
            if(snakes.get(0).getDirection() != Snake.RIGHT)
                snakes.get(0).setDirection(Snake.LEFT);
        }

        if(Gdx.input.isKeyJustPressed(Input.Keys.S)) { // Move down
            if(snakes.get(0).getDirection() != Snake.UP)
                snakes.get(0).setDirection(Snake.DOWN);
        }

        if(Gdx.input.isKeyJustPressed(Input.Keys.D)) { // Move right
            if(snakes.get(0).getDirection() != Snake.LEFT)
                snakes.get(0).setDirection(Snake.RIGHT);
        }
    }

    @Override
    public void update(float dt) {
        handleInput();

        

        snakes.get(0).update(dt);
    }

    @Override
    public void render(SpriteBatch sb) {
        sb.begin();
        sb.draw(background, 0, 0, SnakeMulti.WIDTH, SnakeMulti.HEIGHT);

        for(int i = 0; i < snakes.get(0).getSize(); i++) {
            sb.draw(snakes.get(0).getTexture().get(i), snakes.get(0).getBodyParts().get(i).x, snakes.get(0).getBodyParts().get(i).y);
        }


        sb.end();
    }

    @Override
    public void dispose() {
        background.dispose();
    }
}
