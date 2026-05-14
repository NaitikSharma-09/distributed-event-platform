// broker/TopicManager.java
package broker;

import java.util.*;

public class TopicManager {

    private static final Map<String,
            List<Message>> topics =
            new HashMap<>();

    public static synchronized void addMessage(
            String topic,
            Message message
    ) {

        topics.putIfAbsent(
                topic,
                new ArrayList<>()
        );

        topics.get(topic).add(message);
    }

    public static synchronized List<Message>
    getMessages(String topic) {

        return topics.getOrDefault(
                topic,
                new ArrayList<>()
        );
    }
}