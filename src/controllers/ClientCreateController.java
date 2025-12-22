package controllers;

import io.javalin.http.Context;
import models.Client;
import observer.ObservableClientRepository;
import validation.ClientValidator;
import views.ClientCreateView;
import views.ErrorView;
import views.PopupCloseView;

import java.util.List;

public class ClientCreateController {

    private final ObservableClientRepository repo;

    public ClientCreateController(ObservableClientRepository repo) {
        this.repo = repo;
    }

    public void showForm(Context ctx) {
        ctx.contentType("text/html; charset=utf-8");
        ctx.result(ClientCreateView.render("", "", "", "", "", "", "", List.of()));
    }

    public void submit(Context ctx) {
        try {
            String name = ctx.formParam("name");
            String address = ctx.formParam("address");
            String phone = ctx.formParam("phone");
            String email = ctx.formParam("email");
            String contactPerson = ctx.formParam("contactPerson");
            String taxId = ctx.formParam("taxId");
            String registrationNumber = ctx.formParam("registrationNumber");

            List<String> errors = ClientValidator.validate(name, phone, email, taxId);
            if (!errors.isEmpty()) {
                ctx.status(400).result(ClientCreateView.render(
                        name, address, phone, email, contactPerson, taxId, registrationNumber, errors
                ));
                return;
            }

            // ВНИМАНИЕ: repo.add формирует новый ID сам (по ТЗ ЛР2).
            // Поэтому здесь id = 1 как "валидная заглушка" (твой Client не принимает 0).
            Client draft = new Client(
                    1, name, address, phone, email, contactPerson, taxId, registrationNumber
            );

            repo.add(draft);

            ctx.contentType("text/html; charset=utf-8");
            ctx.result(PopupCloseView.render("Клиент добавлен"));
        } catch (Exception e) {
            ctx.status(500).result(ErrorView.render("Ошибка добавления", e.toString()));
        }
    }
}
