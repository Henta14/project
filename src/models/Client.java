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

    private static class Parsed {
        int id;
        String name;
        String address;
        String phone;
        String email;
        String contactPerson;
        String taxId;
        String regNum;
    }

    private static Parsed parseCsv(String csvLine) {
        String[] parts = csvLine.split(",");
        if (parts.length != 8) {
            throw new IllegalArgumentException("Неверный формат CSV");
        }
        Parsed p = new Parsed();
        p.id = Integer.parseInt(parts[0].trim());
        p.name = parts[1].trim();
        p.address = parts[2].trim();
        p.phone = parts[3].trim();
        p.email = parts[4].trim();
        p.contactPerson = parts[5].trim();
        p.taxId = parts[6].trim();
        p.regNum = parts[7].trim();
        return p;
    }

    private static Parsed parseJson(String jsonString) {
        JSONObject obj = new JSONObject(jsonString);
        Parsed p = new Parsed();
        p.id = obj.getInt("clientId");
        p.name = obj.getString("name");
        p.address = obj.getString("address");
        p.phone = obj.getString("phone");
        p.email = obj.getString("email");
        p.contactPerson = obj.getString("contactPerson");
        p.taxId = obj.getString("taxId");
        p.regNum = obj.getString("registrationNumber");
        return p;
    }

    private Client(Parsed p) {
        this(p.id, p.name, p.address, p.phone, p.email, p.contactPerson, p.taxId, p.regNum);
    }

    // Конструктор из CSV
    public Client(String csvLine) {
        this(parseCsv(csvLine));
    }

    // Конструктор из JSON
    public Client(String jsonString, boolean isJson) {
        this(parseJson(jsonString));
    }

    public JSONObject toJsonObject() {
        JSONObject obj = new JSONObject();
        obj.put("clientId", getClientId());
        obj.put("name", getName());
        obj.put("address", address);
        obj.put("phone", phone);
        obj.put("email", email);
        obj.put("contactPerson", getContactPerson());
        obj.put("taxId", getTaxId());
        obj.put("registrationNumber", getRegistrationNumber());
        return obj;
    }

    public static Client fromJsonObject(JSONObject obj) {
        return new Client(obj.toString(), true);
    }

    // getters/setters
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
        if (!validatePhone(phone)) throw new IllegalArgumentException("Некорректный телефон");
        this.phone = phone;
    }

    public void setEmail(String email) {
        if (!validateEmail(email)) throw new IllegalArgumentException("Некорректный email");
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
