package views;

import models.ClientShort;

import java.util.List;

public class ClientListView {

    public static String render(List<ClientShort> items, String q, String sort) {
        StringBuilder sb = new StringBuilder();

        String qq = q == null ? "" : q;
        String ss = sort == null ? "name_asc" : sort;

        sb.append("<div class='row'>")
                .append("<h1 style='margin:0'>Клиенты</h1>")
                .append("<a class='btn' target='_blank' href='/clients/new'>+ Добавить</a>")
                .append("</div><br/>");

        sb.append("""
            <form method="get" action="/" class="row" style="gap:10px;align-items:center">
              <input name="q" placeholder="Поиск по названию..." value="%s" style="width:260px">

              <select name="sort">
                <option value="name_asc" %s>Название A→Z</option>
                <option value="name_desc" %s>Название Z→A</option>
                <option value="id_asc" %s>ID ↑</option>
                <option value="id_desc" %s>ID ↓</option>
              </select>

              <button type="submit">Применить</button>
              <a class="btn" href="/">Сброс</a>
            </form>
            <br/>
        """.formatted(
                LayoutView.esc(qq),
                "name_asc".equals(ss) ? "selected" : "",
                "name_desc".equals(ss) ? "selected" : "",
                "id_asc".equals(ss) ? "selected" : "",
                "id_desc".equals(ss) ? "selected" : ""
        ));

        sb.append("<table border='1' cellspacing='0' cellpadding='8' style='border-collapse:collapse;width:100%'>");
        sb.append("<tr>")
                .append("<th>ID</th>")
                .append("<th>Название</th>")
                .append("<th>Контакт</th>")
                .append("<th>Подробнее</th>")
                .append("</tr>");

        if (items == null || items.isEmpty()) {
            sb.append("<tr><td colspan='4'>Нет данных</td></tr>");
        } else {
            for (ClientShort cs : items) {
                sb.append("<tr>")
                        .append("<td>").append(cs.getClientId()).append("</td>")
                        .append("<td>").append(LayoutView.esc(cs.getName())).append("</td>")
                        .append("<td>").append(LayoutView.esc(cs.getContactPerson())).append("</td>")
                        .append("<td>")
                        .append("<a class='btn' target='_blank' href='/clients/")
                        .append(cs.getClientId())
                        .append("'>Открыть</a>")
                        .append("</td>")
                        .append("</tr>");
            }
        }

        sb.append("</table>");

        return LayoutView.page("Клиенты", sb.toString());
    }
}
