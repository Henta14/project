package repos;

import models.Client;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;

public class ClientRepYaml extends AbstractClientFileRepository {

    private final Path yamlPath;
    private final Yaml yaml;

    public ClientRepYaml(String filePath) {
        this.yamlPath = Paths.get(filePath);

        DumperOptions opt = new DumperOptions();
        opt.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        opt.setPrettyFlow(true);
        opt.setIndent(2);
        // indicatorIndent НЕ трогаем — иначе ловим YAMLException
        opt.setDefaultScalarStyle(DumperOptions.ScalarStyle.PLAIN);

        this.yaml = new Yaml(opt);
    }

    @Override
    protected List<Client> load() {
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

    @Override
    protected void save(List<Client> items) {
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

    // helpers
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
