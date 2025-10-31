package models;

public class ClientShort {
    private String name;           // Имя клиента
    private String contactPerson;  // Контактное лицо
    private String taxId;          // ИНН
    private String registrationNumber; // ОГРН

    // Конструктор из полного объекта Client
    public ClientShort(Client client) {
        this.name = client.getName();
        this.contactPerson = client.getContactPerson();
        this.taxId = client.getTaxId();
        this.registrationNumber = client.getRegistrationNumber();
    }

    // Метод для вывода полной информации краткой версии
    public String fullInfo() {
        return name + ", " + contactPerson + ", " + taxId + ", " + registrationNumber;
    }

    // Метод для вывода краткой информации
    public String shortInfo() {
        return name + " (" + contactPerson + ")";
    }

    // Геттеры (при необходимости)
    public String getName() { return name; }
    public String getContactPerson() { return contactPerson; }
    public String getTaxId() { return taxId; }
    public String getRegistrationNumber() { return registrationNumber; }
}
