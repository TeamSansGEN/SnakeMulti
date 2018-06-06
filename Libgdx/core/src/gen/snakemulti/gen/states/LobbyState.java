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

import java.net.Socket;

public class LobbyState extends State {

    private Texture background;
    private ImageButton readyButton;

    private Socket socket;
    private int playerConnected = 0;
    private final int NUMBER_OF_PLAYERS = 2;


    public LobbyState(GameStateManager gsm) {
        super(gsm);
        background  = new Texture("backgroundLobby.png");

        Texture playTexture = new Texture("readyButton.png");
        Drawable drawable = new TextureRegionDrawable(new TextureRegion(playTexture));
        readyButton = new ImageButton(drawable);
        readyButton.setPosition(400,300);

        Stage stage= new Stage();
        Gdx.input.setInputProcessor(stage);
        stage.addActor(readyButton);

        readyButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y){
                goToPlayState();
            }
        });

    }

    public void goToPlayState(){
        playerConnected++;
        //while(playerConnected < NUMBER_OF_PLAYERS)
        //{}
        gsm.set(new PlayState(gsm,1));
    }

    @Override
    public void handleInput() {

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
        BitmapFont font = new BitmapFont();
        font.draw(sb, "You are alone :( , wait for minimum two player", SnakeMulti.WIDTH/80,(SnakeMulti.HEIGHT - SnakeMulti.HEIGHT/8) );
        readyButton.draw(sb,5);
        sb.end(); // close the box

        //everything inside the box will be rendered

    }

    @Override
    public void dispose() {

    }
}
