// broker/TopicManager.java
package broker;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Manages topics and their messages in-memory.
 * Provides thread-safe methods for adding and retrieving messages.
 */
public final class TopicManager {

    /**
     * Map of topic names to their list of messages.
     */
    private static final Map<String, List<Message>> TOPICS = new HashMap<>();

    /**
     * Lock object for synchronization.
     */
    private static final Object LOCK = new Object();

    /**
     * Private constructor to prevent instantiation.
     */
    private TopicManager() {
    }

    /**
     * Adds a message to the specified topic and notifies waiting consumers.
     *
     * @param topic   the topic name
     * @param message the message to add
     */
    public static void addMessage(final String topic, final Message message) {
        if (topic == null || message == null) {
            return;
        }
        synchronized (LOCK) {
            TOPICS.computeIfAbsent(topic, k -> new ArrayList<>()).add(message);
            LOCK.notifyAll();
        }
    }

    /**
     * Retrieves messages for a topic starting from a specific index.
     * Returns a copy of the list to ensure thread safety.
     *
     * @param topic     the topic name
     * @param fromIndex the index to start from (inclusive)
     * @return a list of new messages, or an empty list if none
     */
    public static List<Message> getMessages(final String topic, final int fromIndex) {
        if (topic == null || fromIndex < 0) {
            return Collections.emptyList();
        }
        
        synchronized (LOCK) {
            final List<Message> allMessages = TOPICS.get(topic);
            if (allMessages == null || fromIndex >= allMessages.size()) {
                return Collections.emptyList();
            }
            
            return new ArrayList<>(allMessages.subList(fromIndex, allMessages.size()));
        }
    }

    /**
     * Blocks the current thread until new messages are available or a timeout occurs.
     */
    public static void waitForMessages() {
        synchronized (LOCK) {
            try {
                // Wait for a maximum of 5 seconds to allow for periodic health checks
                LOCK.wait(5000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}