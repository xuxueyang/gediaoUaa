package uaa.service.common.redis.dto;

import org.apache.commons.lang3.StringUtils;
import uaa.service.common.redis.util.RedisProperties;
import uaa.service.common.redis.util.SpringRedisUtils;

public class RedisKeyDTO {
    private static RedisProperties REDIS_PROPERTIES = (RedisProperties) SpringRedisUtils.getBean(RedisProperties.class);
    public static final String PROJECT_CODE;
    private String tenantCode;
    private String containerType;
    private String containerId;
    private String objectType;
    private String objectId;

    public RedisKeyDTO() {
    }

    public RedisKeyDTO(String tenantCode, String containerType, String containerId, String objectType, String objectId) {
        this.tenantCode = tenantCode;
        this.containerType = containerType;
        this.containerId = containerId;
        this.objectType = objectType;
        this.objectId = objectId;
    }

    public String getTenantCode() {
        return this.tenantCode;
    }

    public void setTenantCode(String tenantCode) {
        this.tenantCode = tenantCode;
    }

    public String getContainerType() {
        return this.containerType;
    }

    public void setContainerType(String containerType) {
        this.containerType = containerType;
    }

    public String getContainerId() {
        return this.containerId;
    }

    public void setContainerId(String containerId) {
        this.containerId = containerId;
    }

    public String getObjectType() {
        return this.objectType;
    }

    public void setObjectType(String objectType) {
        this.objectType = objectType;
    }

    public String getObjectId() {
        return this.objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public String getKey() {
        StringBuilder key = new StringBuilder();
        if (StringUtils.isNotBlank(PROJECT_CODE)) {
            key.append(":");
            key.append(PROJECT_CODE);
        }

        if (StringUtils.isNotBlank(this.tenantCode)) {
            key.append(":");
            key.append(this.tenantCode);
        }

        if (StringUtils.isNotBlank(this.containerType)) {
            key.append(":");
            key.append(this.containerType);
        }

        if (StringUtils.isNotBlank(this.containerId)) {
            key.append(":");
            key.append(this.containerId);
        }

        if (StringUtils.isNotBlank(this.objectType)) {
            key.append(":");
            key.append(this.objectType);
        }

        if (StringUtils.isNotBlank(this.objectId)) {
            key.append(":");
            key.append(this.objectId);
        }

        return key != null && key.length() > 0 ? key.deleteCharAt(0).toString() : null;
    }

    static {
        PROJECT_CODE = REDIS_PROPERTIES.getProjectCode();
    }
}
