package repos;

import models.Client;
import models.ClientShort;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;

public class ClientRepYaml implements ClientRepository {

    private final Path yamlPath;
    private final Yaml yaml;

    public ClientRepYaml(String filePath) {
        this.yamlPath = Paths.get(filePath);

        DumperOptions opt = new DumperOptions();
        opt.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        opt.setPrettyFlow(true);
        opt.setIndent(2);

        // ВАЖНО: indicatorIndent должен быть строго меньше indent.
        // Чтобы не ловить YAMLException — просто не трогаем indicatorIndent вообще.
        // opt.setIndicatorIndent(1);

        opt.setDefaultScalarStyle(DumperOptions.ScalarStyle.PLAIN);

        this.yaml = new Yaml(opt);
    }

    // a) Чтение всех значений из файла
    @Override
    public List<Client> readAll() {
        if (!Files.exists(yamlPath)) {
            return new ArrayList<>();
        }

        try {
            String content = Files.readString(yamlPath, StandardCharsets.UTF_8).trim();
            if (content.isEmpty()) return new ArrayList<>();

            Object loaded = yaml.load(content);
            if (loaded == null) return new ArrayList<>();

            if (!(loaded instanceof List<?> list)) {
                throw new IllegalArgumentException("YAML должен содержать список клиентов (List)");
            }

            List<Client> result = new ArrayList<>();
            for (Object item : list) {
                if (!(item instanceof Map<?, ?> rawMap)) {
                    throw new IllegalArgumentException("Элемент YAML списка должен быть Map");
                }

                @SuppressWarnings("unchecked")
                Map<String, Object> map = (Map<String, Object>) rawMap;

                result.add(fromMap(map));
            }

            return result;

        } catch (Exception e) {
            throw new RuntimeException("Ошибка чтения YAML: " + yamlPath, e);
        }
    }

    // b) Запись всех значений в файл
    @Override
    public void writeAll(List<Client> items) {
        try {
            Path parent = yamlPath.getParent();
            if (parent != null) Files.createDirectories(parent);

            List<Map<String, Object>> list = new ArrayList<>();
            for (Client c : items) {
                list.add(toMap(c));
            }

            String out = yaml.dump(list);

            Files.writeString(
                    yamlPath,
                    out,
                    StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.TRUNCATE_EXISTING
            );

        } catch (IOException e) {
            throw new RuntimeException("Ошибка записи YAML: " + yamlPath, e);
        }
    }

    // c) Получить объект по ID
    @Override
    public Client getById(int id) {
        for (Client c : readAll()) {
            if (c.getClientId() == id) return c;
        }
        return null;
    }

    // d) get_k_n_short_list
    @Override
    public List<ClientShort> getKthNShortList(int k, int n) {
        if (k <= 0 || n <= 0) throw new IllegalArgumentException("k и n должны быть > 0");

        List<Client> all = readAll();
        int start = (n - 1) * k;
        if (start >= all.size()) return new ArrayList<>();

        int end = Math.min(start + k, all.size());
        List<ClientShort> out = new ArrayList<>();

        for (int i = start; i < end; i++) {
            out.add(new ClientShort(all.get(i)));
        }

        return out;
    }

    // e) Сортировать элементы по выбранному полю (name)
    @Override
    public void sortByName() {
        List<Client> all = readAll();
        all.sort(Comparator.comparing(Client::getName, String.CASE_INSENSITIVE_ORDER));
        writeAll(all);
    }

    // f) Добавить объект (сформировать новый ID)
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

    // g) Заменить элемент списка по ID
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

    // h) Удалить элемент списка по ID
    @Override
    public boolean deleteById(int id) {
        List<Client> all = readAll();
        boolean removed = all.removeIf(c -> c.getClientId() == id);
        if (removed) writeAll(all);
        return removed;
    }

    // i) get_count
    @Override
    public int getCount() {
        return readAll().size();
    }

    // ===== helpers =====

    private Map<String, Object> toMap(Client c) {
        Map<String, Object> m = new LinkedHashMap<>();
        m.put("clientId", c.getClientId());
        m.put("name", c.getName());
        m.put("address", c.getAddress());
        m.put("phone", c.getPhone());
        m.put("email", c.getEmail());
        m.put("contactPerson", c.getContactPerson());
        m.put("taxId", c.getTaxId());
        m.put("registrationNumber", c.getRegistrationNumber());
        return m;
    }

    private Client fromMap(Map<String, Object> m) {
        int clientId = toInt(m.get("clientId"));
        String name = toStr(m.get("name"));
        String address = toStr(m.get("address"));
        String phone = toStr(m.get("phone"));
        String email = toStr(m.get("email"));
        String contactPerson = toStr(m.get("contactPerson"));
        String taxId = toStr(m.get("taxId"));
        String registrationNumber = toStr(m.get("registrationNumber"));

        return new Client(clientId, name, address, phone, email, contactPerson, taxId, registrationNumber);
    }

    private static String toStr(Object v) {
        return v == null ? "" : String.valueOf(v);
    }

    private static int toInt(Object v) {
        if (v == null) return 0;
        if (v instanceof Number n) return n.intValue();
        return Integer.parseInt(String.valueOf(v).trim());
    }
}
