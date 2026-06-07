// broker/BrokerServer.java

package broker;

import java.net.ServerSocket;
import java.net.Socket;
import java.io.BufferedReader;
import java.io.InputStreamReader;

public class BrokerServer {

    public static void main(String[] args) {

        try {

            ServerSocket serverSocket = new ServerSocket(9092);

            System.out.println("Broker Server started on port 9092");

            while (true) {

                final Socket socket = serverSocket.accept();

                System.out.println("New client connected");

                new Thread(new ClientHandler(socket)).start();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}