package controllers;

import io.javalin.http.Context;
import models.ClientShort;
import observer.ObservableClientRepository;
import observer.RepoEventType;
import observer.RepoObserver;
import views.ClientListView;
import views.ErrorView;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

public class ClientListController {

    private final ObservableClientRepository repo;

    public ClientListController(ObservableClientRepository repo) {
        this.repo = repo;
    }

    public void handle(Context ctx) {
        try {
            String q = ctx.queryParam("q");

            int k = parseIntOrDefault(ctx.queryParam("k"), 20);
            int n = parseIntOrDefault(ctx.queryParam("n"), 1);
            if (k <= 0) k = 20;
            if (n <= 0) n = 1;

            AtomicReference<List<ClientShort>> pageRef = new AtomicReference<>(List.of());

            RepoObserver obs = event -> {
                if (event.getType() == RepoEventType.PAGE_LOADED) {
                    @SuppressWarnings("unchecked")
                    List<ClientShort> list = (List<ClientShort>) event.getPayload();
                    pageRef.set(list == null ? List.of() : list);
                }
            };

            repo.addObserver(obs);
            repo.getShortPage(k, n);      // ✅ ВАЖНО: вызываем твой метод
            repo.removeObserver(obs);

            List<ClientShort> list = pageRef.get();

            // ✅ фильтрация по названию
            if (q != null && !q.trim().isEmpty()) {
                String needle = q.trim().toLowerCase();
                list = list.stream()
                        .filter(cs -> cs.getName() != null && cs.getName().toLowerCase().contains(needle))
                        .collect(Collectors.toList());
            }

            ctx.contentType("text/html; charset=utf-8");
            ctx.result(ClientListView.render(list, q));
        } catch (Exception e) {
            ctx.status(500).result(ErrorView.render("Ошибка", e.toString()));
        }
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
