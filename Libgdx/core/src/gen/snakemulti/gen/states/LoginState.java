package gen.snakemulti.gen.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import gen.snakemulti.GameConstants;

import java.io.IOException;
import java.net.Socket;


public class LoginState extends State implements Input.TextInputListener {

    private Texture background;
    private ImageButton loginButton;

    private Socket clientSocket;

    private Stage stage;

    public LoginState(final GameStateManager gsm) {
        super(gsm);
        background = new Texture("backgroundLobby3.png");

        Texture loginButtonTexture = new Texture("loginButton.png");
        Drawable loginButtonDrawable = new TextureRegionDrawable(new TextureRegion(loginButtonTexture));
        loginButton = new ImageButton(loginButtonDrawable);
        loginButton.setSize(130, 50);
        loginButton.setPosition(GameConstants.WIDTH/2 - 130/2, GameConstants.HEIGHT/2 - 130/2 - 100);

        stage = new Stage();

        TextField.TextFieldStyle style = new TextField.TextFieldStyle();
        style.fontColor = Color.RED;
        style.font = new BitmapFont();

        Skin skin = new Skin(Gdx.files.local("skin/uiskin.json"));

        final TextField usernameField = new TextField("", skin);
        usernameField.setText("Enter your username");
        usernameField.setSize(180, 30);
        usernameField.setPosition(GameConstants.WIDTH/2 - 180/2, GameConstants.HEIGHT/2 + 50);

        usernameField.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                usernameField.setText("");
            }
        });

        final TextField passwordField = new TextField("", skin);
        passwordField.setPasswordMode(true);
        passwordField.setPasswordCharacter('*');
        passwordField.setText("********");
        passwordField.setSize(180, 30);
        passwordField.setPosition(GameConstants.WIDTH/2 - 180/2, GameConstants.HEIGHT/2 - 50/2);

        passwordField.addListener(new ClickListener() {
           @Override
           public void clicked(InputEvent event, float x, float y) {
               super.clicked(event, x, y);
               passwordField.setText("");
           }
        });

        loginButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // TODO: transfere pseudo/mdp au serveur, serveur check database, serveur répond au client: OUI/NON
                String username = usernameField.getText();
                String password = passwordField.getText();
                try {
                    clientSocket = new Socket("127.0.0.1", 2828);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if(true) {

                    gsm.set(new MenuState(gsm, username, clientSocket));
                    dispose();
                }
            }
        });

        stage.addActor(usernameField);
        stage.addActor(passwordField);
        stage.addActor(loginButton);

        Gdx.input.setInputProcessor(stage);
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

        stage.draw();
        stage.act();

        //loginButton.draw(sb, 1);
    }

    @Override
    public void dispose() {
        background.dispose();
        loginButton.clearListeners();
    }

    // Interface Input.TextInputListener
    @Override
    public void input(String text) {
    }

    // Interface Input.TextInputListener
    @Override
    public void canceled() {

    }
}
