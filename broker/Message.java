// broker/Message.java

package broker;

import java.util.Objects;

/**
 * Represents a single message in the system.
 */
public final class Message {

    /**
     * The topic this message belongs to.
     */
    private final String topic;

    /**
     * The message payload.
     */
    private final String payload;

    /**
     * Constructs a new Message.
     *
     * @param topic   the topic name
     * @param payload the message payload
     */
    public Message(final String topic, final String payload) {
        this.topic = topic;
        this.payload = payload;
    }

    /**
     * Gets the topic name.
     *
     * @return the topic
     */
    public String getTopic() {
        return topic;
    }

    /**
     * Gets the message payload.
     *
     * @return the payload
     */
    public String getPayload() {
        return payload;
    }

    @Override
    public String toString() {
        return "Message{" + "topic='" + topic + '\'' + ", payload='" + payload + '\'' + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Message message = (Message) o;
        return Objects.equals(topic, message.topic) && Objects.equals(payload, message.payload);
    }

    @Override
    public int hashCode() {
        return Objects.hash(topic, payload);
    }
}