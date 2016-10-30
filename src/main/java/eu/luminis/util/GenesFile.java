package eu.luminis.util;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import eu.luminis.genetics.Genome;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static java.nio.file.Files.readAllBytes;
import static java.nio.file.Paths.get;

public class GenesFile {
    private final String filePath;

    public GenesFile(String filePath) {
        this.filePath = filePath;
    }

    public List<Genome> read() throws IOException {
        String json = new String(readAllBytes(get(filePath)));
        Type listType = new TypeToken<ArrayList<Genome>>(){}.getType();

        return new Gson().fromJson(json, listType);
    }
}
