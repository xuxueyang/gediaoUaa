package uaa.service.util;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;

public class DBObjectUtil {
//
//    /**
//     * 把实体bean对象转换成DBObject
//     * 
//     * @param bean
//     * @return
//     * @throws IllegalArgumentException
//     * @throws IllegalAccessException
//     */
//    public static <T> DBObject bean2DBObject(T bean) {
//        if (bean == null) {
//            return null;
//        }
//        DBObject dbObject = new BasicDBObject();
//        String json = JsonUtils.objectToJson(bean);
//        dbObject = (DBObject) JSON.parse(json);
//        return dbObject;
//    }
//
//    /**
//     * 把DBObject转换成bean对象
//     * 
//     * @param dbObject
//     * @param bean
//     * @return
//     * @throws IllegalAccessException
//     * @throws InvocationTargetException
//     * @throws NoSuchMethodException
//     */
//    @SuppressWarnings("unchecked")
//    public static <T> T dbObject2Bean(DBObject dbObject, T bean) {
//        if (bean == null) {
//            return null;
//        }
//        String json = JSON.serialize(dbObject);
//        bean = (T) JsonUtils.jsonToBean(json, bean.getClass());
//        return bean;
//    }
}
//---------------------
//    作者：何何何何何
//    来源：CSDN
//    原文：https://blog.csdn.net/hjcenry/article/details/50545676
//    版权声明：本文为博主原创文章，转载请附上博文链接！
