package gen.snakemulti.gen.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Vector2;
import gen.snakemulti.SnakeMulti;
import gen.snakemulti.sprites.Apple;
import gen.snakemulti.sprites.Snake;

import javax.xml.crypto.Data;
import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import java.net.DatagramPacket;
import java.util.Map;

import java.util.Timer;
import java.util.TimerTask;


public class PlayState extends State {


    //private static String IP_SERVER   = "10.192.95.72";
    private static String IP_SERVER   = "192.168.43.30";
    private static int    PORT_SERVER = 2830;

    private String clientName;
    private Texture background;

    private int numberOfPlayers;

    private DatagramSocket clientSocket;

    private Snake snake;

    private boolean isEat = true;
    private Apple apple1;

    private Map<String, Snake> players;


    //TESTS ENLEVER DES TRUC DE RENDER
    String textureName = "snake" + 1 + ".png";
    Texture texture = new Texture(textureName);
    String textureNameApple = "apple16.png";
    Texture textureApple = new Texture(textureNameApple);
    ///////////

    //private List<Snake> snakes;

    public PlayState(GameStateManager gsm, int numberOfPlayers) {
        super(gsm);
        this.numberOfPlayers = numberOfPlayers;
        background = new Texture("backgroundLobby.png");
        //snakes = new ArrayList<Snake>();
        players = new HashMap<String, Snake>();

        apple1 = new Apple();

        clientName = "Lio";
        players.put("Jee", new Snake(48, 600, Snake.RIGHT, "Jee", "127.0.0.1"));
        players.put("Lio", new Snake(800, 600, Snake.LEFT, "Lio", "127.0.0.1"));

        //TESTS ENLEVER DES TRUC DE RENDER

        ////////

        //players.put("Lio", new Snake(900, 600,  Snake.DOWN, "Lio", "127.0.0.1"));
        //position du snake doit être multiple de 4
        //snakes.add(new Snake(200, 400, Snake.RIGHT, "snake1.png", "joueur1", "10.192.91.230"));
        //snakes.add(new Snake(400, 200, Snake.LEFT, "snake2.png", "joueur2", "10.192.91.230"));

        snake = getClientSnake(clientName);

        try {
            clientSocket = new DatagramSocket(PORT_SERVER);
        } catch (SocketException e) {
            e.printStackTrace();
        }
        //initGame();
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

        //snake.update(dt);
        if(snake.isAlive()) {
            snake.moveSnake();
        }
        System.out.println("Snake STATE = " + snake.isAlive());
        //snakes.get(1).update(dt);
        if(snake.collides()) {
            snake.kill();
        }
        /*if (snakes.get(0).collides(snakes.get(1))) {
            snakes.get(0).kill();
        }

        if (snakes.get(1).collides(snakes.get(0))) {
            snakes.get(1).kill();
        }*/

        /*Thread sendAndReceivePosition = new Thread() {
            public void run() {

                while(true) {
                    if (snake.isAlive()) {
                        //sendUPD(snakes.get(0));
                        sendPosition(snake);
                    }

                    //receivUPD(2829);
                    players = receivePosition();
                }
            }
        };
        sendAndReceivePosition.start();*/


            //sendUPD(snakes.get(0));
            //System.out.println("send");
        sendPosition(snake);


        //receivUPD(2829);
        players = receivePosition();
        snake = players.get(clientName);

    }

    ////////////////////////
    ////////////////////////
    ////////////////////////


    private int app1X;
    private int app1Y;


    public void changeBool(){

        isEat = true;
    }

    public void eatApple(int x, int y){

        if(snake.getHeadPosition().x - x <= textureApple.getWidth() && snake.getHeadPosition().y - y <= textureApple.getHeight() &&
                snake.getHeadPosition().x - x >= 0  && snake.getHeadPosition().y - y >= 0){
            isEat = true;
        }
    }

    public void randomTimeApple(){


            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                public void run() {
                    app1X = apple1.randomX();
                    app1Y = apple1.randomY();
                }
            }, 0, 10000);

            //timer.
    }


    @Override
    public void render(SpriteBatch sb) {
        sb.begin();
        sb.draw(background, 0, 0, SnakeMulti.WIDTH, SnakeMulti.HEIGHT);

        eatApple(app1X, app1Y);


        if(isEat) {
            snake.addTail();
            isEat= false;
            randomTimeApple();
        }


        sb.draw(textureApple, app1X, app1Y);


        /*for (int i = 0; i < snake.getSize(); i++) {
            sb.draw(snake.getTexture().get(i), snake.getBodyParts().get(i).x, snake.getBodyParts().get(i).y);
        }
        for (int i = 0; i < snakes.get(1).getSize(); i++) {
            sb.draw(snakes.get(1).getTexture().get(i), snakes.get(1).getBodyParts().get(i).x, snakes.get(1).getBodyParts().get(i).y);
        }*/
        //int playerNumber = 1;
        for(Snake s : players.values()) {
            for(int i = 0; i < s.getSize(); i++) {

                sb.draw(texture, s.getBodyParts().get(i).x, s.getBodyParts().get(i).y);
            }
            //playerNumber++;
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

            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, InetAddress.getByName(IP_SERVER), 2829);
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

            /*
            try {
                clientSocket.receive(receivePacket);
            }
            catch(IOException e) {
                continue;
            }
            */

            clientSocket.receive(receivePacket);
            /*
            while (true) {
                try {
                    System.out.println("receivData");
                    System.out.println(receivePacket.getData());
                    for (byte b: receivePacket.getData()) {
                        System.out.print(b);
                    }
                    System.out.println();

                    clientSocket.receive(receivePacket);
                    break;
                }
                catch(IOException e) {
                    System.out.println("continueeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee");
                    continue;
                }
            }
            */


            //clientSocket.receive(receivePacket);
            //System.out.println(new String(receivePacket.getData()));

            //System.out.println("bis");
            ByteArrayInputStream bis = new ByteArrayInputStream(receiveData);
            //System.out.println("in");
            ObjectInputStream in = new ObjectInputStream(bis);
            //System.out.println("position");
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


        try {

            DatagramSocket socket = new DatagramSocket();

            ByteArrayOutputStream bStream = new ByteArrayOutputStream();
            ObjectOutput oo = new ObjectOutputStream(bStream);
            oo.writeObject(snake);
            oo.close();

            byte[] buffer = bStream.toByteArray();

            DatagramPacket request = new DatagramPacket(buffer, buffer.length, InetAddress.getByName(IP_SERVER), PORT_SERVER);
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

                int SIZE_BUFFER = 16384;

                //On s'occupe maintenant de l'objet paquet
                byte[] buffer = new byte[SIZE_BUFFER];
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

    private void initGame() {
        byte[] msg = "ready".getBytes();

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
        }
    }

}
