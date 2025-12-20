package models;

import repos.ClientRepYaml;

public class ClientYamlRepoTestMain {
    public static void main(String[] args) {

        ClientRepYaml repo = new ClientRepYaml("data/clients.yaml");

        // очищаем
        repo.writeAll(java.util.List.of());
        System.out.println("YAML очищен. Count = " + repo.getCount());
        System.out.println();

        Client a = new Client(1, "ООО Ромашка", "Москва", "+79991234567", "info@romashka.ru",
                "Иванов И.И.", "12345678901", "987654321");

        Client b = new Client(1, "ООО Луна", "СПб", "+79990001122", "luna@mail.ru",
                "Сидоров С.С.", "55544433333", "999888777");

        Client c = new Client(1, "ООО Звезда", "Казань", "+79995553322", "star@mail.ru",
                "Петров П.П.", "11122233333", "888777666");

        var aSaved = repo.add(a);
        var bSaved = repo.add(b);
        var cSaved = repo.add(c);

        System.out.println("Добавили, Count = " + repo.getCount());
        System.out.println(aSaved);
        System.out.println(bSaved);
        System.out.println(cSaved);
        System.out.println();

        repo.sortByName();
        System.out.println("Отсортировали по name");
        System.out.println();

        System.out.println("Short page 1 (k=2):");
        for (ClientShort s : repo.getKthNShortList(2, 1)) {
            System.out.println("  " + s.toShortString());
        }
        System.out.println();

        int id = bSaved.getClientId();
        System.out.println("getById(" + id + "): " + repo.getById(id));
        System.out.println();

        Client upd = new Client(1, "ООО Луна (обновлено)", "СПб, Невский", "+79990001122", "new_luna@mail.ru",
                "Сидоров С.С.", "55544433333", "999888777");

        System.out.println("replaceById(" + id + "): " + repo.replaceById(id, upd));
        System.out.println("getById(" + id + "): " + repo.getById(id));
        System.out.println();

        System.out.println("deleteById(" + aSaved.getClientId() + "): " + repo.deleteById(aSaved.getClientId()));
        System.out.println("Count = " + repo.getCount());
    }
}
