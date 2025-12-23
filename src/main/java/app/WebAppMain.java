package app;

import controllers.ClientCreateController;
import controllers.ClientDeleteController;
import controllers.ClientDetailsController;
import controllers.ClientEditController;
import controllers.ClientListController;
import db.DbManager;
import io.javalin.Javalin;
import observer.ObservableClientRepository;
import repos.ClientRepDb;
import repos.ClientRepDbDecorator;   // ✅ добавили
import repos.ClientRepository;

public class WebAppMain {

    public static void main(String[] args) {

        DbManager.initIfNeeded(
                "jdbc:postgresql://localhost:5433/lab_db",
                "postgres",
                "postgres"
        );

        // ✅ важно: используем декоратор (ЛР2 фильтр+сорт)
        ClientRepository repo = new ClientRepDbDecorator(
                new ClientRepDb(DbManager.getInstance())
        );

        ObservableClientRepository observableRepo = new ObservableClientRepository(repo);

        ClientListController listController = new ClientListController(observableRepo);
        ClientDetailsController detailsController = new ClientDetailsController(observableRepo);
        ClientCreateController createController = new ClientCreateController(observableRepo);
        ClientEditController editController = new ClientEditController(observableRepo);
        ClientDeleteController deleteController = new ClientDeleteController(observableRepo);

        Javalin app = Javalin.create(cfg -> cfg.http.defaultContentType = "text/html; charset=utf-8");

        app.get("/", listController::handle);

        app.get("/clients/new", createController::showForm);
        app.post("/clients", createController::submit);

        app.get("/clients/{id}", detailsController::handle);

        app.get("/clients/{id}/edit", editController::showForm);
        app.post("/clients/{id}", editController::submit);

        app.post("/clients/{id}/delete", deleteController::submit);

        app.start(8090);
        System.out.println("Open: http://localhost:8090");
    }
}
