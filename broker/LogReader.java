package broker;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Utility for reading messages from topic-specific log files.
 */
public final class LogReader {

    /**
     * Private constructor to prevent instantiation.
     */
    private LogReader() {
    }

    /**
     * Reads all messages from the log file for the specified topic.
     *
     * @param topic the topic name
     * @return a list of message payloads
     */
    public static List<String> read(final String topic) {
        final List<String> messages = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(FileStorage.getLogFile(topic)))) {
            String line;
            while ((line = reader.readLine()) != null) {
                messages.add(line);
            }
        } catch (IOException e) {
            System.err.println("Could not read log for topic [" + topic + "]: " + e.getMessage());
        }
        return messages;
    }
}
