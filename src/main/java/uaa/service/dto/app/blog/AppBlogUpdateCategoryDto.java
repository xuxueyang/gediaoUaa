package uaa.service.dto.app.blog;

public class AppBlogUpdateCategoryDto {

    private String id;
    private String token;
    private String categoryId;
    private String permissionVerify;


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

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }
}
