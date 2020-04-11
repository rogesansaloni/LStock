package network;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;

public class Server extends Thread {
    private String ip;
    private int port;
    private Server server;
    private ServerSocket sSocket;
    private boolean isOn;
    private LinkedList<DedicatedServer> clients;

    public Server(String ip, int port) throws IOException {
        this.ip = ip;
        this.port = port;
        this.isOn = false;
        this.sSocket = new ServerSocket(port);
        this.clients = new LinkedList<DedicatedServer>();
    }

    public void startServer() {
        // Start main server thread
        isOn = true;
        this.start();
    }

    public void stopServer() {
        // Stop main server thread
        isOn = false;
        //model.disconnectFromDatabase();
        this.interrupt();
    }

    public void run() {
        while(isOn) {
            try {
                // Wait for petitions to accept them
                // Block the execution
                System.out.println("Waiting for clients to connect...");
                Socket clientSocket = sSocket.accept();

                // Create dedicated server to attend to the client
                DedicatedServer client = new DedicatedServer(clientSocket);
                clients.add(client);

                System.out.println("Client has connected correctly!");

                // Start dedicated server for the client
                client.startServerConnection();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    public void stopListening() {
        // Paramos todos los servidores dedicados creados cuando ya no atendemos más peticiones
        for (DedicatedServer client : clients) {
            client.stopServerConnection();
        }
    }
}
