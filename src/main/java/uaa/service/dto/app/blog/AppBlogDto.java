package uaa.service.dto.app.blog;

import io.swagger.annotations.ApiModelProperty;
import uaa.service.dto.app.log.AppLogTagDTO;

import java.time.ZonedDateTime;
import java.util.List;

/**
 * Created by UKi_Hi on 2018/12/9.
 */
public class AppBlogDto {
    @ApiModelProperty(name = "创建时间")
    private ZonedDateTime createdDate;

    @ApiModelProperty(name = "更新时间")
    private ZonedDateTime updatedDate;

    @ApiModelProperty(name = "内容")
    private String content;

    @ApiModelProperty(name = "评论")
    private List<AppBlogCommentDto> commentDtos;

    @ApiModelProperty(name = "阅读数目")
    private int readCount;

    @ApiModelProperty(name = "作者名字")
    private String authorName;
    @ApiModelProperty(name = "作者ID")
    private String authorId;

    @ApiModelProperty(name = "博客ID")
    private String id;

    @ApiModelProperty(name = "标题")
    private String title;

    @ApiModelProperty(name = "标签信息")
    private List<AppLogTagDTO> tagList;

    @ApiModelProperty(name = "权限")
    private String permissionType;

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getAuthorId() {
        return authorId;
    }

    public void setAuthorId(String authorId) {
        this.authorId = authorId;
    }

    public ZonedDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(ZonedDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public ZonedDateTime getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(ZonedDateTime updatedDate) {
        this.updatedDate = updatedDate;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<AppBlogCommentDto> getCommentDtos() {
        return commentDtos;
    }

    public void setCommentDtos(List<AppBlogCommentDto> commentDtos) {
        this.commentDtos = commentDtos;
    }

    public int getReadCount() {
        return readCount;
    }

    public void setReadCount(int readCount) {
        this.readCount = readCount;
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

    public String getPermissionType() {
        return permissionType;
    }

    public void setPermissionType(String permissionType) {
        this.permissionType = permissionType;
    }

    public List<AppLogTagDTO> getTagList() {
        return tagList;
    }

    public void setTagList(List<AppLogTagDTO> tagList) {
        this.tagList = tagList;
    }
}
