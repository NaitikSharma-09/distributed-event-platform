package producer;

import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class ProducerClient {

    public static void main(String[] args) {

        try {

            Socket socket = new Socket("localhost", 9092);

            PrintWriter writer =
                    new PrintWriter(socket.getOutputStream(), true);

            Scanner scanner = new Scanner(System.in);

            System.out.println("Connected to Broker");

            while (true) {

                System.out.print("Enter topic: ");
                String topic = scanner.nextLine();

                System.out.print("Enter message: ");
                String payload = scanner.nextLine();

                String finalMessage = topic + ":" + payload;

                writer.println(finalMessage);

                System.out.println("Sent -> " + finalMessage);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
} 