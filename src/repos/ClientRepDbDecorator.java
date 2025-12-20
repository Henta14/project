package repos;

import db.DbManager;
import models.Client;
import models.ClientShort;
import repos.db.ClientDbQueryRepository;
import repos.db.ClientFilter;
import repos.db.ClientSort;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClientRepDbDecorator implements ClientRepository, ClientDbQueryRepository {

    private final ClientRepository inner; // обычный интерфейс (через адаптер)
    private final DbManager db;

    public ClientRepDbDecorator(ClientRepDb dbRepo) {
        this.inner = new ClientRepDbAdapter(dbRepo);
        this.db = DbManager.getInstance();
    }

    // ===== обычные методы репозитория — просто делегируем =====
    @Override
    public List<Client> readAll() {
        return inner.readAll();
    }

    @Override
    public void writeAll(List<Client> items) {
        inner.writeAll(items);
    }

    @Override
    public Client getById(int id) {
        return inner.getById(id);
    }

    @Override
    public List<ClientShort> getKthNShortList(int k, int n) {
        return inner.getKthNShortList(k, n);
    }

    @Override
    public void sortByName() {
        inner.sortByName();
    }

    @Override
    public Client add(Client item) {
        return inner.add(item);
    }

    @Override
    public boolean replaceById(int id, Client newValue) {
        return inner.replaceById(id, newValue);
    }

    @Override
    public boolean deleteById(int id) {
        return inner.deleteById(id);
    }

    @Override
    public int getCount() {
        return inner.getCount();
    }

    // ===== расширенные методы с фильтром/сортировкой (это пункт 7) =====

    @Override
    public List<ClientShort> getKthNShortList(int k, int n, ClientFilter filter, ClientSort sort) {
        if (k <= 0 || n <= 0) throw new IllegalArgumentException("k и n должны быть > 0");
        if (sort == null) sort = ClientSort.NAME_ASC;

        int offset = (n - 1) * k;

        StringBuilder sql = new StringBuilder("""
                SELECT client_id, name, address, phone, email, contact_person, tax_id, registration_number
                FROM clients
                """);

        List<Object> params = new ArrayList<>();
        appendWhere(sql, params, filter);

        sql.append(" ORDER BY ").append(sort.toOrderBySql());
        sql.append(" LIMIT ? OFFSET ?");

        params.add(k);
        params.add(offset);

        List<ClientShort> out = new ArrayList<>();

        try (Connection con = db.getConnection();
             PreparedStatement ps = con.prepareStatement(sql.toString())) {

            bindParams(ps, params);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Client full = mapClient(rs);
                    out.add(new ClientShort(full));
                }
            }

            return out;

        } catch (SQLException e) {
            throw new RuntimeException("DB decorator: ошибка getKthNShortList(filter, sort)", e);
        }
    }

    @Override
    public int getCount(ClientFilter filter) {
        StringBuilder sql = new StringBuilder("SELECT COUNT(*) AS cnt FROM clients");
        List<Object> params = new ArrayList<>();
        appendWhere(sql, params, filter);

        try (Connection con = db.getConnection();
             PreparedStatement ps = con.prepareStatement(sql.toString())) {

            bindParams(ps, params);

            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                return rs.getInt("cnt");
            }

        } catch (SQLException e) {
            throw new RuntimeException("DB decorator: ошибка getCount(filter)", e);
        }
    }

    // ===== helpers =====

    private void appendWhere(StringBuilder sql, List<Object> params, ClientFilter filter) {
        if (filter == null || filter.isEmpty()) return;

        // фильтр по имени: LOWER(name) LIKE LOWER(?)
        sql.append(" WHERE LOWER(name) LIKE LOWER(?)");
        params.add("%" + filter.getNameContains().trim() + "%");
    }

    private void bindParams(PreparedStatement ps, List<Object> params) throws SQLException {
        for (int i = 0; i < params.size(); i++) {
            ps.setObject(i + 1, params.get(i));
        }
    }

    private Client mapClient(ResultSet rs) throws SQLException {
        return new Client(
                rs.getInt("client_id"),
                rs.getString("name"),
                rs.getString("address"),
                rs.getString("phone"),
                rs.getString("email"),
                rs.getString("contact_person"),
                rs.getString("tax_id"),
                rs.getString("registration_number")
        );
    }
}
