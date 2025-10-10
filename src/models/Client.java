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

    public Client(int clientId, String name, String address, String phone, String email,
                  String contactPerson, String taxId, String registrationNumber) {
        this.clientId = clientId;
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.email = email;
        this.contactPerson = contactPerson;
        this.taxId = taxId;
        this.registrationNumber = registrationNumber;
    }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getContactPerson() { return contactPerson; }
    public void setContactPerson(String contactPerson) { this.contactPerson = contactPerson; }
    public String getTaxId() { return taxId; }
    public void setTaxId(String taxId) { this.taxId = taxId; }
    public String getRegistrationNumber() { return registrationNumber; }
    public void setRegistrationNumber(String registrationNumber) { this.registrationNumber = registrationNumber; }

    public class Validator {

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
    }

}


