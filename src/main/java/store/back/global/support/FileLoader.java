package store.back.global.support;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

public class FileLoader {

    public static List<String> readFile(String filePath) throws IOException {
        try (InputStream inputStream = FileLoader.class.getClassLoader().getResourceAsStream(filePath)) {
            assert inputStream != null;
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

            return reader.lines().toList();
        }
    }
}
