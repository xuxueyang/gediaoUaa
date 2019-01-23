package uaa.web.rest.util.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import uaa.domain.app.log.AppLogEach;

import java.util.HashSet;
import java.util.Set;

@Service
public class RedisService {

//    @Autowired
//    private RedisTemplate<String, Object> redisTemplate;

    private Set<AppLogEach> users = new HashSet<AppLogEach>();


    @CachePut(value = "user", key = "'User:'+#user.id")
    public AppLogEach addUser(AppLogEach user) {
        users.add(user);
        return user;
    }

    @Cacheable(value = "user", key = "'User:'+#id")
    public AppLogEach addUser(String id, String name, int age) {
        AppLogEach user = new AppLogEach();
        users.add(user);
        return user;
    }

    @Cacheable(value = "user", key = "'User:'+#id")
    public AppLogEach getStudent(String id) {
        System.out.println("not in redis cache");
        for (AppLogEach user : users) {
            if (user.getId().equals(id)) {
                return user;
            }
        }
        return null;
    }

//    @CachePut(value = "city", key = "'City:'+#city.id")
//    public City addCity(City city) {
//        cities.add(city);
//        return city;
//    }
}
//---------------------
//    作者：abombhz
//    来源：CSDN
//    原文：https://blog.csdn.net/abombhz/article/details/78123253
//    版权声明：本文为博主原创文章，转载请附上博文链接！
