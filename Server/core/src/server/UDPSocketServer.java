package server;

import com.badlogic.gdx.math.Vector2;

import gen.snakemulti.sprites.*;

import javax.xml.crypto.Data;
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

    private static int numberOfReadyPlayers = 0;

    private static final int[][] INITIAL_POSITIONS = {{50, 600}, {900, 600}, {900, 40}, {50, 40}};

    public UDPSocketServer(String ipAddress, int port) {

        this.port = port;
        players = new HashMap<String, Snake>();
        numberOfPlayers = 0;

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
        while(true) {
            byte[] bufferReceive = new byte[8192];
            byte[] bufferSend = new byte[8192];
            DatagramPacket packet = new DatagramPacket(bufferReceive, bufferReceive.length);
            DatagramPacket sendPacket = null;
            try {
                socket.receive(packet);

                bufferReceive = packet.getData();
                ByteArrayInputStream in = new ByteArrayInputStream(bufferReceive);
                ObjectInputStream is = new ObjectInputStream(in);

                Snake snake = (Snake) is.readObject();

                // update the snake Object of the player
                players.put(snake.getName(), snake);

                System.out.println(snake.getName() + ": ("+snake.getHeadPosition().x+", "+snake.getHeadPosition().y+")");

                ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
                ObjectOutputStream out = new ObjectOutputStream(byteOut);
                out.writeObject(players);
                bufferSend = byteOut.toByteArray();

                sendPacket = new DatagramPacket(bufferSend, bufferSend.length, packet.getAddress(), packet.getPort());

                //Thread.sleep(16);
                socket.send(sendPacket);

                //if(validateSnakePosition(snake.getHeadPosition(), previousPosition)) {
                if(true) { //TODO
                    // Send data with all positions (all snakes)
                    //snakesPositions = new byte[]{'t', 'e', 's', 't'};
                }
                else {
                    //snakesPositions = new byte[]{'0'};
                }


            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
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

    public void sendData(byte[] data, InetAddress ipAddress, int port) {
        DatagramPacket packet = new DatagramPacket(data, data.length, ipAddress, port);
        try {
            socket.send(packet);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void initGame(String[] playersList){
        numberOfPlayers = playersList.length;
        for(int i = 0; i < numberOfPlayers; i++) {

            int initX = INITIAL_POSITIONS[i][0];
            int initY = INITIAL_POSITIONS[i][1];
            String initDirection = Snake.DIRECTIONS[1];

            Snake s = new Snake(initX, initY, initDirection, playersList[i], "127.0.0.1");
            players.put(playersList[i], s);
        }

        // wait to receive 'ready' from all players
        // and send go when everyone is 'ready'.
        //receiveReadySendGo();
    }

    private void receiveReadySendGo() {
        byte[] buffer = new byte[1024];
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length);

        while(true) {
            System.out.println("still in loop");
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
