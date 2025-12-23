package views;

import java.util.List;

public class ClientFormView {

    public static String render(
            String title,
            String action,
            String submitText,
            String name,
            String address,
            String phone,
            String email,
            String contactPerson,
            String taxId,
            String registrationNumber,
            List<String> errors
    ) {
        StringBuilder sb = new StringBuilder();
        sb.append("<h1>").append(LayoutView.esc(title)).append("</h1>");

        if (errors != null && !errors.isEmpty()) {
            sb.append("<div style='border:1px solid #c00;padding:10px;border-radius:10px'>")
                    .append("<b>Ошибки:</b><ul>");
            for (String e : errors) sb.append("<li>").append(LayoutView.esc(e)).append("</li>");
            sb.append("</ul></div><br/>");
        }

        sb.append("""
            <form method="post" action="%s">
              <p><label>Название<br><input name="name" value="%s" style="width:420px"></label></p>
              <p><label>Адрес<br><input name="address" value="%s" style="width:420px"></label></p>
              <p><label>Телефон<br><input name="phone" value="%s" style="width:420px"></label></p>
              <p><label>Email<br><input name="email" value="%s" style="width:420px"></label></p>
              <p><label>Контактное лицо<br><input name="contactPerson" value="%s" style="width:420px"></label></p>
              <p><label>ИНН<br><input name="taxId" value="%s" style="width:420px"></label></p>
              <p><label>Рег. номер<br><input name="registrationNumber" value="%s" style="width:420px"></label></p>

              <p class="row">
                <button type="submit">%s</button>
                <a class="btn" href="/" onclick="if(window.opener){window.close(); return false;}">Отмена</a>
              </p>
            </form>
        """.formatted(
                LayoutView.esc(action),
                LayoutView.esc(nvl(name)),
                LayoutView.esc(nvl(address)),
                LayoutView.esc(nvl(phone)),
                LayoutView.esc(nvl(email)),
                LayoutView.esc(nvl(contactPerson)),
                LayoutView.esc(nvl(taxId)),
                LayoutView.esc(nvl(registrationNumber)),
                LayoutView.esc(submitText)
        ));

        return LayoutView.page(title, sb.toString());
    }

    private static String nvl(String s) {
        return s == null ? "" : s;
    }
}
