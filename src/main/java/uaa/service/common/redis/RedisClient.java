package uaa.service.common.redis;


import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.redis.core.*;
import org.springframework.stereotype.Component;
import uaa.service.common.redis.dto.*;
import com.alibaba.fastjson.JSON;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

//@Component
public class RedisClient {
//    @Autowired
//    private HashOperations<String, String, String> hashOperations;
//    @Autowired
//    private ValueOperations<String, String> valueOperations;
//    @Autowired
//    private ListOperations<String, String> listOperations;
//    @Autowired
//    private SetOperations<String, String> setOperations;
//    @Autowired
//    private StringRedisTemplate redisTemplate;
//
//    public RedisClient() {
//    }
//
//    public void setObject(RedisObjectDTO redisObjectDTO) {
//        String key = redisObjectDTO.getKey();
//        Object value = redisObjectDTO.getValue();
//        String strValue = null;
//        if (value instanceof String) {
//            strValue = (String)value;
//        } else {
//            strValue = JSON.toJSONString(value);
//        }
//
//        if (redisObjectDTO.getTimeout() != null) {
//            this.valueOperations.set(key, strValue, redisObjectDTO.getTimeout().longValue(), TimeUnit.SECONDS);
//        } else {
//            this.valueOperations.set(key, strValue);
//        }
//
//    }
//
//    public String getObject(RedisObjectDTO redisObjectDTO) {
//        String key = redisObjectDTO.getKey();
//        return (String)this.valueOperations.get(key);
//    }
//
//    public Long increment(RedisObjectDTO redisObjectDTO) {
//        String key = redisObjectDTO.getKey();
//        return this.valueOperations.increment(key, Long.valueOf(redisObjectDTO.getValue().toString()).longValue());
//    }
//
//    public void deleteKey(RedisKeyDTO redisKeyDTO) {
//        String key = redisKeyDTO.getKey();
//        this.redisTemplate.delete(key);
//    }
//
//    public Long getExpire(RedisKeyDTO redisKeyDTO) {
//        String key = redisKeyDTO.getKey();
//        return this.redisTemplate.getExpire(key, TimeUnit.SECONDS);
//    }
//
//    public void expireKey(RedisExpireDTO redisExpireDTO) {
//        String key = redisExpireDTO.getKey();
//        this.redisTemplate.expire(key, redisExpireDTO.getTimeout().longValue(), TimeUnit.SECONDS);
//    }
//
//    public void setHashAll(RedisHashDTO redisHashDTO) {
//        String key = redisHashDTO.getKey();
//        Map<String, String> value = redisHashDTO.getValue();
//        this.hashOperations.putAll(key, value);
//        if (redisHashDTO.getTimeout() != null) {
//            this.redisTemplate.expire(key, redisHashDTO.getTimeout().longValue(), TimeUnit.SECONDS);
//        }
//
//    }
//
//    public Map<String, String> getHashAll(RedisHashDTO redisHashDTO) {
//        String key = redisHashDTO.getKey();
//        return this.hashOperations.entries(key);
//    }
//
//    public Set<String> getHashKeys(RedisHashDTO redisHashDTO) {
//        String key = redisHashDTO.getKey();
//        return this.hashOperations.keys(key);
//    }
//
//    public List<String> getHashValues(RedisHashDTO redisHashDTO) {
//        String key = redisHashDTO.getKey();
//        return this.hashOperations.values(key);
//    }
//
//    public boolean hashHasKey(RedisHashFieldDTO redisHashFieldDTO) {
//        String key = redisHashFieldDTO.getKey();
//        return this.hashOperations.hasKey(key, redisHashFieldDTO.getHashKey()).booleanValue();
//    }
//
//    public void setHashField(RedisHashFieldDTO redisHashFieldDTO) {
//        String key = redisHashFieldDTO.getKey();
//        this.hashOperations.put(key, redisHashFieldDTO.getHashKey(), redisHashFieldDTO.getFieldValue());
//    }
//
//    public String getHashField(RedisHashFieldDTO redisHashFieldDTO) {
//        String key = redisHashFieldDTO.getKey();
//        return (String)this.hashOperations.get(key, redisHashFieldDTO.getHashKey());
//    }
//
//    public void deleteHashField(RedisHashFieldDTO redisHashFieldDTO) {
//        String key = redisHashFieldDTO.getKey();
//        this.hashOperations.delete(key, new Object[]{redisHashFieldDTO.getHashKey()});
//    }
//
//    public void setListAll(RedisListDTO redisListDTO) {
//        String key = redisListDTO.getKey();
//        this.listOperations.rightPushAll(key, redisListDTO.getValue());
//        if (redisListDTO.getTimeout() != null) {
//            this.redisTemplate.expire(key, redisListDTO.getTimeout().longValue(), TimeUnit.SECONDS);
//        }
//
//    }
//
//    public void setListValue(RedisValueDTO redisValueDTO) {
//        String key = redisValueDTO.getKey();
//        this.listOperations.rightPush(key, redisValueDTO.getValue());
//    }
//
//    public List<String> getListAll(RedisListDTO redisListDTO) {
//        String key = redisListDTO.getKey();
//        return this.listOperations.range(key, 0L, -1L);
//    }
//
//    public void setSetAll(RedisSetDTO redisSetDTO) {
//        String key = redisSetDTO.getKey();
//        this.setOperations.add(key, redisSetDTO.getValue().toArray(new String[0]));
//        if (redisSetDTO.getTimeout() != null) {
//            this.redisTemplate.expire(key, redisSetDTO.getTimeout().longValue(), TimeUnit.SECONDS);
//        }
//
//    }
//
//    public void setSetValue(RedisValueDTO redisValueDTO) {
//        String key = redisValueDTO.getKey();
//        this.setOperations.add(key, new String[]{redisValueDTO.getValue()});
//    }
//
//    public Set<String> getSetAll(RedisSetDTO redisSetDTO) {
//        String key = redisSetDTO.getKey();
//        return this.setOperations.members(key);
//    }
//
//    public void removeSetValue(RedisValueDTO redisValueDTO) {
//        String key = redisValueDTO.getKey();
//        this.setOperations.remove(key, new Object[]{redisValueDTO.getValue()});
//    }
}
