package uaa.repository.es;


import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.xcontent.XContentBuilder;

public interface ElasticsearchCrudRepository<T, ID extends Serializable> {
    boolean createIndex(String var1);

    boolean createIndex(String var1, Settings var2);

    boolean createMapping(String var1, String var2, XContentBuilder var3);

    boolean updateMapping(String var1, String var2, XContentBuilder var3);

    boolean deleteIndex(String var1);

    boolean indexExist(String var1);

    String insertData(String var1, String var2, String var3, T var4);

    String insertDataWithParent(String var1, String var2, String var3, T var4, String var5, String var6, String var7);

    String updateData(String var1, String var2, String var3, T var4) throws ExecutionException, InterruptedException;

    String updateDataWithParent(String var1, String var2, String var3, T var4, String var5, String var6, String var7) throws ExecutionException, InterruptedException;

    String upsertData(String var1, String var2, String var3, T var4) throws ExecutionException, InterruptedException;

    String upsertDataWithParent(String var1, String var2, String var3, T var4, String var5, String var6, String var7) throws ExecutionException, InterruptedException;

    String deleteData(String var1, String var2, String var3);

    String deleteDataWithParent(String var1, String var2, String var3, String var4, String var5, String var6);

    String bulkInsertData(String var1, String var2, Map<String, T> var3);

    String bulkUpdateData(String var1, String var2, Map<String, T> var3);

    String bulkDeleteData(String var1, String var2, List<String> var3);

    void indexRefresh(String... var1);

    void indexRefreshByCode(String... var1);

    String prepareIndex(String var1);

    String prepareSearchId(String var1, String var2, String var3);
}
