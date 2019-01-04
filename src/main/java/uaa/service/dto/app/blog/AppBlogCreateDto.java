package uaa.service.dto.app.blog;

import java.util.List;

/**
 * Created by UKi_Hi on 2018/12/8.
 */
public class AppBlogCreateDto {
    private String title;
    private String content;
    private String token;
    private String sourceType;

    private List<String>  tagIds;

    private String permissionType;
    private String permissionVerify;
    private String imgUrlId;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public List<String> getTagIds() {
        return tagIds;
    }

    public void setTagIds(List<String> tagIds) {
        this.tagIds = tagIds;
    }

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

    public String getSourceType() {
        return sourceType;
    }

    public void setSourceType(String sourceType) {
        this.sourceType = sourceType;
    }

    public String getImgUrlId() {
        return imgUrlId;
    }

    public void setImgUrlId(String imgUrlId) {
        this.imgUrlId = imgUrlId;
    }
}
