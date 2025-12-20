package repos;

import models.Client;
import models.ClientShort;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ClientRepJson implements ClientRepository {

    private final Path jsonPath;

    public ClientRepJson(String filePath) {
        this.jsonPath = Paths.get(filePath);
    }

    // ========= a) readAll =========
    @Override
    public List<Client> readAll() {
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

    // ========= b) writeAll =========
    @Override
    public void writeAll(List<Client> items) {
        try {
            Path parent = jsonPath.getParent();
            if (parent != null) Files.createDirectories(parent);

            JSONArray arr = new JSONArray();
            for (Client c : items) {
                arr.put(c.toJsonObject());
            }

            Files.writeString(
                    jsonPath,
                    arr.toString(2), // красивый JSON
                    StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.TRUNCATE_EXISTING
            );

        } catch (IOException e) {
            throw new RuntimeException("Ошибка записи JSON: " + jsonPath, e);
        }
    }

    // ========= c) getById =========
    @Override
    public Client getById(int id) {
        for (Client c : readAll()) {
            if (c.getClientId() == id) return c;
        }
        return null; // можно Optional, но для лабы норм так
    }

    // ========= d) getKthNShortList(k, n) =========
    @Override
    public List<ClientShort> getKthNShortList(int k, int n) {
        if (k <= 0 || n <= 0) throw new IllegalArgumentException("k и n должны быть > 0");

        List<Client> all = readAll();
        int start = (n - 1) * k;
        if (start >= all.size()) return new ArrayList<>();

        int end = Math.min(start + k, all.size());
        List<ClientShort> out = new ArrayList<>();

        for (int i = start; i < end; i++) {
            out.add(new ClientShort(all.get(i))); // Client extends ClientShort => ок
        }
        return out;
    }

    // ========= e) sort by name (сохраняем в файл) =========
    @Override
    public void sortByName() {
        List<Client> all = readAll();
        all.sort(Comparator.comparing(Client::getName, String.CASE_INSENSITIVE_ORDER));
        writeAll(all);
    }

    // ========= f) add (new ID) =========
    @Override
    public Client add(Client item) {
        List<Client> all = readAll();
        int newId = nextId(all);

        Client withId = new Client(
                newId,
                item.getName(),
                item.getAddress(),
                item.getPhone(),
                item.getEmail(),
                item.getContactPerson(),
                item.getTaxId(),
                item.getRegistrationNumber()
        );

        all.add(withId);
        writeAll(all);
        return withId;
    }

    private int nextId(List<Client> all) {
        int max = 0;
        for (Client c : all) {
            if (c.getClientId() > max) max = c.getClientId();
        }
        return max + 1;
    }

    // ========= g) replaceById =========
    @Override
    public boolean replaceById(int id, Client newValue) {
        List<Client> all = readAll();

        for (int i = 0; i < all.size(); i++) {
            if (all.get(i).getClientId() == id) {

                Client replaced = new Client(
                        id,
                        newValue.getName(),
                        newValue.getAddress(),
                        newValue.getPhone(),
                        newValue.getEmail(),
                        newValue.getContactPerson(),
                        newValue.getTaxId(),
                        newValue.getRegistrationNumber()
                );

                all.set(i, replaced);
                writeAll(all);
                return true;
            }
        }
        return false;
    }

    // ========= h) deleteById =========
    @Override
    public boolean deleteById(int id) {
        List<Client> all = readAll();
        boolean removed = all.removeIf(c -> c.getClientId() == id);
        if (removed) writeAll(all);
        return removed;
    }

    // ========= i) getCount =========
    @Override
    public int getCount() {
        return readAll().size();
    }
}
