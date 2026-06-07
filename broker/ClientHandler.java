// broker/ClientHandler.java

package broker;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * Handles communication with a single client.
 * Supports both producing messages and subscribing to topics.
 */
public class ClientHandler implements Runnable {

    private static final String SUBSCRIBE_PREFIX = "SUBSCRIBE:";

    private final Socket socket;

    public ClientHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try (
            Socket s = socket;
            BufferedReader reader = new BufferedReader(
                new InputStreamReader(s.getInputStream(), StandardCharsets.UTF_8));
            PrintWriter writer = new PrintWriter(
                new BufferedWriter(new OutputStreamWriter(s.getOutputStream(), StandardCharsets.UTF_8)),
                true)
        ) {
            String input;
            while ((input = reader.readLine()) != null) {
                input = input.trim();
                if (input.isEmpty()) {
                    continue;
                }

                System.out.println("Received: " + input);

                if (input.startsWith(SUBSCRIBE_PREFIX)) {
                    String topic = input.substring(SUBSCRIBE_PREFIX.length()).trim();
                    if (topic.isEmpty()) {
                        writer.println("ERROR: Topic name cannot be empty");
                        continue;
                    }

                    System.out.println("Consumer subscribed to " + topic);
                    consumeTopic(topic, writer);
                    return;
                }

                publishMessage(input);
            }
        } catch (IOException e) {
            System.err.println(
                "Client handler error for " + socket.getRemoteSocketAddress() + ": " + e.getMessage());
        }
    }

    private void consumeTopic(String topic, PrintWriter writer) {
        int messageIndex = 0;
        while (!Thread.currentThread().isInterrupted()) {
            List<Message> messages = TopicManager.getMessages(topic, messageIndex);
            for (Message message : messages) {
                writer.println(message.getPayload());
                if (writer.checkError()) {
                    return;
                }
                messageIndex++;
            }
            if (writer.checkError()) {
                return;
            }
            if (messages.isEmpty()) {
                TopicManager.waitForMessages();
            }
        }
    }

    private void publishMessage(String input) {
        String[] parts = input.split(":", 2);
        if (parts.length < 2) {
            System.err.println("Invalid format from client: " + input);
            return;
        }

        String topic = parts[0].trim();
        String payload = parts[1];

        if (topic.isEmpty()) {
            System.err.println("Empty topic received from client");
            return;
        }

        TopicManager.addMessage(topic, new Message(topic, payload));
        LogWriter.write(topic, payload);
        System.out.println("Stored message in [" + topic + "] : " + payload);
    }
}
