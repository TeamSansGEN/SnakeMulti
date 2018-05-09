package server;

import com.badlogic.gdx.math.Vector2;

import gen.snakemulti.sprites.*;

import java.io.*;
import java.net.*;
import java.util.ArrayList;

public class UDPSocketServer {

    private int port;

    public UDPSocketServer(int port) {

        this.port = port;
    }

    public void createAndListenSocket() {

        //initGame(2);

        try {


            DatagramSocket serverUDP = new DatagramSocket(port);

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

                Snake snake = null;

                try {

                    snake = (Snake) is.readObject();
                    System.out.println("Snake object received = " + snake);

                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }

                String hostname = snake.getIpAdress();

                try {

                    InetAddress address = InetAddress.getByName(hostname);
                    DatagramSocket socket = new DatagramSocket();

                    ByteArrayOutputStream bStream = new ByteArrayOutputStream();
                    ObjectOutput oo = new ObjectOutputStream(bStream);
                    oo.writeObject(snake);
                    oo.close();

                    byte[] buffer2 = bStream.toByteArray();

                    DatagramPacket request = new DatagramPacket(buffer2, buffer2.length, address, 3333);
                    System.out.println(address);
                    socket.send(request);
                }
                catch (SocketException e) {
                    e.printStackTrace();
                }
                catch (UnknownHostException e) {
                    e.printStackTrace();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }

            }
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (IOException i) {
            i.printStackTrace();
        }
    }

    /*
    public void initGame(int numberOfPlayers){

        //TODO forloop

        Snake s1 =  new Snake(200, 400, Snake.RIGHT, "snake1.png", "JEE", "10.192.91.230");
        Snake s2 =  new Snake(200, 400, Snake.RIGHT, "snake2.png", "LIO", "10.192.95.118");

        ArrayList<Snake> snakes = new ArrayList<Snake>();
        snakes.add(s1);
        snakes.add(s2);

        String hostname = s1.getIpAdress();

        try {

            InetAddress address = InetAddress.getByName(hostname);
            DatagramSocket socket = new DatagramSocket();

            ByteArrayOutputStream bStream = new ByteArrayOutputStream();
            ObjectOutput oo = new ObjectOutputStream(bStream);
            oo.writeObject(snake);
            oo.close();

            byte[] buffer2 = bStream.toByteArray();

            DatagramPacket request = new DatagramPacket(buffer2, buffer2.length, address, 3333);
            System.out.println(address);
            socket.send(request);
        }
        catch (SocketException e) {
            e.printStackTrace();
        }
        catch (UnknownHostException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    */

}
