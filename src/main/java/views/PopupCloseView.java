package views;

public class PopupCloseView {
    public static String render(String message) {
        String body = """
            <h2>%s</h2>
            <p>Окно закроется автоматически…</p>
            <script>
              if (window.opener) {
                window.opener.location.reload();
                window.close();
              } else {
                window.location.href = "/";
              }
            </script>
        """.formatted(LayoutView.esc(message));
        return LayoutView.page("Готово", body);
    }
}
