package uaa.repository.es;

import java.io.Serializable;
import java.util.List;

public interface ElasticsearchRepository<T, ID extends Serializable> extends ElasticsearchCrudRepository<T, ID> {
    T getData(String var1, String var2, String var3, Class<T> var4);

    List<T> search(List<EsSearchDTO> var1, Class<T> var2);
}
