package models;

import org.json.JSONObject;

public class Client {
    private int clientId;
    private String name;
    private String address;
    private String phone;
    private String email;
    private String contactPerson;
    private String taxId;
    private String registrationNumber;

    // Универсальный метод для всех проверок
    private static boolean validate(String value, String regex) {
        return value != null && value.matches(regex);
    }

    public static boolean validateEmail(String email) {
        return validate(email, "^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$");
    }

    public static boolean validatePhone(String phone) {
        return validate(phone, "^\\+?\\d{10,15}$");
    }

    public static boolean validateNotEmpty(String value) {
        return value != null && !value.trim().isEmpty();
    }

    // Основной конструктор
    public Client(int clientId, String name, String address, String phone, String email,
                  String contactPerson, String taxId, String registrationNumber) {
        if (!validateNotEmpty(name)) throw new IllegalArgumentException("Имя не может быть пустым");
        if (!validatePhone(phone)) throw new IllegalArgumentException("Некорректный номер телефона");
        if (!validateEmail(email)) throw new IllegalArgumentException("Некорректный email");

        this.clientId = clientId;
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.email = email;
        this.contactPerson = contactPerson;
        this.taxId = taxId;
        this.registrationNumber = registrationNumber;
    }

    // Конструктор из CSV
    public Client(String csvLine) {
        String[] parts = csvLine.split(",");
        if (parts.length != 8) throw new IllegalArgumentException("Неверный формат CSV");

        int id = Integer.parseInt(parts[0].trim());
        String name = parts[1].trim();
        String address = parts[2].trim();
        String phone = parts[3].trim();
        String email = parts[4].trim();
        String contactPerson = parts[5].trim();
        String taxId = parts[6].trim();
        String regNum = parts[7].trim();

        // Используем основной конструктор для валидации
        new Client(id, name, address, phone, email, contactPerson, taxId, regNum);

        this.clientId = id;
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.email = email;
        this.contactPerson = contactPerson;
        this.taxId = taxId;
        this.registrationNumber = regNum;
    }

    // Конструктор из JSON
    public Client(String jsonString, boolean isJson) {
        JSONObject obj = new JSONObject(jsonString);
        this.clientId = obj.getInt("clientId");
        this.name = obj.getString("name");
        this.address = obj.getString("address");
        this.phone = obj.getString("phone");
        this.email = obj.getString("email");
        this.contactPerson = obj.getString("contactPerson");
        this.taxId = obj.getString("taxId");
        this.registrationNumber = obj.getString("registrationNumber");
    }

    // Геттеры и сеттеры
    public int getClientId() { return clientId; }
    public String getName() { return name; }
    public String getAddress() { return address; }
    public String getPhone() { return phone; }
    public String getEmail() { return email; }
    public String getContactPerson() { return contactPerson; }
    public String getTaxId() { return taxId; }
    public String getRegistrationNumber() { return registrationNumber; }

    public void setEmail(String email) {
        if (!validateEmail(email)) throw new IllegalArgumentException("Некорректный email");
        this.email = email;
    }

    public void setPhone(String phone) {
        if (!validatePhone(phone)) throw new IllegalArgumentException("Некорректный телефон");
        this.phone = phone;
    }

    public void setName(String name) {
        if (!validateNotEmpty(name)) throw new IllegalArgumentException("Имя не может быть пустым");
        this.name = name;
    }

    // ✅ Полная информация
    @Override
    public String toString() {
        return "Client{" +
                "clientId=" + clientId +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                ", contactPerson='" + contactPerson + '\'' +
                ", taxId='" + taxId + '\'' +
                ", registrationNumber='" + registrationNumber + '\'' +
                '}';
    }

    // ✅ Краткая информация
    public String toShortString() {
        return "Client{" +
                "name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                '}';
    }

    // ✅ Сравнение объектов по содержимому
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Client)) return false;
        Client client = (Client) o;
        return clientId == client.clientId &&
                name.equals(client.name) &&
                phone.equals(client.phone) &&
                email.equals(client.email);
    }

    @Override
    public int hashCode() {
        int result = clientId;
        result = 31 * result + name.hashCode();
        result = 31 * result + phone.hashCode();
        result = 31 * result + email.hashCode();
        return result;
    }
}
