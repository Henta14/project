package repos;

import models.Client;
import models.ClientShort;

import java.util.List;

public class ClientRepDbAdapter implements ClientRepository {

    private final ClientRepDb dbRepo;

    public ClientRepDbAdapter(ClientRepDb dbRepo) {
        this.dbRepo = dbRepo;
    }

    // Эти методы для DB по заданию не требуются -> явно запрещаем
    @Override
    public List<Client> readAll() {
        throw new UnsupportedOperationException("readAll() не поддерживается для DB репозитория");
    }

    @Override
    public void writeAll(List<Client> items) {
        throw new UnsupportedOperationException("writeAll() не поддерживается для DB репозитория");
    }

    @Override
    public void sortByName() {
        throw new UnsupportedOperationException("sortByName() не поддерживается для DB репозитория");
    }

    // ====== а–f из пункта 4 (через делегацию) ======

    @Override
    public Client getById(int id) {
        return dbRepo.getById(id);
    }

    @Override
    public List<ClientShort> getKthNShortList(int k, int n) {
        return dbRepo.getKthNShortList(k, n);
    }

    @Override
    public Client add(Client item) {
        return dbRepo.add(item);
    }

    @Override
    public boolean replaceById(int id, Client newValue) {
        return dbRepo.replaceById(id, newValue);
    }

    @Override
    public boolean deleteById(int id) {
        return dbRepo.deleteById(id);
    }

    @Override
    public int getCount() {
        return dbRepo.getCount();
    }
}
