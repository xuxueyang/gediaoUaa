package uaa.service.common.redis;

import uaa.service.common.redis.dto.RedisKeyDTO;

public class RedisValueDTO extends RedisKeyDTO {
    private String value;

    public RedisValueDTO() {
    }

    public RedisValueDTO(String tenantCode, String containerType, String containerId, String objectType, String objectId) {
        super(tenantCode, containerType, containerId, objectType, objectId);
    }

    public String getValue() {
        return this.value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
