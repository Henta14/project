package repos;

import models.Client;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

public class ClientRepJson extends AbstractClientFileRepository {

    private final Path jsonPath;

    public ClientRepJson(String filePath) {
        this.jsonPath = Paths.get(filePath);
    }

    @Override
    protected List<Client> load() {
        if (!Files.exists(jsonPath)) {
            return new ArrayList<>();
        }

        try {
            String content = Files.readString(jsonPath, StandardCharsets.UTF_8).trim();
            if (content.isEmpty()) return new ArrayList<>();

            JSONArray arr = new JSONArray(content);
            List<Client> result = new ArrayList<>();

            for (int i = 0; i < arr.length(); i++) {
                JSONObject obj = arr.getJSONObject(i);
                result.add(Client.fromJsonObject(obj));
            }
            return result;

        } catch (Exception e) {
            throw new RuntimeException("Ошибка чтения JSON: " + jsonPath, e);
        }
    }

    @Override
    protected void save(List<Client> items) {
        try {
            Path parent = jsonPath.getParent();
            if (parent != null) Files.createDirectories(parent);

            JSONArray arr = new JSONArray();
            for (Client c : items) {
                arr.put(c.toJsonObject());
            }

            Files.writeString(
                    jsonPath,
                    arr.toString(2),
                    StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.TRUNCATE_EXISTING
            );

        } catch (IOException e) {
            throw new RuntimeException("Ошибка записи JSON: " + jsonPath, e);
        }
    }
}
