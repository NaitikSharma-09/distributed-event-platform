package broker;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;

public class ClientHandler implements Runnable {

    private Socket socket;

    public ClientHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {

        try {
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(socket.getInputStream())
            );

            String message;

            while ((message = reader.readLine()) != null) {
                System.out.println("Received: " + message);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}