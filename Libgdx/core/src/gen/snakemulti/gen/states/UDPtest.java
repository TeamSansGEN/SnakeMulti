package gen.snakemulti.gen.states;

import gen.snakemulti.sprites.Snake;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

public class UDPtest implements Runnable{

    private int port;

    public UDPtest(int port){
        this.port = port;
    }

    public void clientUDP(){

        new Thread(new UDPtest(port)).start();
    }

    @Override
    public void run() {

        receivUPD(port);
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

                    System.out.println("Snake object received TO DRAW = " + snake);
                    snake = (Snake) is.readObject();

                    //TODO verify new position
                    //TODO verify death
                    //TODO



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
