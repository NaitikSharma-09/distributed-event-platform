package broker;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class LogReader {

    public static List<String> read(String topic) {

        List<String> messages = new ArrayList<>();

        try {
            BufferedReader reader = new BufferedReader(
                    new FileReader(topic + ".log")
            );

            String line;

            while ((line = reader.readLine()) != null) {
                messages.add(line);
            }

            reader.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return messages;
    }
}
