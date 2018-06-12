package gen.snakemulti.gen.states;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import gen.snakemulti.GameConstants;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class LobbyState extends State {

    private Texture background;

    private String username;
    private String text;

    private BitmapFont font;

    private Socket clientSocket;


    public LobbyState(GameStateManager gsm, String username, Socket clientSocket) {
        super(gsm);
        background = new Texture("backgroundLobby3.png");
        this.username = username;
        this.clientSocket = clientSocket;

        text = "You're alone... :(";

        font = new BitmapFont();
        font.setColor(Color.WHITE);
        font.getData().setScale(1.5f, 1.5f);

        sendReady();

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
        font.draw(sb, text, 10, GameConstants.HEIGHT / 2 - 100);

        sb.end();
    }

    @Override
    public void dispose() {

    }

    private String waitForGo() {
        BufferedReader reader = null;
        String str = "";

        try {
            reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));



            while ((str = reader.readLine()) != null) {
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if(reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return str;
        }
    }

    private void sendReady() {
        PrintWriter writer = null;

        try {
            // send 'ready' to server
            writer = new PrintWriter(clientSocket.getOutputStream());
            writer.println("READY," + username);
            writer.flush();

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            writer.close();
        }
    }
}
