package models;

public class Main {
    public static void main(String[] args) {

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

        System.out.println("Client full:  " + client.toString());
        System.out.println("Client full:  " + client);
        System.out.println("Client short: " + client.toShortString());
        System.out.println();

        // 2. Краткий клиент из полного
        ClientShort shortFromFull = new ClientShort(client);
        System.out.println("ClientShort from Client: " + shortFromFull.toShortString());
        System.out.println();

        // 3. Клиент из CSV
        String csv = "2,ООО Луна,Санкт-Петербург,+79990001122,luna@mail.ru,Сидоров С.С.,55544433333,999888777";
        Client clientCsv = new Client(csv);

        System.out.println("CSV full:  " + clientCsv.toString());
        System.out.println("CSV short: " + clientCsv.toShortString());
        System.out.println();

        // 4. Клиент из JSON
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

        System.out.println("JSON full:  " + clientJson.toString());
        System.out.println("JSON short: " + clientJson.toShortString());
    }
}
