package gen.snakemulti.gen.states;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public abstract class State {
    protected GameStateManager gsm;

    public State(GameStateManager gsm) {
        this.gsm = gsm;
    }

    protected abstract void handleInput();

    public abstract void update(float dt);

    public abstract void render(SpriteBatch sb);

    public abstract void dispose();
}
