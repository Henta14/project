package repos.db;

public class ClientFilter {

    private final String nameContains;

    public ClientFilter(String nameContains) {
        this.nameContains = nameContains;
    }

    public String getNameContains() {
        return nameContains;
    }

    public boolean isEmpty() {
        return nameContains == null || nameContains.isBlank();
    }
}
