package uaa.service.common.redis.dto;

import java.util.Map;

public class RedisHashDTO extends RedisKeyDTO {
    private Map<String, String> value;
    private Long timeout;

    public Map<String, String> getValue() {
        return this.value;
    }

    public void setValue(Map<String, String> value) {
        this.value = value;
    }

    public RedisHashDTO() {
    }

    public RedisHashDTO(String tenantCode, String containerType, String containerId, String objectType, String objectId) {
        super(tenantCode, containerType, containerId, objectType, objectId);
    }

    public Long getTimeout() {
        return this.timeout;
    }

    public void setTimeout(Long timeout) {
        this.timeout = timeout;
    }
}
