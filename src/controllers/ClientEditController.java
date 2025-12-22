package controllers;

import io.javalin.http.Context;
import models.Client;
import observer.ObservableClientRepository;
import validation.ClientValidator;
import views.ClientFormView;
import views.ErrorView;
import views.PopupCloseView;

import java.util.List;

public class ClientEditController {

    private final ObservableClientRepository repo;

    public ClientEditController(ObservableClientRepository repo) {
        this.repo = repo;
    }

    public void showForm(Context ctx) {
        try {
            int id = Integer.parseInt(ctx.pathParam("id"));
            Client c = repo.getById(id);

            if (c == null) {
                ctx.status(404).result(ErrorView.render("Не найдено", "Клиент id=" + id + " не найден"));
                return;
            }

            ctx.contentType("text/html; charset=utf-8");
            ctx.result(ClientFormView.render(
                    "Редактировать клиента",
                    "/clients/" + id,
                    "Сохранить",
                    c.getName(),
                    c.getAddress(),
                    c.getPhone(),
                    c.getEmail(),
                    c.getContactPerson(),
                    c.getTaxId(),
                    c.getRegistrationNumber(),
                    List.of()
            ));
        } catch (Exception e) {
            ctx.status(500).result(ErrorView.render("Ошибка", e.toString()));
        }
    }

    public void submit(Context ctx) {
        try {
            int id = Integer.parseInt(ctx.pathParam("id"));

            String name = ctx.formParam("name");
            String address = ctx.formParam("address");
            String phone = ctx.formParam("phone");
            String email = ctx.formParam("email");
            String contactPerson = ctx.formParam("contactPerson");
            String taxId = ctx.formParam("taxId");
            String registrationNumber = ctx.formParam("registrationNumber");

            List<String> errors = ClientValidator.validate(name, phone, email, taxId);
            if (!errors.isEmpty()) {
                ctx.status(400);
                ctx.contentType("text/html; charset=utf-8");
                ctx.result(ClientFormView.render(
                        "Редактировать клиента",
                        "/clients/" + id,
                        "Сохранить",
                        name, address, phone, email, contactPerson, taxId, registrationNumber,
                        errors
                ));
                return;
            }

            Client newValue = new Client(id, name, address, phone, email, contactPerson, taxId, registrationNumber);
            boolean ok = repo.replaceById(id, newValue);

            if (!ok) {
                ctx.status(404).result(ErrorView.render("Не найдено", "Клиент id=" + id + " не найден"));
                return;
            }

            ctx.contentType("text/html; charset=utf-8");
            ctx.result(PopupCloseView.render("Клиент обновлён ✅"));
        } catch (Exception e) {
            ctx.status(500).result(ErrorView.render("Ошибка редактирования", e.toString()));
        }
    }
}
