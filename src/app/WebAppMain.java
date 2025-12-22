package app;

import controllers.ClientDetailsController;
import controllers.ClientListController;
import db.DbManager;
import io.javalin.Javalin;
import observer.ObservableClientRepository;
import repos.ClientRepDb;
import repos.ClientRepDbAdapter;
import repos.ClientRepository;

public class WebAppMain {

    public static void main(String[] args) {

        // TODO: подставь свой пароль/юзер/URL (те, что уже работали в тестах)
        DbManager.initIfNeeded(
                "jdbc:postgresql://localhost:5433/lab_db",
                "postgres",
                "postgres"
        );

        ClientRepository repo = new ClientRepDbAdapter(new ClientRepDb(DbManager.getInstance()));
        ObservableClientRepository observableRepo = new ObservableClientRepository(repo);

        ClientListController listController = new ClientListController(observableRepo);
        ClientDetailsController detailsController = new ClientDetailsController(observableRepo);

        Javalin app = Javalin.create(cfg -> {
            cfg.http.defaultContentType = "text/html; charset=utf-8";
        });

        app.get("/", listController::handle);
        app.get("/clients/{id}", detailsController::handle);

        app.start(8090);
        System.out.println("Open: http://localhost:8090");
    }
}
