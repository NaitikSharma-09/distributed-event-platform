package producer;

import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;
import shared.Constants;

/**
 * A client that publishes messages to specific topics.
 */
public final class ProducerClient {

    /**
     * Private constructor to prevent instantiation.
     */
    private ProducerClient() {
    }

    /**
     * Starts the producer client.
     *
     * @param args command line arguments
     */
    public static void main(final String[] args) {
        try (
            Socket socket = new Socket(Constants.HOST, Constants.PORT);
            PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
            Scanner scanner = new Scanner(System.in)
        ) {
            System.out.println("Connected to Broker at " + Constants.HOST + ":" + Constants.PORT);

            while (true) {
                System.out.print("Enter topic (or 'exit'): ");
                if (!scanner.hasNextLine()) {
                    break;
                }
                final String topic = scanner.nextLine().trim();
                if (topic.equalsIgnoreCase("exit")) {
                    break;
                }

                System.out.print("Enter message: ");
                if (!scanner.hasNextLine()) {
                    break;
                }
                final String payload = scanner.nextLine();

                final String finalMessage = topic + Constants.MESSAGE_SEPARATOR + payload;
                writer.println(finalMessage);

                System.out.println("Sent -> " + finalMessage);
            }
        } catch (final Exception e) {
            System.err.println("Producer error: " + e.getMessage());
        }
    }
}
