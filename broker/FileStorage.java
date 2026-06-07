package broker;

import java.io.File;

/**
 * Utility for managing file paths for topic storage.
 */
public final class FileStorage {

    /**
     * Private constructor to prevent instantiation.
     */
    private FileStorage() {
    }

    /**
     * Gets the log file for a specific topic.
     *
     * @param topic the topic name
     * @return the log file
     */
    public static File getLogFile(final String topic) {
        return new File(topic + ".log");
    }
}

