package broker;

import java.io.File;

public class FileStorage {
    // Placeholder utility for future storage implementations.
    // Keep minimal to avoid compile/runtime issues when referenced.
    public static File getLogFile(String topic) {
        return new File(topic + ".log");
    }
}

