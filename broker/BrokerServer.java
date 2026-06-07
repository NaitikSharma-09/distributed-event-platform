// broker/BrokerServer.java

package broker;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import shared.Constants;

/**
 * The main entry point for the Broker Server.
 * Listens for client connections and delegates them to ClientHandlers.
 */
public final class BrokerServer {

    /**
     * Thread pool for managing client handler threads.
     */
    private static final ExecutorService THREAD_POOL = Executors.newCachedThreadPool();

    /**
     * Private constructor to prevent instantiation.
     */
    private BrokerServer() {
    }

    /**
     * Starts the Broker Server and listens for incoming connections.
     *
     * @param args command line arguments (not used)
     */
    public static void main(final String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(Constants.PORT)) {
            System.out.println("Broker Server started on port " + Constants.PORT);

            while (!Thread.currentThread().isInterrupted()) {
                final Socket socket = serverSocket.accept();
                System.out.println("New client connected: " + socket.getRemoteSocketAddress());
                THREAD_POOL.execute(new ClientHandler(socket));
            }
        } catch (IOException e) {
            System.err.println("Broker Server I/O error: " + e.getMessage());
        } finally {
            THREAD_POOL.shutdown();
        }
    }
}