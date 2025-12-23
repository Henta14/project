package views;

public class LayoutView {
    public static String page(String title, String body) {
        return """
        <!doctype html>
        <html lang="ru">
        <head>
          <meta charset="utf-8"/>
          <title>%s</title>
          <style>
            body{font-family:Arial,sans-serif;margin:24px}
            table{border-collapse:collapse;width:100%%}
            th,td{border:1px solid #ddd;padding:8px}
            th{background:#f5f5f5;text-align:left}
            .row{display:flex;gap:12px;align-items:center}
            a.btn{display:inline-block;padding:8px 12px;border:1px solid #333;border-radius:10px;text-decoration:none}
          </style>
        </head>
        <body>%s</body>
        </html>
        """.formatted(esc(title), body);
    }

    public static String esc(String s) {
        if (s == null) return "";
        return s.replace("&","&amp;").replace("<","&lt;").replace(">","&gt;");
    }
}
