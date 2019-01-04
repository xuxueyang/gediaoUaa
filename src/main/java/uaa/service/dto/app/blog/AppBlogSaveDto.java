package uaa.service.dto.app.blog;

import uaa.service.dto.app.log.AppLogTagDTO;

import java.util.List;

/**
 * Created by UKi_Hi on 2018/12/16.
 */
public class AppBlogSaveDto {

    private String id;
    private String title;
    private String content;
    private String token;
    private String permissionType;
    private List<AppLogTagDTO> tagList;
    private String permissionVerify;
    private String imgUrlId;

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

    public List<AppLogTagDTO> getTagList() {
        return tagList;
    }

    public void setTagList(List<AppLogTagDTO> tagList) {
        this.tagList = tagList;
    }


    public String getImgUrlId() {
        return imgUrlId;
    }

    public void setImgUrlId(String imgUrlId) {
        this.imgUrlId = imgUrlId;
    }
}
