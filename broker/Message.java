package broker;

public class Message {

    private String topic;
    private String payload;

    public Message(String topic, String payload) {
        this.topic = topic;
        this.payload = payload;
    }

    public String getTopic() {
        return topic;
    }

    public String getPayload() {
        return payload;
    }
}