package repos;

import models.Client;
import models.ClientShort;

import java.util.List;

public interface ClientRepository {

    // a) Чтение всех
    List<Client> readAll();

    // b) Запись всех
    void writeAll(List<Client> items);

    // c) Получить по ID
    Client getById(int id);

    // d) k элементов на странице n
    List<ClientShort> getKthNShortList(int k, int n);

    // e) Сортировать по выбранному полю (у нас: name)
    void sortByName();

    // f) Добавить (генерим новый ID)
    Client add(Client item);

    // g) Заменить по ID
    boolean replaceById(int id, Client newValue);

    // h) Удалить по ID
    boolean deleteById(int id);

    // i) Количество элементов
    int getCount();
}
