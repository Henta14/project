package observer;

import models.Client;
import models.ClientShort;
import repos.ClientRepository;
import repos.ClientRepDbDecorator;
import repos.db.ClientFilter;
import repos.db.ClientSort;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class ObservableClientRepository {

    private final ClientRepository repo;
    private final CopyOnWriteArrayList<RepoObserver> observers = new CopyOnWriteArrayList<>();

    public ObservableClientRepository(ClientRepository repo) {
        this.repo = repo;
    }

    public void addObserver(RepoObserver o) {
        observers.addIfAbsent(o);
    }

    public void removeObserver(RepoObserver o) {
        observers.remove(o);
    }

    private void notifyAllObservers(RepoEvent e) {
        for (RepoObserver o : observers) {
            o.onEvent(e);
        }
    }

    // ===== уже было: страница без фильтра/сорта =====
    public void getShortPage(int k, int n) {
        List<ClientShort> list = repo.getKthNShortList(k, n);
        notifyAllObservers(new RepoEvent(RepoEventType.PAGE_LOADED, list));
    }

    // ✅ НОВОЕ: страница с фильтром/сортировкой (для пункта 7)
    public void getShortPage(int k, int n, ClientFilter filter, ClientSort sort) {
        if (repo instanceof ClientRepDbDecorator dec) {
            List<ClientShort> list = dec.getKthNShortList(k, n, filter, sort);
            notifyAllObservers(new RepoEvent(RepoEventType.PAGE_LOADED, list));
            return;
        }

        // fallback (если вдруг не декоратор)
        getShortPage(k, n);
    }

    // ===== уже было: getById =====
    public Client getById(int id) {
        Client c = repo.getById(id);
        notifyAllObservers(new RepoEvent(RepoEventType.CLIENT_LOADED, c));
        return c;
    }

    // ===== уже было: count без фильтра =====
    public int getCount() {
        int count = repo.getCount();
        notifyAllObservers(new RepoEvent(RepoEventType.COUNT_LOADED, count));
        return count;
    }

    // ✅ НОВОЕ: count с фильтром (используем ЛР2)
    public int getCount(ClientFilter filter) {
        if (repo instanceof ClientRepDbDecorator dec) {
            int count = dec.getCount(filter);
            notifyAllObservers(new RepoEvent(RepoEventType.COUNT_LOADED, count));
            return count;
        }
        return getCount();
    }

    // ===== остальные методы репо (add/replace/delete) как у тебя уже есть =====
    public Client add(Client c) {
        Client added = repo.add(c);
        notifyAllObservers(new RepoEvent(RepoEventType.CLIENT_ADDED, added));
        return added;
    }

    public boolean replaceById(int id, Client c) {
        boolean ok = repo.replaceById(id, c);
        notifyAllObservers(new RepoEvent(RepoEventType.CLIENT_UPDATED, ok));
        return ok;
    }

    public boolean deleteById(int id) {
        boolean ok = repo.deleteById(id);
        notifyAllObservers(new RepoEvent(RepoEventType.CLIENT_DELETED, ok));
        return ok;
    }
}
