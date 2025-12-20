package repos;

import models.Client;
import models.ClientShort;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public abstract class AbstractClientFileRepository implements ClientRepository {

    // Наследники должны уметь только загрузить/сохранить список
    protected abstract List<Client> load();
    protected abstract void save(List<Client> items);

    @Override
    public final List<Client> readAll() {
        return load();
    }

    @Override
    public final void writeAll(List<Client> items) {
        save(items);
    }

    @Override
    public Client getById(int id) {
        for (Client c : readAll()) {
            if (c.getClientId() == id) return c;
        }
        return null;
    }

    @Override
    public List<ClientShort> getKthNShortList(int k, int n) {
        if (k <= 0 || n <= 0) throw new IllegalArgumentException("k и n должны быть > 0");

        List<Client> all = readAll();
        int start = (n - 1) * k;
        if (start >= all.size()) return new ArrayList<>();

        int end = Math.min(start + k, all.size());
        List<ClientShort> out = new ArrayList<>();

        for (int i = start; i < end; i++) {
            out.add(new ClientShort(all.get(i)));
        }
        return out;
    }

    @Override
    public void sortByName() {
        List<Client> all = readAll();
        all.sort(Comparator.comparing(Client::getName, String.CASE_INSENSITIVE_ORDER));
        writeAll(all);
    }

    @Override
    public Client add(Client item) {
        List<Client> all = readAll();
        int newId = nextId(all);

        Client withId = copyWithId(newId, item);
        all.add(withId);

        writeAll(all);
        return withId;
    }

    @Override
    public boolean replaceById(int id, Client newValue) {
        List<Client> all = readAll();

        for (int i = 0; i < all.size(); i++) {
            if (all.get(i).getClientId() == id) {
                all.set(i, copyWithId(id, newValue));
                writeAll(all);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean deleteById(int id) {
        List<Client> all = readAll();
        boolean removed = all.removeIf(c -> c.getClientId() == id);
        if (removed) writeAll(all);
        return removed;
    }

    @Override
    public int getCount() {
        return readAll().size();
    }

    protected int nextId(List<Client> all) {
        int max = 0;
        for (Client c : all) {
            if (c.getClientId() > max) max = c.getClientId();
        }
        return max + 1;
    }

    protected Client copyWithId(int id, Client src) {
        return new Client(
                id,
                src.getName(),
                src.getAddress(),
                src.getPhone(),
                src.getEmail(),
                src.getContactPerson(),
                src.getTaxId(),
                src.getRegistrationNumber()
        );
    }
}
