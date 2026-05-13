// broker/TopicManager.java

package broker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TopicManager {

    // Define a simple Message class to resolve symbol errors
    public static class Message {
        private String payload;

        public Message(String payload) {
            this.payload = payload;
        }

        public String getPayload() {
            return payload;
        }

        @Override
        public String toString() {
            return payload;
        }
    }

    private static final Map<String, List<Message>> topics =
            new HashMap<>();

    public static synchronized void addMessage(
            String topic,
            Message message
    ) {
        topics.putIfAbsent(topic, new ArrayList<>());
        topics.get(topic).add(message);
        // Optionally, persist using LogWriter if desired, e.g.:
        // LogWriter.write(topic, message.getPayload());
    }

    public static synchronized List<Message> getMessages(
            String topic
    ) {
        return topics.getOrDefault(topic, new ArrayList<>());
    }
}