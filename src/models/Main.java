package models;

import models.Client;
import models.ClientShort;

public class    Main {
    public static void main(String[] args) {

        // Создание объекта Client
        Client client = new Client(1, "ООО Ромашка", "Москва, ул. Ленина", "+79991234567",
                "info@romashka.ru", "Иванов И.И.", "1234567890", "987654321");

        System.out.println("Client full: " + client.toString());
        System.out.println("Client short: " + client.toShortString());

        // Создание объекта ClientShort
        ClientShort clientShort = new ClientShort(client);

        System.out.println("ClientShort fullInfo: " + clientShort.fullInfo());
        System.out.println("ClientShort shortInfo: " + clientShort.shortInfo());
    }
}
