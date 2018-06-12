package gen.snakemulti.gen.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.google.gson.Gson;
import gen.snakemulti.Game;
import gen.snakemulti.GameConstants;
import gen.snakemulti.SnakeMulti;
import gen.snakemulti.WallsSchemasGenerator;
import gen.snakemulti.sprites.Apple;
import gen.snakemulti.sprites.Bonus;
import gen.snakemulti.sprites.Penalty;
import gen.snakemulti.sprites.Snake;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.HashMap;

import java.net.DatagramPacket;
import java.util.List;
import java.util.Map;


public class PlayState extends State {

    private static String IP_SERVER   = "192.168.0.46";
    private static int    PORT_SERVER = 2830;

    private String clientName;
    private Texture background;

    private DatagramSocket clientSocket;

    private int numberOfPlayers;

    private Snake snake;

    private Game game;

    private Gson gson;

    private Map<String, Snake> players;
    private List<Apple> apples;
    private List<Bonus> bonuses;
    private List<Penalty> penalties;
    private List<Vector2> poops;
    private List<Texture> playersTexture;
    private Map<String, Integer> playerSpeed;
    private List<Vector2> walls;

    //TESTS ENLEVER DES TRUC DE RENDER
    private String textureName = "snake" + 1 + ".png";
    private Texture texture = new Texture(textureName);
    private Texture textureApple = new Texture(GameConstants.APPLE_TEXTURE_NAME);
    private Texture bricksTexture = new Texture(GameConstants.BRICKS_TEXTURE_NAME);
    private Texture bonus1Texture = new Texture(GameConstants.SPEED_BONUS_TEXTURE_NAME);
    private Texture poopTexture = new Texture(GameConstants.POOP_TEXTURE_NAME);
    private Texture freezePenaltyTexture = new Texture(GameConstants.FREEZE_PENALTY_TEXTURE_NAME);
    ///////////

    BitmapFont scoresFont;
    Label.LabelStyle textStyle;
    Label text;
    private Stage stage;

    public PlayState(GameStateManager gsm, int numberOfPlayers) {
        super(gsm);
        this.numberOfPlayers = numberOfPlayers;
        background = new Texture("background.png");
        players = new HashMap<String, Snake>();

        //apple = new Apple();

        clientName = "Jee";
        players.put("Jee", new Snake(48, 600, Snake.RIGHT, "Jee", "127.0.0.1"));
        players.put("Lio", new Snake(800, 600, Snake.LEFT, "Lio", "127.0.0.1"));
        players.put("Blanco", new Snake(800, 40, Snake.LEFT, "Blanco", "127.0.0.1"));

        snake = getClientSnake(clientName);

        try {
            clientSocket = new DatagramSocket(PORT_SERVER);
        } catch (SocketException e) {
            e.printStackTrace();
        }

        gson = new Gson();

        stage = new Stage();
        scoresFont = new BitmapFont();
        textStyle = new Label.LabelStyle();
        textStyle.font = scoresFont;
        text = new Label("Jee 0", textStyle);
        stage.addActor(text);

        // Initialize the game and variables
        initGame();
    }

    private Snake getClientSnake(String username) {
        for(String key : players.keySet()) {
            if(key.equals(username)) {
                return players.get(key);
            }
        }
        return null;
    }

    @Override
    protected void handleInput() {

        if (Gdx.input.isKeyJustPressed(Input.Keys.W) && !snake.getDirection().equals(Snake.DOWN)) { // Move up
                snake.setDirection(Snake.UP);
        }

        else if (Gdx.input.isKeyJustPressed(Input.Keys.A) && !snake.getDirection().equals(Snake.RIGHT)) { // Move left
                snake.setDirection(Snake.LEFT);
        }

        else if (Gdx.input.isKeyJustPressed(Input.Keys.S) && !snake.getDirection().equals(Snake.UP)) { // Move down
                snake.setDirection(Snake.DOWN);
        }

        else if (Gdx.input.isKeyJustPressed(Input.Keys.D) && !snake.getDirection().equals(Snake.LEFT)) { // Move right
                snake.setDirection(Snake.RIGHT);
        }
    }

