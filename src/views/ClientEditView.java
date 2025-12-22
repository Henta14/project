package views;

import models.Client;

import java.util.List;

public class ClientEditView {

    public static String render(Client c, List<String> errors) {
        if (c == null) {
            return LayoutView.page("Редактирование", "<h1>Клиент не найден</h1><p><a class='btn' href='/'>Назад</a></p>");
        }

        StringBuilder sb = new StringBuilder();
        sb.append("<h1>Редактировать клиента</h1>");

        if (errors != null && !errors.isEmpty()) {
            sb.append("<div style='border:1px solid #c00;padding:10px;border-radius:10px'>")
                    .append("<b>Ошибки:</b><ul>");
            for (String e : errors) sb.append("<li>").append(LayoutView.esc(e)).append("</li>");
            sb.append("</ul></div><br/>");
        }

        sb.append("""
            <form method="post" action="/clients/%d">
              <p><label>Название<br><input name="name" value="%s" style="width:420px"></label></p>
              <p><label>Адрес<br><input name="address" value="%s" style="width:420px"></label></p>
              <p><label>Телефон<br><input name="phone" value="%s" style="width:420px"></label></p>
              <p><label>Email<br><input name="email" value="%s" style="width:420px"></label></p>
              <p><label>Контактное лицо<br><input name="contactPerson" value="%s" style="width:420px"></label></p>
              <p><label>ИНН<br><input name="taxId" value="%s" style="width:420px"></label></p>
              <p><label>Рег. номер<br><input name="registrationNumber" value="%s" style="width:420px"></label></p>

              <p class="row">
                <button type="submit">Сохранить</button>
                <a class="btn" href="/" onclick="if(window.opener){window.close(); return false;}">Отмена</a>
              </p>
            </form>
        """.formatted(
                c.getClientId(),
                LayoutView.esc(nvl(c.getName())),
                LayoutView.esc(nvl(c.getAddress())),
                LayoutView.esc(nvl(c.getPhone())),
                LayoutView.esc(nvl(c.getEmail())),
                LayoutView.esc(nvl(c.getContactPerson())),
                LayoutView.esc(nvl(c.getTaxId())),
                LayoutView.esc(nvl(c.getRegistrationNumber()))
        ));

        return LayoutView.page("Редактирование клиента", sb.toString());
    }

    private static String nvl(String s) { return s == null ? "" : s; }
}
