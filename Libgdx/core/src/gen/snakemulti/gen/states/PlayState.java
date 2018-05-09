package gen.snakemulti.gen.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import gen.snakemulti.SnakeMulti;
import gen.snakemulti.sprites.Snake;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

import java.net.DatagramPacket;

public class PlayState extends State {

    private Texture background;

    private int numberOfPlayers;

    private List<Snake> snakes;

    public PlayState(GameStateManager gsm, int numberOfPlayers) {
        super(gsm);
        this.numberOfPlayers = numberOfPlayers;
        background = new Texture("backgroundLobby.png");
        snakes = new ArrayList<Snake>();

        //for(int i = 0; i < numberOfPlayers; i++) {
        //position du snake doit être multiple de 4
        snakes.add(new Snake(200, 400, Snake.RIGHT, "snake1.png", "joueur1", "127.0.0.1"));
        snakes.add(new Snake(400, 200, Snake.LEFT, "snake2.png", "joueur2", "127.0.0.1"));
        //}

    }

    @Override
    protected void handleInput() {

        if (Gdx.input.isKeyJustPressed(Input.Keys.W)) { // Move up
            if (snakes.get(0).getDirection() != Snake.DOWN)
                snakes.get(0).setDirection(Snake.UP);
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.A)) { // Move left
            if (snakes.get(0).getDirection() != Snake.RIGHT)
                snakes.get(0).setDirection(Snake.LEFT);
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.S)) { // Move down
            if (snakes.get(0).getDirection() != Snake.UP)
                snakes.get(0).setDirection(Snake.DOWN);
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.D)) { // Move right
            if (snakes.get(0).getDirection() != Snake.LEFT)
                snakes.get(0).setDirection(Snake.RIGHT);
        }


        if (Gdx.input.isKeyJustPressed(Input.Keys.UP)) { // Move up
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
        }

    }

    @Override
    public void update(float dt) {
        handleInput();

        snakes.get(0).update(dt);
        snakes.get(1).update(dt);

        if (snakes.get(0).collides(snakes.get(1))) {
            snakes.get(0).kill();
        }

        if (snakes.get(1).collides(snakes.get(0))) {
            snakes.get(1).kill();
        }
    }

    @Override
    public void render(SpriteBatch sb) {
        sb.begin();
        sb.draw(background, 0, 0, SnakeMulti.WIDTH, SnakeMulti.HEIGHT);

        if (snakes.get(0).isAlive()) {
            sendUPD(snakes.get(0));
        }

        if (snakes.get(1).isAlive()) {
            sendUPD(snakes.get(1));
        }


        UDPtest u = new UDPtest(3333);
        //u.clientUDP();
        //Snake s1 = u.receivUPD(3333);

        for (int i = 0; i < snakes.get(0).getSize(); i++) {
            sb.draw(snakes.get(0).getTexture().get(i), snakes.get(0).getBodyParts().get(i).x, snakes.get(0).getBodyParts().get(i).y);
        }
        for (int i = 0; i < snakes.get(1).getSize(); i++) {
            sb.draw(snakes.get(1).getTexture().get(i), snakes.get(1).getBodyParts().get(i).x, snakes.get(1).getBodyParts().get(i).y);
        }

        sb.end();

    }

    @Override
    public void dispose() {
        background.dispose();
    }

    public ArrayList<Snake> getSnake() {

        return new ArrayList<Snake>(snakes);
    }

    public void sendUPD(Snake snake) {

        String hostname = snake.getIpAdress();
        int port = 2829;

        try {

            InetAddress address = InetAddress.getByName(hostname);
            DatagramSocket socket = new DatagramSocket();

            ByteArrayOutputStream bStream = new ByteArrayOutputStream();
            ObjectOutput oo = new ObjectOutputStream(bStream);
            oo.writeObject(snake);
            oo.close();

            byte[] buffer = bStream.toByteArray();

            DatagramPacket request = new DatagramPacket(buffer, buffer.length, address, port);
            socket.send(request);
            socket.close();
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    public Snake receivUPD(int port) {

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

                try {

                    snake = (Snake) is.readObject();
                    System.out.println("Snake object received TO DRAW = " + snake);
                    return snake;

                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }

            }
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (IOException i) {
            i.printStackTrace();
        }

        return snake;
    }


}
