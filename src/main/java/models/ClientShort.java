package models;

public class ClientShort {

    private int clientId;
    private String name;
    private String contactPerson;
    private String taxId;
    private String registrationNumber;

    // Универсальный метод для всех проверок
    protected static boolean validate(String value, String regex) {
        return value != null && value.matches(regex);
    }

    public static boolean validateId(int id) {
        return id > 0;
    }

    public static boolean validateNotEmpty(String value) {
        return value != null && !value.trim().isEmpty();
    }

    public static boolean validateTaxId(String taxId) {
        return validate(taxId, "^\\d{10,12}$");
    }

    public static boolean validateRegNumber(String regNum) {
        return validate(regNum, "^\\d{8,15}$");
    }

    // Основной конструктор краткой версии
    public ClientShort(int clientId,
                       String name,
                       String contactPerson,
                       String taxId,
                       String registrationNumber) {

        if (!validateId(clientId)) {
            throw new IllegalArgumentException("Некорректный clientId");
        }
        if (!validateNotEmpty(name)) {
            throw new IllegalArgumentException("Имя не может быть пустым");
        }
        if (!validateNotEmpty(contactPerson)) {
            throw new IllegalArgumentException("Контактное лицо не может быть пустым");
        }
        if (!validateTaxId(taxId)) {
            throw new IllegalArgumentException("Некорректный taxId (10–12 цифр)");
        }
        if (!validateRegNumber(registrationNumber)) {
            throw new IllegalArgumentException("Некорректный registrationNumber (8–15 цифр)");
        }

        this.clientId = clientId;
        this.name = name;
        this.contactPerson = contactPerson;
        this.taxId = taxId;
        this.registrationNumber = registrationNumber;
    }

    // Конструктор копирования
    public ClientShort(ClientShort other) {
        this(
                other.getClientId(),
                other.getName(),
                other.getContactPerson(),
                other.getTaxId(),
                other.getRegistrationNumber()
        );
    }

    // Геттеры
    public int getClientId() {
        return clientId;
    }

    public String getName() {
        return name;
    }

    public String getContactPerson() {
        return contactPerson;
    }

    public String getTaxId() {
        return taxId;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    // Краткая информация
    public String toShortString() {
        return name + " (" + contactPerson + ")";
    }

    // Полная информация для краткого варианта
    @Override
    public String toString() {
        return "ClientShort{" +
                "clientId=" + clientId +
                ", name='" + name + '\'' +
                ", contactPerson='" + contactPerson + '\'' +
                ", taxId='" + taxId + '\'' +
                ", registrationNumber='" + registrationNumber + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ClientShort)) return false;
        ClientShort that = (ClientShort) o;
        return clientId == that.clientId &&
                name.equals(that.name) &&
                contactPerson.equals(that.contactPerson) &&
                taxId.equals(that.taxId) &&
                registrationNumber.equals(that.registrationNumber);
    }

    @Override
    public int hashCode() {
        int result = clientId;
        result = 31 * result + name.hashCode();
        result = 31 * result + contactPerson.hashCode();
        result = 31 * result + taxId.hashCode();
        result = 31 * result + registrationNumber.hashCode();
        return result;
    }
}
