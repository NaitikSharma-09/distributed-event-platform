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

                Runnable clientHandler = new Runnable() {
                    @Override
                    public void run() {
                        try {
                            BufferedReader reader = new BufferedReader(
                                new InputStreamReader(socket.getInputStream())
                            );
                            String message;
                            while ((message = reader.readLine()) != null) {
                                // Split message into topic and payload
                                int idx = message.indexOf(':');
                                if (idx != -1) {
                                    String topic = message.substring(0, idx);
                                    String payload = message.substring(idx + 1);
                                    LogWriter.write(topic, payload);
                                    TopicManager.addMessage(topic, new Message(topic, payload));
                                    System.out.println("Received from client -> Topic: " + topic + ", Message: " + payload);
                                } else {
                                    System.out.println("Malformed message from client: " + message);
                                }
                            }
                            socket.close();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                };

                new Thread(clientHandler).start();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}