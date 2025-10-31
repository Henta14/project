package models;

public class ClientShort extends Client {

    public ClientShort(Client client) {
        super(client.getClientId(),
                client.getName(),
                client.getContactPerson(),
                client.getTaxId(),
                client.getRegistrationNumber());
    }

    // Полная информация
    public String fullInfo() {
        return getName() + ", " + getContactPerson() + ", " + getTaxId() + ", " + getRegistrationNumber();
    }

    // Краткая информация
    public String shortInfo() {
        return getName() + " (" + getContactPerson() + ")";
    }
}
