package controllers;

import io.javalin.http.Context;
import models.Client;
import observer.*;

import views.ClientDetailsView;
import views.ErrorView;

public class ClientDetailsController {

    private final ObservableClientRepository repo;

    public ClientDetailsController(ObservableClientRepository repo) {
        this.repo = repo;
    }

    public void handle(Context ctx) {
        try {
            int id = Integer.parseInt(ctx.pathParam("id"));

            final Holder<Client> holder = new Holder<>();

            RepoObserver obs = event -> {
                if (event.getType() == RepoEventType.CLIENT_LOADED) {
                    holder.value = (Client) event.getPayload();
                }
            };

            repo.addObserver(obs);
            repo.getById(id);
            repo.removeObserver(obs);

            ctx.contentType("text/html; charset=utf-8");
            ctx.result(ClientDetailsView.render(holder.value));
        } catch (Exception e) {
            ctx.status(500).result(ErrorView.render("Ошибка", e.toString()));
        }
    }

    private static class Holder<T> { T value; }
}
