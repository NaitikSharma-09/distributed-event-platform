package shared;

/**
 * Shared constants for the distributed event platform.
 */
public final class Constants {

    /**
     * The host name for the broker server.
     */
    public static final String HOST = "localhost";

    /**
     * The port number for the broker server.
     */
    public static final int PORT = 9092;

    /**
     * Prefix for subscription requests.
     */
    public static final String SUBSCRIBE_PREFIX = "SUBSCRIBE:";

    /**
     * separator for topic and payload.
     */
    public static final String MESSAGE_SEPARATOR = ":";

    /**
     * Error message for empty topics.
     */
    public static final String ERROR_EMPTY_TOPIC = "ERROR: Topic name cannot be empty";

    /**
     * Private constructor to prevent instantiation.
     */
    private Constants() {
    }
}