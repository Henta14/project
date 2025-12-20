package models;

import repos.ClientRepDb;

public class ClientDbRepoTestMain {
    public static void main(String[] args) {

        String url = "jdbc:postgresql://localhost:5433/lab_db";
        String user = "postgres";
        String pass = "postgres"; // тот, что уже сработал в DbConnectTestMain

        ClientRepDb repo = new ClientRepDb(url, user, pass);

        System.out.println("COUNT (start) = " + repo.getCount());

        Client c = new Client(
                1,
                "ООО База-Тест",
                "Москва",
                "+79990000001",
                "dbtest@mail.ru",
                "Иванов И.И.",
                "12345678901",
                "111222333"
        );

        Client saved = repo.add(c);
        System.out.println("ADDED -> " + saved);
        System.out.println("COUNT = " + repo.getCount());

        int id = saved.getClientId();
        System.out.println("getById(" + id + ") -> " + repo.getById(id));

        System.out.println("Short list page 1 (k=5):");
        for (ClientShort s : repo.getKthNShortList(5, 1)) {
            System.out.println("  " + s.toShortString());
        }

        Client upd = new Client(
                1,
                "ООО База-Тест (обновлено)",
                "Москва, Тверская",
                "+79990000001",
                "dbtest_new@mail.ru",
                "Иванов И.И.",
                "12345678901",
                "111222333"
        );

        System.out.println("replaceById -> " + repo.replaceById(id, upd));
        System.out.println("getById(" + id + ") -> " + repo.getById(id));

        System.out.println("deleteById -> " + repo.deleteById(id));
        System.out.println("COUNT (end) = " + repo.getCount());
    }
}
