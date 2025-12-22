package controllers;

import io.javalin.http.Context;
import models.ClientShort;
import observer.*;

import views.ClientListView;
import views.ErrorView;

import java.util.List;

public class ClientListController {

    private final ObservableClientRepository repo;

    public ClientListController(ObservableClientRepository repo) {
        this.repo = repo;
    }

    public void handle(Context ctx) {
        try {
            int k = parseIntOr(ctx.queryParam("k"), 20);
            int n = parseIntOr(ctx.queryParam("n"), 1);

            final Holder<List<ClientShort>> holder = new Holder<>();

            RepoObserver obs = event -> {
                if (event.getType() == RepoEventType.PAGE_LOADED) {
                    @SuppressWarnings("unchecked")
                    List<ClientShort> list = (List<ClientShort>) event.getPayload();
                    holder.value = list;
                }
            };

            repo.addObserver(obs);
            repo.getShortPage(k, n);
            repo.removeObserver(obs);

            ctx.contentType("text/html; charset=utf-8");
            ctx.result(ClientListView.render(holder.value));
        } catch (Exception e) {
            ctx.status(500).result(ErrorView.render("Ошибка", e.toString()));
        }
    }

    private int parseIntOr(String s, int def) {
        try { return s == null ? def : Integer.parseInt(s); }
        catch (Exception ignored) { return def; }
    }

    private static class Holder<T> { T value; }
}
