package views;

public class ErrorView {
    public static String render(String title, String message) {
        String body = "<h1>" + LayoutView.esc(title) + "</h1><pre>" + LayoutView.esc(message) + "</pre>";
        return LayoutView.page(title, body);
    }
}