    @Override
    public void update(float dt) {
        handleInput();

        if(snake.isAlive()) {
            snake.moveSnake();
        }

        // Check for any self-collision
        if(snake.collides()) {
            snake.kill();
        }

        // Send the new position to the server
        sendPosition(snake);

        // Get the new game value from the server
        game = receivePosition();

        // Get players position and status from the server
        players = game.getPlayers();

        // Get new consumables lists from the server
        apples      = game.getApples();
        bonuses     = game.getBonuses();
        penalties   = game.getPenalties();
        playerSpeed = game.getPlayerSpeed();
        poops       = game.getPoops();

        // Update the client's snake
        snake = players.get(clientName);
        snake.setSpeed(game.getPlayerSpeed().get(clientName));
    }

    @Override
    public void render(SpriteBatch sb) {
        sb.begin();
        sb.draw(background, 0, 0, SnakeMulti.WIDTH, SnakeMulti.HEIGHT);

        int playerNumber = 0;
        for(Snake s : players.values()) {
            for(int i = 0; i < s.getSize(); i++) {
                sb.draw(GameConstants.SNAKE_TEXTURE[playerNumber], s.getBodyParts().get(i).x, s.getBodyParts().get(i).y);
            }
            playerNumber++;
        }

        // draw apples
        for(Apple apple : apples) {
            sb.draw(textureApple, apple.getX(), apple.getY());
        }

        // draw bonuses
        for(Bonus bonus : bonuses) {
            sb.draw(bonus1Texture, bonus.getX(), bonus.getY());
        }

        // draw penalties
        for(Penalty penalty : penalties) {
            sb.draw(freezePenaltyTexture, penalty.getX(), penalty.getY());
        }

        // draw walls
        for(Vector2 brick : walls) {
            sb.draw(bricksTexture, brick.x, brick.y);
        }

        // draw poops
        for(Vector2 poop : poops) {
            sb.draw(poopTexture, poop.x, poop.y);
        }

        sb.end();
    }

    @Override
    public void dispose() {
        background.dispose();
        clientSocket.close();
    }

    private void sendPosition(Snake snake) {
        byte[] sendData;

        try {
            sendData = gson.toJson(snake).getBytes();

            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, InetAddress.getByName(IP_SERVER), 2829);
            clientSocket.send(sendPacket);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Game receivePosition() {
        byte[] receiveData = new byte[GameConstants.BUFF_SIZE];
        Game game = null;
        try {
            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);

            clientSocket.receive(receivePacket);

            String gameJson = new String(receivePacket.getData(), 0, receivePacket.getLength());
            game = gson.fromJson(gameJson, Game.class);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return game;
    }

    private void initGame() {



        // assign a texture to each player
        playersTexture = new ArrayList<>();
        for(int i = 0; i < players.size(); i++) {
            String textureName = "snake" + (i+1) + ".png";
            playersTexture.add(new Texture(textureName));
        }

        // initialize all consumables lists
        apples    = new ArrayList<>();
        bonuses   = new ArrayList<>();
        penalties = new ArrayList<>();
        poops     = new ArrayList<>();

        // initialize walls schema
        WallsSchemasGenerator wallsSchemasGenerator = new WallsSchemasGenerator(1);
        walls = wallsSchemasGenerator.getWallsSchema();

        // initialize the game object
        game = new Game(players, apples, bonuses, penalties, poops);

        /*byte[] msg = "ready".getBytes();

        try {
            // Send 'ready' to the server
            DatagramPacket packet = new DatagramPacket(msg, msg.length, InetAddress.getByName(IP_SERVER), PORT_SERVER);
            clientSocket.send(packet);

            // Wait for 'go' from server
            while(true) {
                byte[] buffer = new byte[1024];
                DatagramPacket receivedPacket = new DatagramPacket(buffer, buffer.length);
                clientSocket.receive(receivedPacket);
                String data = new String(receivedPacket.getData());
                data = data.replaceAll("[^A-Za-z0-9]", ""); //remove all non aplhanumeric character
                if(data.equals("go")) {
                    break;
                }
            }
            clientSocket.close();

        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }*/
    }
}
