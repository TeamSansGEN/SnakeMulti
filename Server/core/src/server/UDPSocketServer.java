package server;

import com.badlogic.gdx.math.Vector2;

import com.google.gson.Gson;
import gen.snakemulti.sprites.*;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UDPSocketServer {

    private int port;
    private DatagramSocket socket;
    private InetAddress ipAddress;
    private static Map<String, Snake> players; // <username, snake>
    private int numberOfPlayers;
    private Game game;
    private Gson gson;
    private List<Apple> apples;
    private List<Bonus> bonuses;
    private List<Penalty> penalties;
    private List<Vector2> poops;
    private List<Vector2> walls;
    private String [] playersList;
    private static int numberOfReadyPlayers = 0;

    private static final int[][] INITIAL_POSITIONS = {{48, 600}, {800, 600}, {800, 40}, {48, 40}};

    public UDPSocketServer(String ipAddress, int port) {

        this.port = port;
        players = new HashMap<>();
        numberOfPlayers = 0;
        gson = new Gson();

        try {
            this.ipAddress = InetAddress.getByName(ipAddress);
            socket = new DatagramSocket(port);

        } catch (SocketException e) {
            e.printStackTrace();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    public void listen() {
        Snake snake;

        while(true) {
            byte[] bufferReceive = new byte[GameConstants.BUFF_SIZE];
            byte[] bufferSend = new byte[GameConstants.BUFF_SIZE];
            DatagramPacket packet = new DatagramPacket(bufferReceive, bufferReceive.length);
            DatagramPacket sendPacket = null;
            try {
                socket.receive(packet);

                // Get snake from client
                String snakeJson = new String(packet.getData(), 0, packet.getLength());
                snake = gson.fromJson(snakeJson, Snake.class);

                // Update the snake Object of the player
                players.put(snake.getName(), snake);

                // Update the game
                game = game.update(walls);

                game.setPlayers(players);

                // New round
                if(game.getNumberOfDead() >= players.size() - 1) {
                    initGame(playersList);
                }

                // generate the packet from json string
                bufferSend = gson.toJson(game).getBytes();
                sendPacket = new DatagramPacket(bufferSend, bufferSend.length, packet.getAddress(), packet.getPort());

                // send the current game value to all clients
                socket.send(sendPacket);

                //TODO validate position

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private boolean validateSnakePosition(Vector2 currentPosition, Vector2 previousPosition) {
        if(previousPosition == null) { //TODO a modifier, test avec s.getPosition()
            return true;
        }

        if(currentPosition.x <= previousPosition.x + Snake.BODY_SIZE && currentPosition.x >= previousPosition.x - Snake.BODY_SIZE) {
            return true;
        }
        if(currentPosition.y <= previousPosition.y + Snake.BODY_SIZE && currentPosition.y >= previousPosition.y - Snake.BODY_SIZE) {
            return true;
        }
        return false;
    }

    public void initGame(String[] playersList){
        this.playersList = new String[playersList.length];
        for(int i = 0; i < playersList.length; i++) {
            this.playersList[i] = playersList[i];
        }

        numberOfPlayers = playersList.length;
        for(int i = 0; i < numberOfPlayers; i++) {

            int initX = INITIAL_POSITIONS[i][0];
            int initY = INITIAL_POSITIONS[i][1];
            String initDirection = Snake.DIRECTIONS[1];
            Snake s = new Snake(initX, initY, initDirection, playersList[i], "127.0.0.1");
            players.put(playersList[i], s);
        }

        // initialize walls schema
        WallsSchemasGenerator wallsSchemasGenerator = new WallsSchemasGenerator(1);
        walls = wallsSchemasGenerator.getWallsSchema();

        apples    = new ArrayList<>();
        bonuses   = new ArrayList<>();
        penalties = new ArrayList<>();
        poops     = new ArrayList<>();

        // Generate apples
        for(int i = 0; i < GameConstants.MAX_APPLES; i++) {
            Apple apple = new Apple(GameConstants.APPLE_TEXTURE_NAME, GameConstants.NORMAL_CONSUMABLE_SIZE);

            // Verify that the apple is not generated on an existing object
            while (!Game.consumableValidPosition(apple, walls, new ArrayList<Snake>(players.values()))) {
                apple.setNewPosition();
            }
            apples.add(apple);
        }

        for(int i = 0; i < GameConstants.MAX_BONUSES; i++) {
            // Generate bonuses
            Bonus speedBonus = new Bonus(GameConstants.SPEED_BONUS_TEXTURE_NAME, GameConstants.NORMAL_CONSUMABLE_SIZE);
            //Bonus freezeSnakesBonus = new Bonus(GameConstants.FREEZE_SNAKES_BONUS_TEXTURE_NAME);

            // Verify that the bonus is not generated on an existing object
            while (!Game.consumableValidPosition(speedBonus, walls, new ArrayList<Snake>(players.values()))) {
                speedBonus.setNewPosition();
            }
            /*while (!Game.consumableValidPosition(freezeSnakesBonus, walls, new ArrayList<Snake>(players.values()))) {
                freezeSnakesBonus.setNewPosition();
            }*/

            bonuses.add(speedBonus);
            //bonuses.add(freezeSnakesBonus);
        }

        // Generate penalties
        for(int i = 0; i < GameConstants.MAX_PENALTIES; i++) {
            Penalty freezePenalty = new Penalty(GameConstants.FREEZE_PENALTY_TEXTURE_NAME, GameConstants.FREEZE_PENALTY_SIZE);

            while(!Game.consumableValidPosition(freezePenalty, walls, new ArrayList<Snake>(players.values()))) {
                freezePenalty.setNewPosition();
            }
            penalties.add(freezePenalty);
        }

        // Keep scores of previous round(s)
        if(game != null) {
            game = new Game(players, apples, bonuses, penalties, poops, game.getPlayerScore());
        }
        else {
            // Initialize the game for the FIRST TIME with all players and empty consumables lists
            game = new Game(players, apples, bonuses, penalties, poops);
        }

        // wait to receive 'ready' from all players
        // and send go when everyone is 'ready'.
        //receiveReadySendGo();
    }

    private void receiveReadySendGo() {
        byte[] buffer = new byte[1024];
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length);

        while(true) {
            try {
                System.out.println(numberOfReadyPlayers + " players ready");
                socket.receive(packet);
                buffer = packet.getData();
                String data = new String(packet.getData());
                data = data.replaceAll("[^A-Za-z0-9]", ""); //remove all non aplhanumeric character
                if(data.equals("ready")) {
                    numberOfReadyPlayers++;
                    System.out.println(numberOfReadyPlayers + " players ready");
                    break;
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        byte[] goMsg = "go".getBytes();
        DatagramPacket goPacket = new DatagramPacket(goMsg, goMsg.length, packet.getAddress(), packet.getPort());

        while(true) {
            try {
                if(numberOfReadyPlayers == numberOfPlayers) {
                    socket.send(goPacket);
                    break;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
