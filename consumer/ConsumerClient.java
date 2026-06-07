package consumer;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;
import shared.Constants;

/**
 * A client that consumes messages from a specific topic.
 */
public final class ConsumerClient {

    /**
     * Private constructor to prevent instantiation.
     */
    private ConsumerClient() {
    }

    /**
     * Starts the consumer client.
     *
     * @param args command line arguments
     */
    public static void main(final String[] args) {
        try (
            Socket socket = new Socket(Constants.HOST, Constants.PORT);
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
            Scanner scanner = new Scanner(System.in)
        ) {
            System.out.print("Enter topic to subscribe: ");
            if (scanner.hasNextLine()) {
                final String topic = scanner.nextLine();
                writer.println(Constants.SUBSCRIBE_PREFIX + topic);
                System.out.println("Subscribed to topic: " + topic);

                String message = reader.readLine();
                while (message != null) {
                    System.out.println("Received -> " + message);
                    message = reader.readLine();
                }
            }
        } catch (final Exception e) {
            System.err.println("Consumer error: " + e.getMessage());
        }
    }
}
