package uaa.config.learns;

import com.alibaba.fastjson.JSON;
import com.github.benmanes.caffeine.cache.CacheLoader;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.checkerframework.checker.nullness.qual.*;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;

/**
 * @author xuxy
 * @date 2020/12/25 上午10:28
 */
@Slf4j
@Configuration
public class CacheConfig {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Bean("studentCache")
    public LoadingCache<Integer, Object> studentCache() {
        int k =0;
        ValueOperations<String, String> operations = stringRedisTemplate.opsForValue();
        operations.set("a", "b");
        int d = 0;

        return Caffeine.newBuilder()
            .maximumSize(10).recordStats()
            .expireAfterWrite(1, TimeUnit.HOURS)
            //                  .scheduler(Scheduler.systemScheduler())  // 需要自定义调度器，用定时任务去主动提前刷新
            .build(new CacheLoader<Integer, Object>() {
                @Nullable
                @Override
                public Object load(@NonNull Integer key) throws Exception {
//                    log.info("从缓存中加载...key={}", key);
                    HashOperations<String, String, String> hashOperations = stringRedisTemplate.opsForHash();
                    String value = hashOperations.get("STU_HS", String.valueOf(key));
                    if (StringUtils.isEmpty(value)) {
                        return null;
                    }
                    return JSON.parseObject(value, Object.class);
                }
            });
    }
}
