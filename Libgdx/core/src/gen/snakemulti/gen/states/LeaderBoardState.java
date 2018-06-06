package gen.snakemulti.gen.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import gen.snakemulti.SnakeMulti;
import gen.snakemulti.*;

public class LeaderBoardState extends State {

    private Texture background;
    private Texture test;


    public LeaderBoardState(GameStateManager gsm) {
        super(gsm);
        background  = new Texture("backgroundLobby.png");

    }

    @Override
    public void handleInput() {

    }

    @Override
    public void update(float dt) {
        handleInput(); //will always be checking for inputs
    }



    BitmapFont font = new BitmapFont();

    @Override
    public void render(SpriteBatch sb) {

        ConnexionSQL sql = new ConnexionSQL();


       // int score = sql.recupScore(1);

        sb.begin(); //open the box



        sb.draw(background,  0, 0, SnakeMulti.WIDTH, SnakeMulti.HEIGHT);
        //font.draw(sb, Integer.toString(score), SnakeMulti.WIDTH/2,SnakeMulti.HEIGHT/2);
        for (int i = 1; i <= 10; i++) {
            font.draw(sb, i + ":", SnakeMulti.WIDTH / 2 - 100, SnakeMulti.HEIGHT - 300 - i * 30);
        }

        sb.end(); // close the box

        //everything inside the box will be rendered

    }

    @Override
    public void dispose() {

    }
}
