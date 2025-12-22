package views;

import models.Client;

public class ClientDetailsView {

    public static String render(Client c) {
        if (c == null) {
            return LayoutView.page("Клиент", "<h1>Не найден</h1><p><a class='btn' href='/'>Назад</a></p>");
        }

        String body = """
        <h1>%s</h1>
        <p><b>ID:</b> %d</p>
        <p><b>Адрес:</b> %s</p>
        <p><b>Телефон:</b> %s</p>
        <p><b>Email:</b> %s</p>
        <p><b>Контакт:</b> %s</p>
        <p><b>ИНН:</b> %s</p>
        <p><b>Рег. номер:</b> %s</p>
        <p><a class='btn' href='/'>Назад</a></p>
        """.formatted(
                LayoutView.esc(c.getName()),
                c.getClientId(),
                LayoutView.esc(c.getAddress()),
                LayoutView.esc(c.getPhone()),
                LayoutView.esc(c.getEmail()),
                LayoutView.esc(c.getContactPerson()),
                LayoutView.esc(c.getTaxId()),
                LayoutView.esc(c.getRegistrationNumber())
        );

        return LayoutView.page("Клиент", body);
    }
}
