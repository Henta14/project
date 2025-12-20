package models;

import db.DbManager;
import repos.ClientRepDb;
import repos.ClientRepDbDecorator;
import repos.db.ClientFilter;
import repos.db.ClientSort;

public class ClientDbDecoratorTestMain {
    public static void main(String[] args) {

        String url = "jdbc:postgresql://localhost:5433/lab_db";
        String user = "postgres";
        String pass = "postgres";

        DbManager.initIfNeeded(url, user, pass);

        ClientRepDb base = new ClientRepDb(DbManager.getInstance());
        ClientRepDbDecorator repo = new ClientRepDbDecorator(base);

        // накидаем пару клиентов
        repo.add(new Client(1, "ООО Ромашка", "Москва", "+79990000001", "a@a.ru", "Иванов", "1234567890",  "111222333"));
        repo.add(new Client(1, "ООО Луна",     "СПб",    "+79990000002", "b@b.ru", "Петров", "12345678901", "222333444"));
        repo.add(new Client(1, "ИП Романов",   "Казань", "+79990000003", "c@c.ru", "Сидоров","123456789012","333444555"));


        ClientFilter f = new ClientFilter("ром"); // найдёт "Ромашка" и "Романов"

        System.out.println("COUNT filter='ром' -> " + repo.getCount(f));

        System.out.println("Short list filter='ром', sort=NAME_ASC, page1(k=10):");
        for (ClientShort s : repo.getKthNShortList(10, 1, f, ClientSort.NAME_ASC)) {
            System.out.println("  " + s.toShortString());
        }
    }
}
