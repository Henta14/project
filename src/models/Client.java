package models;

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

    // Проверки, использующие универсальный метод
    public static boolean validateEmail(String email) {
        return validate(email, "^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$");
    }

    public static boolean validatePhone(String phone) {
        return validate(phone, "^\\+?\\d{10,15}$");
    }

    public static boolean validateNotEmpty(String value) {
        return value != null && !value.trim().isEmpty();
    }

    // Основной конструктор с валидацией
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

    // Перегрузка конструктора — создание из CSV строки
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

    // Перегрузка конструктора — создание из JSON строки (простейший парсер)
    public Client(String json, boolean fromJson) {
        try {
            json = json.replaceAll("[{}\" ]", ""); // убрать фигурные скобки, кавычки, пробелы
            String[] pairs = json.split(",");
            String[] values = new String[8];
            for (int i = 0; i < pairs.length; i++) {
                String[] keyValue = pairs[i].split(":");
                values[i] = keyValue[1];
            }

            int id = Integer.parseInt(values[0]);
            String name = values[1];
            String address = values[2];
            String phone = values[3];
            String email = values[4];
            String contactPerson = values[5];
            String taxId = values[6];
            String regNum = values[7];

            // Проверка валидности
            if (!validateNotEmpty(name)) throw new IllegalArgumentException("Имя не может быть пустым");
            if (!validatePhone(phone)) throw new IllegalArgumentException("Некорректный телефон");
            if (!validateEmail(email)) throw new IllegalArgumentException("Некорректный email");

            this.clientId = id;
            this.name = name;
            this.address = address;
            this.phone = phone;
            this.email = email;
            this.contactPerson = contactPerson;
            this.taxId = taxId;
            this.registrationNumber = regNum;

        } catch (Exception e) {
            throw new IllegalArgumentException("Ошибка разбора JSON: " + e.getMessage());
        }
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
}
