package broker;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import shared.Constants;

/**
 * Handles communication with a single client.
 * Supports both producing messages and subscribing to topics.
 */
public final class ClientHandler implements Runnable {

    /**
     * The socket connected to the client.
     */
    private final Socket socket;

    /**
     * Constructs a new ClientHandler for the given socket.
     *
     * @param socket the client socket
     */
    public ClientHandler(final Socket socket) {
        this.socket = socket;
    }

    /**
     * Main execution loop for the client handler.
     * Reads commands and dispatches to appropriate handlers.
     */
    @Override
    public void run() {
        final String address = this.socket.getRemoteSocketAddress().toString();
        try (
            Socket s = this.socket;
            BufferedReader reader = new BufferedReader(
                new InputStreamReader(s.getInputStream(), StandardCharsets.UTF_8));
            PrintWriter writer = new PrintWriter(
                new BufferedWriter(new OutputStreamWriter(s.getOutputStream(), StandardCharsets.UTF_8)),
                true)
        ) {
            String input = reader.readLine();
            while (input != null) {
                final String trimmedInput = input.trim();
                if (!trimmedInput.isEmpty()) {
                    if (trimmedInput.startsWith(Constants.SUBSCRIBE_PREFIX)) {
                        handleSubscription(trimmedInput, writer, address);
                        return;
                    }
                    handlePublication(trimmedInput, address);
                }
                input = reader.readLine();
            }
        } catch (final IOException e) {
            logError("I/O error for client [" + address + "]: " + e.getMessage());
        } finally {
            logInfo("Client disconnected: " + address);
        }
    }

    /**
     * Handles a subscription request.
     *
     * @param input   the raw input string
     * @param writer  the output writer
     * @param address the client address
     */
    private void handleSubscription(final String input, final PrintWriter writer, final String address) {
        final String topic = input.substring(Constants.SUBSCRIBE_PREFIX.length()).trim();
        if (topic.isEmpty()) {
            writer.println(Constants.ERROR_EMPTY_TOPIC);
            return;
        }

        logInfo("Client [" + address + "] subscribed to topic: " + topic);
        new ConsumerHandler(topic, writer).run();
    }

    /**
     * Handles a publication request.
     *
     * @param input   the raw input string
     * @param address the client address
     */
    private void handlePublication(final String input, final String address) {
        final String[] parts = input.split(Constants.MESSAGE_SEPARATOR, 2);
        if (parts.length < 2) {
            logError("Invalid format from client [" + address + "]: " + input);
            return;
        }

        final String topic = parts[0].trim();
        final String payload = parts[1];

        if (topic.isEmpty()) {
            logError("Empty topic from client [" + address + "]");
            return;
        }

        new ProducerHandler(topic, payload).run();
        logInfo("Client [" + address + "] published to [" + topic + "]: " + payload);
    }

    /**
     * Logs an informational message.
     *
     * @param msg the message to log
     */
    private static void logInfo(final String msg) {
        System.out.println(msg);
    }

    /**
     * Logs an error message.
     *
     * @param msg the message to log
     */
    private static void logError(final String msg) {
        System.err.println(msg);
    }
}
