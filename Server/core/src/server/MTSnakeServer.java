package server;


import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import jdk.nashorn.internal.parser.JSONParser;
import server.objects.User;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.net.DatagramPacket;

/**
 * This class implements a multi-threaded TCP server. It is able to interact
 * with several clients at the time, as well as to continue listening for
 * connection requests.
 *
 * @author Lionel Burgbacher
 */
public class MTSnakeServer {
    final static int NUMBER_OF_PLAYERS = 4;
    final static Logger LOG = Logger.getLogger(MTSnakeServer.class.getName());

    int portUDP;
    int portTCP;

    static List<String> players;

    boolean gameStarted;

    /**
     * Constructor
     *
     * @param portTCP the port to listen on
     * @param portUDP the port to listen on
     */
    public MTSnakeServer(int portTCP, int portUDP) {
        this.portTCP = portTCP;
        this.portUDP = portUDP;

        players = new ArrayList<String>();
        gameStarted = false;
    }

    /**
     * This method initiates the process. The server creates a socket and binds it
     * to the previously specified port. It then waits for clients in a infinite
     * loop. When a client arrives, the server will read its input line by line
     * and send back the data converted to uppercase. This will continue until the
     * client sends the "BYE" command.
     */
    public void serveClients() {
        LOG.info("Starting the Receptionist Worker on a new thread...");
        new Thread(new ReceptionistWorker()).start();
        new Thread(new UDPWorker()).start();
    }

    private class UDPWorker implements Runnable {

        public void run() {

            UDPSocketServer s = new UDPSocketServer("127.0.0.1", portUDP);
            s.initGame(new String[]{"1", "2"});
            s.listen();
        }
    }

    /**
     * This inner class implements the behavior of the "receptionist", whose
     * responsibility is to listen for incoming connection requests. As soon as a
     * new client has arrived, the receptionist delegates the processing to a
     * "servant" who will execute on its own thread.
     */
    private class ReceptionistWorker implements Runnable {

        @SuppressWarnings("resource")
        public void run() {
            ServerSocket serverSocket;

            try {
                serverSocket = new ServerSocket(portTCP);
            } catch (IOException ex) {
                LOG.log(Level.SEVERE, null, ex);
                return;
            }

            while (true) {
                LOG.log(Level.INFO, "Waiting (blocking) for a new client on port {0}", portTCP);
                try {
                    Socket clientSocket = serverSocket.accept();
                    LOG.info("A new client has arrived. Starting a new thread and delegating work to a new servant...");
                    new Thread(new ServantWorker(clientSocket)).start();
                } catch (IOException ex) {
                    Logger.getLogger(MTSnakeServer.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

        /**
         * This inner class implements the behavior of the "servants", whose
         * responsibility is to take care of clients once they have connected. This
         * is where we implement the application protocol logic, i.e. where we read
         * data sent by the client and where we generate the responses.
         */
        private class ServantWorker implements Runnable {

            Socket clientSocket;
            BufferedReader in = null;
            PrintWriter out = null;

            public ServantWorker(Socket clientSocket) {
                try {
                    this.clientSocket = clientSocket;

                    in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                    out = new PrintWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
                } catch (IOException ex) {
                    Logger.getLogger(MTSnakeServer.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

            public void run() {
                String line;
                out.println("Welcome to the Multi-Snake Server.\n");
                out.flush();

                boolean end = false;

                while (!end) {
                    try {
                        LOG.info("Reading the client choice");

                        if ((line = in.readLine()) != null) {

                            if (line.equalsIgnoreCase(Protocol.CMD_HELLO)) {

                            } else if (line.equalsIgnoreCase(Protocol.CMD_HELP)) {

                                //TODO
                            } else if (line.equalsIgnoreCase(Protocol.CMD_START)) {
                                //Start a game, waiting for players
                                if (!gameStarted) {
                                    String playerName = in.readLine();
                                    players.add(playerName);
                                } else {
                                    out.println("game already in progress");
                                    out.flush();
                                }

                            } else if (line.equalsIgnoreCase(Protocol.CMD_END)) {
                                end = true;
                            } else if (line.equalsIgnoreCase(Protocol.CMD_UPDATE)) {

                                //TODO
                            } else if (line.equalsIgnoreCase(Protocol.CMD_LOGIN)) {

                                /*LOG.log(Level.INFO, "Login: requête reçue");
                                //Reponse de login
                                LOG.log(Level.INFO, "Login: demande de récupération de login");
                                out.println("Okay!");
                                out.flush();

                                //Réception des identifiants saisis et parsing
                                LOG.log(Level.INFO, "Login: attente de la tentative d'identification...");
                                String ids = in.readLine();
                                LOG.log(Level.INFO, "Object : " + ids);

                                //Conversion du string en object
                                JsonParser jsonParser = new JsonParser();
                                JsonObject objectFromString = jsonParser.parse(ids).getAsJsonObject();

                                LOG.log(Level.INFO, "Username = ", objectFromString.isJsonNull());
                                return;*/

                            } else if (line.equalsIgnoreCase(Protocol.CMD_LEADERBOARD)) {
                                //Shows leaderboard
                                //TODO
                            }
                            out.flush();
                        }

                        LOG.info("Cleaning up resources...");
                        clientSocket.close();
                        in.close();
                        out.close();

                    } catch (IOException ex) {
                        if (in != null) {
                            try {
                                in.close();
                            } catch (IOException ex1) {
                                LOG.log(Level.SEVERE, ex1.getMessage(), ex1);
                            }
                        }
                        if (out != null) {
                            out.close();
                        }
                        if (clientSocket != null) {
                            try {
                                clientSocket.close();
                            } catch (IOException ex1) {
                                LOG.log(Level.SEVERE, ex1.getMessage(), ex1);
                            }
                        }
                        LOG.log(Level.SEVERE, ex.getMessage(), ex);
                    }
                }
            }


        }
    }
}

