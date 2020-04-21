package network;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import model.entities.TunnelObject;
import utils.JSONReader;

public class NetworkManager extends Thread {
  private Socket serverSocket;
  private ObjectInputStream ois;
  private ObjectOutputStream oos;
  private boolean running;
  private static NetworkManager instance = null;
  private NetworkConfiguration configuration;
  // TODO: Specify the controllers as attributes
  // TODO: Specify the views to be shown

  /**
   * Represents a Singleton
   * @return single and shared global instance
   */
  public synchronized static NetworkManager getInstance () {
    try {
      if (instance == null) {
        instance = new NetworkManager();
      }
      return instance;
    } catch (IOException e) {
      e.printStackTrace();
    }
    return null;
  }

  private NetworkManager() throws IOException {
    // Get Network configuration from JSON
    JSONReader jsonReader = new JSONReader();
    configuration = jsonReader.getClientConfiguration();

    // Set up the connection to the server
    this.running = false;
    this.serverSocket = new Socket(configuration.getIp(), configuration.getPort()); // pass ip and port from NetworkConfiguration
    oos = new ObjectOutputStream(this.serverSocket.getOutputStream());
    oos.flush();
    ois = new ObjectInputStream(this.serverSocket.getInputStream());

    this.start();
  }

  public void startServerConnection () {
    running = true;
    start();
  }

  public void stopServerConnection () {
    running = false;
    interrupt();
  }

  public void sendTunnelObject(TunnelObject object) throws IOException {
    oos.writeObject(object);
  }

  public void sendAuthentificationInformation (TunnelObject object) throws IOException {
    oos.writeObject(object);
  }

  @Override
  public void run () {
    try {
      while (running) {
        System.out.println("Waiting for object to be received...");
        TunnelObject recibido = (TunnelObject) ois.readObject();
        //TODO: Tratar lo que ha recibido --> AuthenticationInfo
      }
    } catch (IOException | ClassNotFoundException e) {
      e.printStackTrace();
    }
  }
}
