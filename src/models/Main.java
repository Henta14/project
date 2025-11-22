package models;

import models.Client;
import models.ClientShort;

public class    Main {
    public static void main(String[] args) {

        // Создание объекта Client
        Client client = new Client(1, "ООО Ромашка", "Москва, ул. Ленина", "+79991234567",
                "info@romashka.ru", "Иванов И.И.", "12345678901", "987654321");

        System.out.println("Client full: " + client.toString());
        System.out.println("Client short: " + client.toShortString());

        // Создание объекта ClientShort
        ClientShort clientShort = new ClientShort(client);

        System.out.println("ClientShort fullInfo: " + clientShort.fullInfo());
        System.out.println("ClientShort shortInfo: " + clientShort.shortInfo());

        String csv = "2,ООО Луна,Санкт-Петербург,+79990001122,luna@mail.ru,Сидоров С.С.,55544433333,999888777";
        Client clientCsv = new Client(csv);

        System.out.println("Полная версия: " + clientCsv.toString());
        System.out.println("Краткая версия: " + clientCsv.toShortString());
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

        System.out.println("Полная версия: " + clientJson.toString());
        System.out.println("Краткая версия: " + clientJson.toShortString());
        System.out.println();
    }
}
