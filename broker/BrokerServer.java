package broker;

import java.net.ServerSocket;
import java.net.Socket;

public class BrokerServer {

    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(9092);

            System.out.println("Broker Server started on port 9092");

            while (true) {
                Socket socket = serverSocket.accept();

                System.out.println("New client connected");

                ClientHandler clientHandler = new ClientHandler(socket);
                new Thread(clientHandler).start();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}