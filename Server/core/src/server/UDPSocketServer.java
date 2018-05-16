package server;

import com.badlogic.gdx.math.Vector2;

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

    private static final int[][] INITIAL_POSITIONS = {{50, 600}, {900, 600}, {900, 40}, {50, 40}};

    public UDPSocketServer(String ipAddress, int port) {

        this.port = port;
        players = new HashMap<String, Snake>();

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

                //System.out.println("Data received => position("+snake.getHeadPosition().x
                //                    +","+snake.getHeadPosition().y+")");


                players.put(snake.getName(), snake);

                ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
                ObjectOutputStream out = new ObjectOutputStream(byteOut);
                out.writeObject(players);
                bufferSend = byteOut.toByteArray();

                sendPacket = new DatagramPacket(bufferSend, bufferSend.length, packet.getAddress(), packet.getPort());
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

        for(int i = 0; i < playersList.length; i++) {

            int initX = INITIAL_POSITIONS[i][0];
            int initY = INITIAL_POSITIONS[i][1];
            String initDirection = Snake.DIRECTIONS[1];
            String textureName = "snake"+ (i+1) + ".png";

            Snake s = new Snake(initX, initY, initDirection, playersList[i], "127.0.0.1");
            players.put(playersList[i], s);
        }
    }

}
