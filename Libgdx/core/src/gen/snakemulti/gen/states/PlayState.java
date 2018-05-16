package gen.snakemulti.gen.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import gen.snakemulti.SnakeMulti;
import gen.snakemulti.sprites.Snake;

import javax.xml.crypto.Data;
import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import java.net.DatagramPacket;
import java.util.Map;

public class PlayState extends State {

    private Texture background;

    private int numberOfPlayers;

    private DatagramSocket clientSocket;

    private Snake snake;

    private Map<String, Snake> players;

    //private List<Snake> snakes;

    public PlayState(GameStateManager gsm, int numberOfPlayers) {
        super(gsm);
        this.numberOfPlayers = numberOfPlayers;
        background = new Texture("backgroundLobby.png");
        //snakes = new ArrayList<Snake>();
        players = new HashMap<String, Snake>();
        players.put("Jee", new Snake( 50, 600, Snake.RIGHT, "Jee", "127.0.0.1"));
        players.put("Lio", new Snake(900, 600,  Snake.DOWN, "Lio", "127.0.0.1"));
        //position du snake doit être multiple de 4
        //snakes.add(new Snake(200, 400, Snake.RIGHT, "snake1.png", "joueur1", "10.192.91.230"));
        //snakes.add(new Snake(400, 200, Snake.LEFT, "snake2.png", "joueur2", "10.192.91.230"));
        snake = getClientSnake("Jee");

        try {
            clientSocket = new DatagramSocket();
        } catch (SocketException e) {
            e.printStackTrace();
        }
        ;
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

        if (Gdx.input.isKeyJustPressed(Input.Keys.W)) { // Move up
            if (snake.getDirection() != Snake.DOWN)
                snake.setDirection(Snake.UP);
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.A)) { // Move left
            if (snake.getDirection() != Snake.RIGHT)
                snake.setDirection(Snake.LEFT);
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.S)) { // Move down
            if (snake.getDirection() != Snake.UP)
                snake.setDirection(Snake.DOWN);
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.D)) { // Move right
            if (snake.getDirection() != Snake.LEFT)
                snake.setDirection(Snake.RIGHT);
        }


        /*if (Gdx.input.isKeyJustPressed(Input.Keys.UP)) { // Move up
            if (snakes.get(1).getDirection() != Snake.DOWN)
                snakes.get(1).setDirection(Snake.UP);
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.LEFT)) { // Move left
            if (snakes.get(1).getDirection() != Snake.RIGHT)
                snakes.get(1).setDirection(Snake.LEFT);
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN)) { // Move down
            if (snakes.get(1).getDirection() != Snake.UP)
                snakes.get(1).setDirection(Snake.DOWN);
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.RIGHT)) { // Move right
            if (snakes.get(1).getDirection() != Snake.LEFT)
                snakes.get(1).setDirection(Snake.RIGHT);
        }*/

    }

    @Override
    public void update(float dt) {
        handleInput();

        snake.update(dt);
        //snakes.get(1).update(dt);

        /*if (snakes.get(0).collides(snakes.get(1))) {
            snakes.get(0).kill();
        }

        if (snakes.get(1).collides(snakes.get(0))) {
            snakes.get(1).kill();
        }*/

        if (snake.isAlive()) {
            //sendUPD(snakes.get(0));
            sendPosition(snake);
        }

        //receivUPD(2829);
        players = receivePosition();
    }

    @Override
    public void render(SpriteBatch sb) {
        sb.begin();
        sb.draw(background, 0, 0, SnakeMulti.WIDTH, SnakeMulti.HEIGHT);
























        
        int playerNumber = 1;
        for(Snake s : players.values()) {
            for(int i = 0; i < snake.getSize(); i++) {
                String textureName = "snake" + playerNumber + ".png";
                sb.draw(new Texture(textureName), s.getBodyParts().get(i).x, s.getBodyParts().get(i).y);
            }
            playerNumber++;
        }

        sb.end();
    }

    @Override
    public void dispose() {
        background.dispose();
        clientSocket.close();
    }

    private void sendPosition(Snake snake) {
        byte[] sendData = new byte[8192];

        try {
            ByteArrayOutputStream bStream = new ByteArrayOutputStream();
            ObjectOutput oo = new ObjectOutputStream(bStream);
            oo.writeObject(snake);
            oo.close();

            sendData = bStream.toByteArray();

            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, InetAddress.getByName("127.0.0.1"), 2829);
            clientSocket.send(sendPacket);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Map<String, Snake> receivePosition() {
        byte[] receiveData = new byte[8192];
        Map<String, Snake> positions = null;
        try {
            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
            clientSocket.receive(receivePacket);
            //System.out.println(new String(receivePacket.getData()));

            ByteArrayInputStream bis = new ByteArrayInputStream(receiveData);
            ObjectInputStream in = new ObjectInputStream(bis);
            positions = (Map<String, Snake>) in.readObject();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return positions;
    }

    /*public ArrayList<Snake> getSnake() {

        return new ArrayList<Snake>(snakes);
    }*/

    public void sendUPD(Snake snake) {

        String serverIP = "127.0.0.1";
        int port = 2829;

        try {

            InetAddress address = InetAddress.getByName(serverIP);
            DatagramSocket socket = new DatagramSocket();

            ByteArrayOutputStream bStream = new ByteArrayOutputStream();
            ObjectOutput oo = new ObjectOutputStream(bStream);
            oo.writeObject(snake);
            oo.close();

            byte[] buffer = bStream.toByteArray();

            DatagramPacket request = new DatagramPacket(buffer, buffer.length, address, port);
            socket.send(request);
            System.out.println("CLIENT => SERVER");
            socket.close();
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    public void receivUPD(int port) {

        Snake snake = null;

        try {

            DatagramSocket serverUDP = new DatagramSocket();

            while (true) {

                //On s'occupe maintenant de l'objet paquet
                byte[] buffer = new byte[8192];
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);

                //Cette méthode permet de récupérer le datagramme envoyé par le client
                //Elle bloque le thread jusqu'à ce que celui-ci ait reçu quelque chose.
                serverUDP.receive(packet);

                byte[] data = packet.getData();
                ByteArrayInputStream in = new ByteArrayInputStream(data);
                ObjectInputStream is = new ObjectInputStream(in);

                System.out.println(data.length);

            }
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (IOException i) {
            i.printStackTrace();
        }
    }


}
