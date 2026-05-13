// consumer/ConsumerClient.java

package consumer;

import java.util.List;
import java.util.Scanner;
import java.util.ArrayList;
import broker.TopicManager;

public class ConsumerClient {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter topic to consume: ");

        String topic = scanner.nextLine();

        while (true) {

            List<Object> messages =
                    TopicManager.getMessages(topic);

            System.out.println(
                    "\nMessages in topic [" + topic + "]"
            );

            for (Object message : messages) {
                // TopicManager.Message is a static inner class
                // We reflect the actual type at runtime for clarity:
                // Assuming it's a TopicManager.Message
                try {
                    java.lang.reflect.Method getPayload = message.getClass().getMethod("getPayload");
                    Object payload = getPayload.invoke(message);
                    System.out.println(payload);
                } catch (Exception e) {
                    System.out.println(message.toString());
                }
            }

            try {
                Thread.sleep(5000);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}