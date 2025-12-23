package controllers;

import io.javalin.http.Context;
import observer.ObservableClientRepository;
import views.ErrorView;
import views.PopupCloseView;

public class ClientDeleteController {

    private final ObservableClientRepository repo;

    public ClientDeleteController(ObservableClientRepository repo) {
        this.repo = repo;
    }

    public void submit(Context ctx) {
        try {
            String rawId = ctx.pathParam("id");
            if (rawId == null || !rawId.matches("\\d+")) {
                ctx.status(404).result(ErrorView.render("Не найдено", "Некорректный id: " + rawId));
                return;
            }

            int id = Integer.parseInt(rawId);
            boolean ok = repo.deleteById(id);

            if (!ok) {
                ctx.status(404).result(ErrorView.render("Не найдено", "Клиент id=" + id + " не найден"));
                return;
            }

            ctx.contentType("text/html; charset=utf-8");
            ctx.result(PopupCloseView.render("Клиент удалён 🗑️"));
        } catch (Exception e) {
            ctx.status(500).result(ErrorView.render("Ошибка удаления", e.toString()));
        }
    }
}
