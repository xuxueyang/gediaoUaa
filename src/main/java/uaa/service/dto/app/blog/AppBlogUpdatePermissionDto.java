package uaa.service.dto.app.blog;

public class AppBlogUpdatePermissionDto {

    private String id;
    private String token;
    private String permissionType;
    private String permissionVerify;

    public String getPermissionType() {
        return permissionType;
    }

    public void setPermissionType(String permissionType) {
        this.permissionType = permissionType;
    }

    public String getPermissionVerify() {
        return permissionVerify;
    }

    public void setPermissionVerify(String permissionVerify) {
        this.permissionVerify = permissionVerify;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
