package models;

public class Main {
    public static void main(String[] args) {
        // Из CSV
        Client c1 = new Client("1,ООО Ромашка,Москва,+79991234567,info@romashka.ru,Иванов И.И.,1234567890,987654321");
        System.out.println("Клиент из CSV: " + c1.getName());

        // Из JSON
        Client c2 = new Client("{\"clientId\":2,\"name\":\"ООО Лотос\",\"address\":\"СПб\",\"phone\":\"+79995556677\",\"email\":\"lotos@mail.ru\",\"contactPerson\":\"Петров П.П.\",\"taxId\":\"111222333\",\"registrationNumber\":\"444555666\"}", true);
        System.out.println("Клиент из JSON: " + c2.getName());
    }
}
