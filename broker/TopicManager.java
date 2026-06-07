// broker/TopicManager.java
package broker;

import java.util.*;

/**
 * Manages topics and their messages in-memory.
 * Provides thread-safe methods for adding and retrieving messages.
 */
public class TopicManager {

    private static final Map<String, List<Message>> topics = new HashMap<>();

    /**
     * Adds a message to the specified topic and notifies waiting consumers.
     */
    public static synchronized void addMessage(String topic, Message message) {
        if (topic == null || message == null) return;
        topics.computeIfAbsent(topic, k -> new ArrayList<>()).add(message);
        TopicManager.class.notifyAll();
    }

    /**
     * Retrieves messages for a topic starting from a specific index.
     * Returns a copy of the list to ensure thread safety.
     */
    public static synchronized List<Message> getMessages(String topic, int fromIndex) {
        if (topic == null || fromIndex < 0) return Collections.emptyList();
        
        List<Message> allMessages = topics.get(topic);
        if (allMessages == null || fromIndex >= allMessages.size()) {
            return Collections.emptyList();
        }
        
        // Return only the new messages as a new list
        return new ArrayList<>(allMessages.subList(fromIndex, allMessages.size()));
    }

    /**
     * Blocks the current thread until new messages are available or a timeout occurs.
     */
    public static synchronized void waitForMessages() {
        try {
            // Wait for a maximum of 5 seconds to allow for periodic health checks
            TopicManager.class.wait(5000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}