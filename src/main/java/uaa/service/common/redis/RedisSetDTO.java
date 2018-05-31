package uaa.service.common.redis;

import uaa.service.common.redis.dto.RedisKeyDTO;

import java.util.Set;

public class RedisSetDTO extends RedisKeyDTO {
    private Set<String> value;
    private Long timeout;

    public RedisSetDTO() {
    }

    public RedisSetDTO(String tenantCode, String containerType, String containerId, String objectType, String objectId) {
        super(tenantCode, containerType, containerId, objectType, objectId);
    }

    public Set<String> getValue() {
        return this.value;
    }

    public void setValue(Set<String> value) {
        this.value = value;
    }

    public Long getTimeout() {
        return this.timeout;
    }

    public void setTimeout(Long timeout) {
        this.timeout = timeout;
    }
}
