package validation;

import java.util.ArrayList;
import java.util.List;

public class ClientValidator {

    public static List<String> validate(
            String name, String phone, String email, String taxId
    ) {
        List<String> errors = new ArrayList<>();

        if (isBlank(name)) errors.add("Название обязательно.");
        if (isBlank(phone)) errors.add("Телефон обязателен.");
        else if (!phone.matches("^\\+?\\d{10,15}$")) errors.add("Телефон должен быть в формате +79990000000 (10–15 цифр).");

        if (!isBlank(email) && !email.contains("@")) errors.add("Email некорректный.");
        if (isBlank(taxId)) errors.add("ИНН обязателен.");
        else if (!taxId.matches("^\\d{10,12}$")) errors.add("ИНН должен быть 10–12 цифр.");

        return errors;
    }

    private static boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }
}
