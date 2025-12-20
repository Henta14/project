package models;

import db.DbManager;
import repos.ClientRepDb;
import repos.ClientRepDbAdapter;
import repos.ClientRepository;

public class ClientDbAdapterTestMain {
    public static void main(String[] args) {

        String url = "jdbc:postgresql://localhost:5433/lab_db";
        String user = "postgres";
        String pass = "postgres";

        // DbManager singleton
        DbManager.init(url, user, pass);

        // "реальный" DB репозиторий
        ClientRepDb dbRepo = new ClientRepDb(DbManager.getInstance());

        // адаптер -> теперь это ClientRepository
        ClientRepository repo = new ClientRepDbAdapter(dbRepo);

        System.out.println("COUNT (start) = " + repo.getCount());

        Client c = new Client(
                1,
                "ООО Адаптер-Тест",
                "Москва",
                "+79990000002",
                "adapter@mail.ru",
                "Иванов И.И.",
                "12345678901",
                "222333444"
        );

        Client saved = repo.add(c);
        int id = saved.getClientId();

        System.out.println("ADDED -> " + saved);
        System.out.println("COUNT = " + repo.getCount());

        System.out.println("getById(" + id + ") -> " + repo.getById(id));

        System.out.println("Short list page 1 (k=5):");
        for (ClientShort s : repo.getKthNShortList(5, 1)) {
            System.out.println("  " + s.toShortString());
        }

        Client upd = new Client(
                1,
                "ООО Адаптер-Тест (обновлено)",
                "Москва, Арбат",
                "+79990000002",
                "adapter_new@mail.ru",
                "Иванов И.И.",
                "12345678901",
                "222333444"
        );

        System.out.println("replaceById -> " + repo.replaceById(id, upd));
        System.out.println("getById(" + id + ") -> " + repo.getById(id));

        System.out.println("deleteById -> " + repo.deleteById(id));
        System.out.println("COUNT (end) = " + repo.getCount());
    }
}
