package broker;

/**
 * Handles publishing a single message in a separate thread.
 */
public final class ProducerHandler implements Runnable {

    /**
     * The topic to publish to.
     */
    private final String topic;

    /**
     * The message payload.
     */
    private final String payload;

    /**
     * Constructs a new ProducerHandler.
     *
     * @param topic   the topic name
     * @param payload the message payload
     */
    public ProducerHandler(final String topic, final String payload) {
        this.topic = topic;
        this.payload = payload;
    }

    /**
     * Publishes the message and logs it.
     */
    @Override
    public void run() {
        final Message message = new Message(this.topic, this.payload);
        TopicManager.addMessage(this.topic, message);
        LogWriter.write(this.topic, this.payload);
    }
}
