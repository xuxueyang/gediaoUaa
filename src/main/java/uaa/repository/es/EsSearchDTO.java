package uaa.repository.es;

public class EsSearchDTO {
    private String tenantCode;
    private String objectType;
    private String objectId;

    public EsSearchDTO() {
    }

    public EsSearchDTO(String tenantCode, String objectType, String objectId) {
        this.tenantCode = tenantCode;
        this.objectType = objectType;
        this.objectId = objectId;
    }

    public String getTenantCode() {
        return this.tenantCode;
    }

    public void setTenantCode(String tenantCode) {
        this.tenantCode = tenantCode;
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
}
