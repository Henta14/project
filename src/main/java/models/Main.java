package models;

import repos.ClientRepJson;

public class Main {
    public static void main(String[] args) {

        // ===== 0) Твои базовые тесты (оставляем) =====
        Client client = new Client(
                1,
                "ООО Ромашка",
                "Москва, ул. Ленина",
                "+79991234567",
                "info@romashka.ru",
                "Иванов И.И.",
                "12345678901",
                "987654321"
        );

        System.out.println("Client full:  " + client);
        System.out.println("Client short: " + client.toShortString());
        System.out.println();

        ClientShort shortFromFull = new ClientShort(client);
        System.out.println("ClientShort from Client: " + shortFromFull.toShortString());
        System.out.println();

        String csv = "2,ООО Луна,Санкт-Петербург,+79990001122,luna@mail.ru,Сидоров С.С.,55544433333,999888777";
        Client clientCsv = new Client(csv);

        System.out.println("CSV full:  " + clientCsv);
        System.out.println("CSV short: " + clientCsv.toShortString());
        System.out.println();

        String json = """
                {
                  "clientId": 3,
                  "name": "ООО Звезда",
                  "address": "Казань",
                  "phone": "+79995553322",
                  "email": "star@mail.ru",
                  "contactPerson": "Петров П.П.",
                  "taxId": "11122233333",
                  "registrationNumber": "888777666"
                }
                """;

        Client clientJson = new Client(json, true);

        System.out.println("JSON full:  " + clientJson);
        System.out.println("JSON short: " + clientJson.toShortString());
        System.out.println();

        // ===== 1) Проверка репозитория JSON (пункт 1) =====
        System.out.println("===== JSON Repository tests =====");

        ClientRepJson repo = new ClientRepJson("data/clients.json");

        // a) чтение всех + i) count
        System.out.println("Count (before) = " + repo.getCount());

        // f) add с генерацией нового ID
        Client added1 = repo.add(client);
        Client added2 = repo.add(clientCsv);
        Client added3 = repo.add(clientJson);

        System.out.println("Added #1 -> " + added1);
        System.out.println("Added #2 -> " + added2);
        System.out.println("Added #3 -> " + added3);
        System.out.println("Count (after add) = " + repo.getCount());
        System.out.println();

        // c) getById
        int idToFind = added2.getClientId();
        Client found = repo.getById(idToFind);
        System.out.println("getById(" + idToFind + ") -> " + (found != null ? found : "NOT FOUND"));
        System.out.println();

        // e) sortByName
        repo.sortByName();
        System.out.println("Sorted by name (saved to file).");
        System.out.println();

        // d) get_k_n_short_list: k=2, n=1 и n=2
        System.out.println("Short list page 1 (k=2):");
        for (ClientShort cs : repo.getKthNShortList(2, 1)) {
            System.out.println("  " + cs.toShortString());
        }
        System.out.println();

        System.out.println("Short list page 2 (k=2):");
        for (ClientShort cs : repo.getKthNShortList(2, 2)) {
            System.out.println("  " + cs.toShortString());
        }
        System.out.println();

        // g) replaceById
        Client updated = new Client(
                999, // id не важен, заменим на idToFind внутри replace
                "ООО Луна (обновлено)",
                "Санкт-Петербург, Невский проспект",
                "+79990001122",
                "new_luna@mail.ru",
                "Сидоров С.С.",
                "55544433333",
                "999888777"
        );

        boolean replaced = repo.replaceById(idToFind, updated);
        System.out.println("replaceById(" + idToFind + ") -> " + replaced);
        System.out.println("After replace getById(" + idToFind + ") -> " + repo.getById(idToFind));
        System.out.println();

        // h) deleteById
        int idToDelete = added1.getClientId();
        boolean deleted = repo.deleteById(idToDelete);
        System.out.println("deleteById(" + idToDelete + ") -> " + deleted);
        System.out.println("Count (after delete) = " + repo.getCount());
        System.out.println("getById(" + idToDelete + ") -> " + repo.getById(idToDelete));
        System.out.println();

        System.out.println("===== DONE =====");
    }
}
