package gen.snakemulti.gen.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import gen.snakemulti.GameConstants;
import gen.snakemulti.SnakeMulti;

import java.net.Socket;

public class MenuState extends State {

    private Texture background;

    private ImageButton playButton;
    private ImageButton leaderboardButton;
    private ImageButton exitButton;

    private Stage stage;

    private String username;

    private String playerNum;
    private String serverIP;

    private Socket clientSocket;

    public MenuState(final GameStateManager gsm, final String username, final Socket clientSocket, final String playerNum, final String serverIP) {
        super(gsm);
        background = new Texture("backgroundLobby3.png");
        this.username = username;
        this.clientSocket = clientSocket;
        this.playerNum = playerNum;
        this.serverIP = serverIP;

        Texture playButtonTexture = new Texture("playButton.png");
        Texture leaderboardButtonTexture = new Texture("leaderboardButton.png");
        Texture exitButtonTexture = new Texture("exitButton.png");

        Drawable playButtonDrawable = new TextureRegionDrawable(new TextureRegion(playButtonTexture));
        Drawable leaderboardButtonDrawable = new TextureRegionDrawable(new TextureRegion(leaderboardButtonTexture));
        Drawable exitButtonDrawable = new TextureRegionDrawable(new TextureRegion(exitButtonTexture));

        playButton = new ImageButton(playButtonDrawable);
        leaderboardButton = new ImageButton(leaderboardButtonDrawable);
        exitButton = new ImageButton(exitButtonDrawable);

        playButton.setPosition(GameConstants.WIDTH / 2 - playButtonTexture.getWidth() / 2, 350);
        leaderboardButton.setPosition(GameConstants.WIDTH / 2 - leaderboardButtonTexture.getWidth() / 2, 250);
        exitButton.setPosition(GameConstants.WIDTH / 2 - exitButtonTexture.getWidth() / 2, 150);

        playButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                //gsm.set(new LobbyState(gsm, username, clientSocket));
                gsm.set(new PlayState(gsm, playerNum, 2, serverIP));
                dispose();
            }
        });

        leaderboardButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                //gsm.set(new LeaderboardState(gsm));
                dispose();
            }
        });

        exitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                dispose();
                Gdx.app.exit();
            }
        });


        stage = new Stage();

        stage.addActor(playButton);
        stage.addActor(leaderboardButton);
        stage.addActor(exitButton);

        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void handleInput() {

    }

    @Override
    public void update(float dt) {

    }

    @Override
    public void render(SpriteBatch sb) {
        sb.begin(); //open the box

        // put everything to be rendered inside the box
        // everything inside the box will be rendered
        sb.draw(background, 0, 0, SnakeMulti.WIDTH, SnakeMulti.HEIGHT);

        sb.end(); // close the box

        stage.draw();
        stage.act();
    }

    @Override
    public void dispose() {
        background.dispose();
        stage.dispose();
    }
}
