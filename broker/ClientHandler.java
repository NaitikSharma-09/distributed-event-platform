// broker/ClientHandler.java

package broker;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;
import broker.TopicManager;

public class ClientHandler implements Runnable {

    private final Socket socket;

    public ClientHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {

        try {

            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(socket.getInputStream())
            );

            String input;

            while ((input = reader.readLine()) != null) {

                System.out.println("Raw Input: " + input);

                String[] parts = input.split(":", 2);

                if (parts.length < 2) {

                    System.out.println("Invalid message format");

                    continue;
                }

                String topic = parts[0];

                String payload = parts[1];

                // Use TopicManager.Message and TopicManager
                TopicManager.Message message = new TopicManager.Message(payload);

                TopicManager.addMessage(topic, message);

                System.out.println(
                        "Stored message in topic [" + topic + "] : " + payload
                );
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}