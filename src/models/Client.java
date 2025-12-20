package models;

import org.json.JSONObject;

public class Client extends ClientShort {

    private String address;
    private String phone;
    private String email;

    // Валидация
    public static boolean validateEmail(String email) {
        return validate(email, "^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$");
    }

    public static boolean validatePhone(String phone) {
        return validate(phone, "^\\+?\\d{10,15}$");
    }

    public Client(int clientId,
                  String name,
                  String address,
                  String phone,
                  String email,
                  String contactPerson,
                  String taxId,
                  String registrationNumber) {

        super(clientId, name, contactPerson, taxId, registrationNumber);

        if (!validatePhone(phone)) {
            throw new IllegalArgumentException("Некорректный телефон");
        }
        if (!validateEmail(email)) {
            throw new IllegalArgumentException("Некорректный email");
        }

        this.address = address;
        this.phone = phone;
        this.email = email;
    }

    // Конструктор из CSV
    public Client(String csvLine) {
        String[] parts = csvLine.split(",");
        if (parts.length != 8) {
            throw new IllegalArgumentException("Неверный формат CSV");
        }

        int id = Integer.parseInt(parts[0].trim());
        String name = parts[1].trim();
        String address = parts[2].trim();
        String phone = parts[3].trim();
        String email = parts[4].trim();
        String contactPerson = parts[5].trim();
        String taxId = parts[6].trim();
        String regNum = parts[7].trim();

        this(id, name, address, phone, email, contactPerson, taxId, regNum);
    }

    // Конструктор из JSON
    public Client(String jsonString, boolean isJson) {
        JSONObject obj = new JSONObject(jsonString);

        int id = obj.getInt("clientId");
        String name = obj.getString("name");
        String address = obj.getString("address");
        String phone = obj.getString("phone");
        String email = obj.getString("email");
        String contactPerson = obj.getString("contactPerson");
        String taxId = obj.getString("taxId");
        String regNum = obj.getString("registrationNumber");

        this(id, name, address, phone, email, contactPerson, taxId, regNum);
    }

    public String getAddress() {
        return address;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    public void setPhone(String phone) {
        if (!validatePhone(phone)) {
            throw new IllegalArgumentException("Некорректный телефон");
        }
        this.phone = phone;
    }

    public void setEmail(String email) {
        if (!validateEmail(email)) {
            throw new IllegalArgumentException("Некорректный email");
        }
        this.email = email;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "Client{" +
                "clientId=" + getClientId() +
                ", name='" + getName() + '\'' +
                ", address='" + address + '\'' +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                ", contactPerson='" + getContactPerson() + '\'' +
                ", taxId='" + getTaxId() + '\'' +
                ", registrationNumber='" + getRegistrationNumber() + '\'' +
                '}';
    }

    @Override
    public String toShortString() {
        return super.toShortString();
    }
}
