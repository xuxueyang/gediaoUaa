package uaa.service.common.redis.dto;

public class RedisObjectDTO extends RedisKeyDTO {
    private Object value;
    private Long timeout;

    public RedisObjectDTO() {
    }

    public RedisObjectDTO(String tenantCode, String containerType, String containerId, String objectType, String objectId) {
        super(tenantCode, containerType, containerId, objectType, objectId);
    }

    public Object getValue() {
        return this.value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public Long getTimeout() {
        return this.timeout;
    }

    public void setTimeout(Long timeout) {
        this.timeout = timeout;
    }
}
