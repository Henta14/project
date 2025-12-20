package repos.db;

import models.ClientShort;
import java.util.List;

public interface ClientDbQueryRepository {
    List<ClientShort> getKthNShortList(int k, int n, ClientFilter filter, ClientSort sort);
    int getCount(ClientFilter filter);
}
