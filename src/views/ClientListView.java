package views;

import models.ClientShort;

import java.util.List;

public class ClientListView {

    public static String render(List<ClientShort> items) {
        StringBuilder sb = new StringBuilder();

        sb.append("<div class='row'>")
                .append("<h1 style='margin:0'>Клиенты</h1>")
                .append("</div><br/>");

        sb.append("<table><thead><tr>")
                .append("<th>ID</th><th>Название</th><th>Контакт</th><th>Полная инфа</th>")
                .append("</tr></thead><tbody>");

        if (items != null) {
            for (ClientShort c : items) {
                sb.append("<tr>")
                        .append("<td>").append(c.getClientId()).append("</td>")
                        .append("<td>").append(LayoutView.esc(c.getName())).append("</td>")
                        .append("<td>").append(LayoutView.esc(c.getContactPerson())).append("</td>")
                        .append("<td><a class='btn' target='_blank' href='/clients/")
                        .append(c.getClientId())
                        .append("'>Открыть</a></td>")
                        .append("</tr>");
            }
        }

        sb.append("</tbody></table>");

        return LayoutView.page("Клиенты", sb.toString());
    }
}
