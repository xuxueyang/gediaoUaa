package uaa.web.rest.util.redis;


import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface CachePut {

    /**
     * Alias for {@link #cacheNames}.
     */
    @AliasFor("cacheNames")
    String[] value() default {};

    @AliasFor("cacheNames")
    String key() default "";

//    ......省略其他
}
//    直接使用RedisAutoConfiguration加载properties文件的配置 RedisTemplate泛型不能使用具体的类型。
//    服务类中用了缓存标签@CachePut和@Cacheable，其中 value字段的标签是@AliasFor(“cacheNames”) ，在后面的RedisCacheManager 中利用cacheName对不同的类型设置不同的过期时间。
//    ---------------------
//    作者：abombhz
//    来源：CSDN
//    原文：https://blog.csdn.net/abombhz/article/details/78123253
//    版权声明：本文为博主原创文章，转载请附上博文链接！
