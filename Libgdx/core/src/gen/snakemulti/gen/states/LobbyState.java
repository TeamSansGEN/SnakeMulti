package gen.snakemulti.gen.states;

import com.badlogic.gdx.Net;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.net.Socket;
import com.badlogic.gdx.scenes.scene2d.Stage;
import gen.snakemulti.GameConstants;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

public class LobbyState extends State {

    private Texture background;

    private String username;
    private String text;
    private String serverResponse;
    private BitmapFont font;
    private String playerNum;
    private String serverIP;
    private Socket clientSocket;


    public LobbyState(final GameStateManager gsm, final String username, String playerNum, Socket clientSocket, String serverIP) {
        super(gsm);
        background = new Texture("backgroundLobby3.png");
        this.username = username;
        this.clientSocket = clientSocket;
        this.playerNum = playerNum;
        this.serverIP = serverIP;
        serverResponse = "";
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
        serverResponse = waitForGo();

        if (!serverResponse.equalsIgnoreCase("") && !serverResponse.equalsIgnoreCase("READY2GO")) {
            text = "Game starting in " + serverResponse + " second(s) !";
        }
        if (serverResponse.equalsIgnoreCase("GO")) {
            gsm.set(new PlayState(gsm, playerNum, 2, serverIP));
            dispose();
        }
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

            str = reader.readLine();

        } catch (IOException e) {
            e.printStackTrace();
        } /*finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }*/
        return str;
    }

    private void sendReady() {
        PrintWriter writer = null;

        // send 'ready' to server
        writer = new PrintWriter(clientSocket.getOutputStream());
        writer.println("READY," + username);
        writer.flush();

         /*catch (IOException e) {
            e.printStackTrace();
        }*/
    }
}
