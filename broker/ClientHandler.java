// broker/ClientHandler.java

package broker;

import java.io.*;
import java.net.Socket;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ClientHandler implements Runnable {

    private final Socket socket;

    public ClientHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {

        try {

            BufferedReader reader =
                    new BufferedReader(
                            new InputStreamReader(
                                    socket.getInputStream()
                            )
                    );

            PrintWriter writer =
                    new PrintWriter(
                            socket.getOutputStream(),
                            true
                    );

            String input;

            while ((input = reader.readLine()) != null) {

                System.out.println("Received: " + input);

                // CONSUMER SUBSCRIBE
                if (input.startsWith("SUBSCRIBE:")) {

                    String topic =
                            input.substring(10);

                    System.out.println(
                            "Consumer subscribed to " + topic
                    );

                    Set<String> sentMessages =
                            new HashSet<>();

                    while (true) {

                        List<Message> messages =
                                TopicManager.getMessages(topic);

                        for (Message message : messages) {

                            if (!sentMessages.contains(
                                    message.getPayload()
                            )) {

                                writer.println(
                                        message.getPayload()
                                );

                                sentMessages.add(
                                        message.getPayload()
                                );
                            }
                        }

                        Thread.sleep(1000);
                    }
                }

                // PRODUCER MESSAGE
                else {

                    String[] parts =
                            input.split(":", 2);

                    if (parts.length < 2) {

                        System.out.println(
                                "Invalid format"
                        );

                        continue;
                    }

                    String topic = parts[0];

                    String payload = parts[1];

                    Message message =
                            new Message(topic, payload);

                    TopicManager.addMessage(
                            topic,
                            message
                    );

                    LogWriter.write(
                            topic,
                            payload
                    );

                    System.out.println(
                            "Stored message in [" +
                                    topic +
                                    "] : " +
                                    payload
                    );
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}