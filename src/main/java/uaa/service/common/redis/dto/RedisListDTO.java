package uaa.service.common.redis.dto;

import java.util.List;

public class RedisListDTO extends RedisKeyDTO {
    private List<String> value;
    private Long timeout;

    public RedisListDTO() {
    }

    public RedisListDTO(String tenantCode, String containerType, String containerId, String objectType, String objectId) {
        super(tenantCode, containerType, containerId, objectType, objectId);
    }

    public List<String> getValue() {
        return this.value;
    }

    public void setValue(List<String> value) {
        this.value = value;
    }

    public Long getTimeout() {
        return this.timeout;
    }

    public void setTimeout(Long timeout) {
        this.timeout = timeout;
    }
}
