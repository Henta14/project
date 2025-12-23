package repos.db;

public enum ClientSort {
    ID_ASC("client_id ASC"),
    ID_DESC("client_id DESC"),
    NAME_ASC("name ASC"),
    NAME_DESC("name DESC");

    private final String orderBySql;

    ClientSort(String orderBySql) {
        this.orderBySql = orderBySql;
    }

    public String toOrderBySql() {
        return orderBySql;
    }
}
