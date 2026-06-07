package broker;

import java.io.FileWriter;
import java.io.IOException;

public class LogWriter {

    public static synchronized void write(String topic, String payload) {
        try (FileWriter writer = new FileWriter(topic + ".log", true)) {
            writer.write(payload + "\n");
        } catch (IOException e) {
            System.err.println("Error writing to log: " + e.getMessage());
        }
    }
}