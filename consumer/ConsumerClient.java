// consumer/ConsumerClient.java
package consumer;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class ConsumerClient {

    public static void main(String[] args) {

        try {

            Socket socket =
                    new Socket("localhost", 9092);

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

            Scanner scanner =
                    new Scanner(System.in);

            System.out.print(
                    "Enter topic to subscribe: "
            );

            String topic =
                    scanner.nextLine();

            writer.println("SUBSCRIBE:" + topic);

            System.out.println(
                    "Subscribed to topic: " + topic
            );

            String message;

            while ((message = reader.readLine()) != null) {

                System.out.println(
                        "Received -> " + message
                );
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}