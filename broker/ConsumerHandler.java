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
        int messageIndex = 0;
        try {
            while (!Thread.currentThread().isInterrupted()) {
                List<Message> messages = TopicManager.getMessages(topic, messageIndex);
                for (Message message : messages) {
                    writer.println(message.getPayload());
                    if (writer.checkError()) break;
                    messageIndex++;
                }
                if (writer.checkError()) break;
                if (messages.isEmpty()) {
                    TopicManager.waitForMessages();
                }
            }
        } finally {
            writer.close();
        }
    }
}
