package uaa.service.dto.app.blog;

import io.swagger.annotations.ApiModelProperty;

import java.time.ZonedDateTime;

/**
 * Created by xuxy on 2019/1/31.
 */
public class AppBlogCategoryDto {
    private String id;
    @ApiModelProperty(name = "分类名")
    private String name;

    @ApiModelProperty(name = "封面图")
    private String imgUrl;

    //点击数目、创建时间、最新更新时间、下属的博客数目、创建者昵称、点赞数、阅览数、收藏数、评论数

    private ZonedDateTime createdDate;
    private ZonedDateTime updatedDate;

    private int blogCount;

    private String createdNickName;
    private int lookCount;
    private int praiseCount;

    private int collectCount;
    private int commentCount;
    private String token;

    public String getCreatedNickName() {
        return createdNickName;
    }

    public void setCreatedNickName(String createdNickName) {
        this.createdNickName = createdNickName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public int getLookCount() {
        return lookCount;
    }

    public void setLookCount(int lookCount) {
        this.lookCount = lookCount;
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

    public int getBlogCount() {
        return blogCount;
    }

    public void setBlogCount(int blogCount) {
        this.blogCount = blogCount;
    }



    public int getPraiseCount() {
        return praiseCount;
    }

    public void setPraiseCount(int praiseCount) {
        this.praiseCount = praiseCount;
    }

    public int getCollectCount() {
        return collectCount;
    }

    public void setCollectCount(int collectCount) {
        this.collectCount = collectCount;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
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
