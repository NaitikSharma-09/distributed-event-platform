package broker;

public class ProducerHandler implements Runnable {
    private final String topic;
    private final String payload;

    public ProducerHandler(String topic, String payload) {
        this.topic = topic;
        this.payload = payload;
    }

    @Override
    public void run() {
        Message message = new Message(topic, payload);
        TopicManager.addMessage(topic, message);
        LogWriter.write(topic, payload);
    }
}
