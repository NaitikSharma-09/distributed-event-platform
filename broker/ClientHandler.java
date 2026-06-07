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

    public ClientHandler(final Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try (
            Socket s = this.socket;
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
                    handleSubscribe(input, writer);
                    return;
                }

                handlePublish(input);
            }
        } catch (IOException e) {
            System.err.println("Client handler error: " + e.getMessage());
        }
    }

    private void handleSubscribe(final String input, final PrintWriter writer) {
        final String topic = input.substring(SUBSCRIBE_PREFIX.length()).trim();
        if (topic.isEmpty()) {
            writer.println("ERROR: Topic name cannot be empty");
            return;
        }

        System.out.println("Consumer subscribed to " + topic);

        int index = 0;
        while (!Thread.currentThread().isInterrupted()) {
            final List<Message> messages = TopicManager.getMessages(topic, index);
            for (final Message message : messages) {
                writer.println(message.getPayload());
                if (writer.checkError()) {
                    return;
                }
                index++;
            }
            if (writer.checkError()) {
                return;
            }
            if (messages.isEmpty()) {
                TopicManager.waitForMessages();
            }
        }
    }

    private void handlePublish(final String input) {
        final String[] parts = input.split(":", 2);
        if (parts.length < 2) {
            System.err.println("Invalid format: " + input);
            return;
        }

        final String topic = parts[0].trim();
        final String payload = parts[1];

        if (topic.isEmpty()) {
            System.err.println("Empty topic received");
            return;
        }

        TopicManager.addMessage(topic, new Message(topic, payload));
        LogWriter.write(topic, payload);
        System.out.println("Stored message in [" + topic + "] : " + payload);
    }
}
