package broker;

import java.io.PrintWriter;
import java.util.List;

/**
 * Handles consuming messages for a specific topic in a separate thread.
 */
public final class ConsumerHandler implements Runnable {

    /**
     * The topic to consume from.
     */
    private final String topic;

    /**
     * The output writer to send messages to.
     */
    private final PrintWriter writer;

    /**
     * Constructs a new ConsumerHandler.
     *
     * @param topic  the topic name
     * @param writer the output writer
     */
    public ConsumerHandler(final String topic, final PrintWriter writer) {
        this.topic = topic;
        this.writer = writer;
    }

    /**
     * Main execution loop for the consumer handler.
     */
    @Override
    public void run() {
        int index = 0;
        while (!Thread.currentThread().isInterrupted()) {
            final List<Message> messages = TopicManager.getMessages(this.topic, index);
            for (final Message message : messages) {
                this.writer.println(message.getPayload());
                if (this.writer.checkError()) {
                    return;
                }
                index++;
            }
            if (this.writer.checkError()) {
                return;
            }
            if (messages.isEmpty()) {
                TopicManager.waitForMessages();
            }
        }
    }
}
