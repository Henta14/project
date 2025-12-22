package controllers;

import io.javalin.http.Context;
import models.ClientShort;
import observer.ObservableClientRepository;
import observer.RepoEventType;
import observer.RepoObserver;
import repos.db.ClientFilter;
import repos.db.ClientSort;
import views.ClientListView;
import views.ErrorView;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class ClientListController {

    private final ObservableClientRepository repo;

    public ClientListController(ObservableClientRepository repo) {
        this.repo = repo;
    }

    public void handle(Context ctx) {
        try {
            String q = ctx.queryParam("q");          // фильтр по имени
            String sortRaw = ctx.queryParam("sort"); // name_asc / name_desc / id_asc / id_desc

            int k = parseIntOrDefault(ctx.queryParam("k"), 20);
            int n = parseIntOrDefault(ctx.queryParam("n"), 1);
            if (k <= 0) k = 20;
            if (n <= 0) n = 1;

            ClientFilter filter = new ClientFilter(q);     // у тебя filter.isEmpty() внутри
            ClientSort sort = parseSort(sortRaw);          // преобразуем строку -> enum

            AtomicReference<List<ClientShort>> pageRef = new AtomicReference<>(List.of());

            RepoObserver obs = event -> {
                if (event.getType() == RepoEventType.PAGE_LOADED) {
                    @SuppressWarnings("unchecked")
                    List<ClientShort> list = (List<ClientShort>) event.getPayload();
                    pageRef.set(list == null ? List.of() : list);
                }
            };

            repo.addObserver(obs);
            repo.getShortPage(k, n, filter, sort); // ✅ вот тут ЛР2 решает сорт/фильтр SQL-ом
            repo.removeObserver(obs);

            ctx.contentType("text/html; charset=utf-8");
            ctx.result(ClientListView.render(pageRef.get(), q, sortRaw));
        } catch (Exception e) {
            ctx.status(500).result(ErrorView.render("Ошибка", e.toString()));
        }
    }

    private ClientSort parseSort(String sortRaw) {
        if (sortRaw == null) return ClientSort.NAME_ASC;

        return switch (sortRaw.toLowerCase()) {
            case "name_desc" -> ClientSort.NAME_DESC;
            case "id_asc" -> ClientSort.ID_ASC;
            case "id_desc" -> ClientSort.ID_DESC;
            default -> ClientSort.NAME_ASC;
        };
    }

    private int parseIntOrDefault(String s, int def) {
        try {
            if (s == null) return def;
            return Integer.parseInt(s.trim());
        } catch (Exception ignored) {
            return def;
        }
    }
}
