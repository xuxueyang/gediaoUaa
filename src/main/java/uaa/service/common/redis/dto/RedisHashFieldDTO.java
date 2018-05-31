package uaa.service.common.redis.dto;


public class RedisHashFieldDTO extends RedisKeyDTO {
    private String hashKey;
    private String fieldValue;

    public RedisHashFieldDTO() {
    }

    public RedisHashFieldDTO(String tenantCode, String containerType, String containerId, String objectType, String objectId) {
        super(tenantCode, containerType, containerId, objectType, objectId);
    }

    public String getHashKey() {
        return this.hashKey;
    }

    public void setHashKey(String hashKey) {
        this.hashKey = hashKey;
    }

    public String getFieldValue() {
        return this.fieldValue;
    }

    public void setFieldValue(String fieldValue) {
        this.fieldValue = fieldValue;
    }
}
