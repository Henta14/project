package observer;

import models.Client;
import models.ClientShort;
import repos.ClientRepository;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class ObservableClientRepository {

    private final ClientRepository repo;
    private final CopyOnWriteArrayList<RepoObserver> observers = new CopyOnWriteArrayList<>();

    public ObservableClientRepository(ClientRepository repo) {
        this.repo = repo;
    }

    public void addObserver(RepoObserver o) { observers.addIfAbsent(o); }
    public void removeObserver(RepoObserver o) { observers.remove(o); }

    private void notifyAllObservers(RepoEvent e) {
        for (RepoObserver o : observers) o.onEvent(e);
    }

    public List<ClientShort> getShortPage(int k, int n) {
        List<ClientShort> list = repo.getKthNShortList(k, n);
        notifyAllObservers(new RepoEvent(RepoEventType.PAGE_LOADED, list));
        return list;
    }

    public Client getById(int id) {
        Client c = repo.getById(id);
        notifyAllObservers(new RepoEvent(RepoEventType.CLIENT_LOADED, c));
        return c;
    }

    public int getCount() {
        return repo.getCount();
    }

    public Client add(Client item) { return repo.add(item); }
    public boolean replaceById(int id, Client item) { return repo.replaceById(id, item); }
    public boolean deleteById(int id) { return repo.deleteById(id); }
}
