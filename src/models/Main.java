package models;

public class Main {
    public static void main(String[] args) {
        Client c1 = new Client("1,ООО Ромашка,Москва,+79991234567,info@romashka.ru,Иванов И.И.,1234567890,987654321");
        Client c2 = new Client("2,ООО Лотос,СПб,+79995556677,lotos@mail.ru,Петров П.П.,111222333,444555666");

        // Полный вывод
        System.out.println(c1.toString());

        // Краткий вывод
        System.out.println(c2.toShortString());

        // Сравнение объектов
        System.out.println("c1 equals c2? " + c1.equals(c2));
    }
}
