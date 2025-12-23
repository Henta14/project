package repos;

import db.DbManager;
import models.Client;
import models.ClientShort;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClientRepDb {

    private final DbManager db;

    public ClientRepDb(DbManager db) {
        this.db = db;
    }

    private Connection open() throws SQLException {
        return db.getConnection();
    }

    // a) Получить объект по ID
    public Client getById(int id) {
        String sql = """
                SELECT client_id, name, address, phone, email, contact_person, tax_id, registration_number
                FROM clients
                WHERE client_id = ?
                """;

        try (Connection con = open();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return null;
                return mapClient(rs);
            }

        } catch (SQLException e) {
            throw new RuntimeException("DB: ошибка getById", e);
        }
    }

    // b) get_k_n_short_list: k элементов на странице n (1..)
    public List<ClientShort> getKthNShortList(int k, int n) {
        if (k <= 0 || n <= 0) throw new IllegalArgumentException("k и n должны быть > 0");
        int offset = (n - 1) * k;

        // Берём полный клиент, потом делаем ClientShort через new ClientShort(client)
        String sql = """
                SELECT client_id, name, address, phone, email, contact_person, tax_id, registration_number
                FROM clients
                ORDER BY name
                LIMIT ? OFFSET ?
                """;

        List<ClientShort> out = new ArrayList<>();

        try (Connection con = open();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, k);
            ps.setInt(2, offset);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Client full = mapClient(rs);
                    out.add(new ClientShort(full));
                }
            }

            return out;

        } catch (SQLException e) {
            throw new RuntimeException("DB: ошибка getKthNShortList", e);
        }
    }

    // c) Добавить (сформировать новый ID)
    public Client add(Client item) {
        String insertSql = """
                INSERT INTO clients(client_id, name, address, phone, email, contact_person, tax_id, registration_number)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?)
                """;

        try (Connection con = open()) {
            con.setAutoCommit(false);

            int newId = nextId(con);

            try (PreparedStatement ps = con.prepareStatement(insertSql)) {
                ps.setInt(1, newId);
                ps.setString(2, item.getName());
                ps.setString(3, item.getAddress());
                ps.setString(4, item.getPhone());
                ps.setString(5, item.getEmail());
                ps.setString(6, item.getContactPerson());
                ps.setString(7, item.getTaxId());
                ps.setString(8, item.getRegistrationNumber());
                ps.executeUpdate();
            }

            con.commit();

            return new Client(
                    newId,
                    item.getName(),
                    item.getAddress(),
                    item.getPhone(),
                    item.getEmail(),
                    item.getContactPerson(),
                    item.getTaxId(),
                    item.getRegistrationNumber()
            );

        } catch (SQLException e) {
            throw new RuntimeException("DB: ошибка add", e);
        }
    }

    private int nextId(Connection con) throws SQLException {
        String sql = "SELECT COALESCE(MAX(client_id), 0) + 1 AS next_id FROM clients";
        try (Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            rs.next();
            return rs.getInt("next_id");
        }
    }

    // d) Заменить элемент списка по ID
    public boolean replaceById(int id, Client newValue) {
        String sql = """
                UPDATE clients
                SET name = ?, address = ?, phone = ?, email = ?, contact_person = ?, tax_id = ?, registration_number = ?
                WHERE client_id = ?
                """;

        try (Connection con = open();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, newValue.getName());
            ps.setString(2, newValue.getAddress());
            ps.setString(3, newValue.getPhone());
            ps.setString(4, newValue.getEmail());
            ps.setString(5, newValue.getContactPerson());
            ps.setString(6, newValue.getTaxId());
            ps.setString(7, newValue.getRegistrationNumber());
            ps.setInt(8, id);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new RuntimeException("DB: ошибка replaceById", e);
        }
    }

    // e) Удалить элемент списка по ID
    public boolean deleteById(int id) {
        String sql = "DELETE FROM clients WHERE client_id = ?";

        try (Connection con = open();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new RuntimeException("DB: ошибка deleteById", e);
        }
    }

    // f) get_count
    public int getCount() {
        String sql = "SELECT COUNT(*) AS cnt FROM clients";

        try (Connection con = open();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            rs.next();
            return rs.getInt("cnt");

        } catch (SQLException e) {
            throw new RuntimeException("DB: ошибка getCount", e);
        }
    }

    // helper
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
