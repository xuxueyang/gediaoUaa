package uaa.web.rest.util.EhCache;

import uaa.domain.User;

public interface EhcacheService {

    // 测试失效情况，有效期为5秒
    public String getTimestamp(String param);

    public String getDataFromDB(String key);

    public void removeDataAtDB(String key);

    public String refreshData(String key);


    public User findById(String userId);

    public boolean isReserved(String userId);

    public void removeUser(String userId);

    public void removeAllUser();
}
//---------------------
//    作者：vbirdbest
//    来源：CSDN
//    原文：https://blog.csdn.net/vbirdbest/article/details/72763048
//    版权声明：本文为博主原创文章，转载请附上博文链接！
