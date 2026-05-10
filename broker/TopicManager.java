package broker;

import java.util.*;

public class TopicManager {

    private static final Map<String, List<Message>> topics = new HashMap<>();

    public static void addMessage(String topic, Message message) {

        topics.putIfAbsent(topic, new ArrayList<>());

        topics.get(topic).add(message);

        System.out.println("Message added to topic: " + topic);
    }
}
