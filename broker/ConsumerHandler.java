package broker;

import java.io.PrintWriter;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ConsumerHandler implements Runnable {

    private final String topic;
    private final PrintWriter writer;

    public ConsumerHandler(String topic, PrintWriter writer) {
        this.topic = topic;
        this.writer = writer;
    }

    @Override
    public void run() {
        Set<String> sentMessages = new HashSet<>();
        try {
            while (true) {
                List<Message> messages = TopicManager.getMessages(topic);
                for (Message message : messages) {
                    if (!sentMessages.contains(message.getPayload())) {
                        writer.println(message.getPayload());
                        sentMessages.add(message.getPayload());
                    }
                }
                Thread.sleep(1000);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
