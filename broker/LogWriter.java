package broker;

import java.io.FileWriter;
import java.io.IOException;

/**
 * Utility for writing messages to topic-specific log files.
 */
public final class LogWriter {

    /**
     * Private constructor to prevent instantiation.
     */
    private LogWriter() {
    }

    /**
     * Writes a payload to the log file for the specified topic.
     *
     * @param topic   the topic name
     * @param payload the message payload
     */
    public static synchronized void write(final String topic, final String payload) {
        try (FileWriter writer = new FileWriter(FileStorage.getLogFile(topic), true)) {
            writer.write(payload + "\n");
        } catch (IOException e) {
            System.err.println("Error writing to log for topic [" + topic + "]: " + e.getMessage());
        }
    }
}