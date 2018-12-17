package uaa.domain.app.blog;


import org.checkerframework.checker.units.qual.C;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import uaa.domain.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "app_blog_blog")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class AppBlogBlog extends BaseEntity implements Serializable {
    @Id
    @Column(name = "ID")
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;


    @Column(name = "TITLE")
    private String title;


    @Column(name = "CONTENT")
    private String content;

    @Column(name = "PERMISSION_TYPE")
    private String permissionType;


    @Column(name = "PERMISSION_VERIFY")
    private String permissionVerify;

    @Column(name = "STATUS")
    private String status;

    @Column(name = "CREATE_ID")
    private String createId;

    @Column(name = "UPDATE_ID")
    private String updateId;

    @Column(name="READ_COUNT")
    private int readCount;
    @Column(name = "SOURCE_TYPE")
    private String sourceType;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreateId() {
        return createId;
    }

    public void setCreateId(String createId) {
        this.createId = createId;
    }

    public String getUpdateId() {
        return updateId;
    }

    public void setUpdateId(String updateId) {
        this.updateId = updateId;
    }

    public int getReadCount() {
        return readCount;
    }

    public void setReadCount(int readCount) {
        this.readCount = readCount;
    }

    public String getSourceType() {
        return sourceType;
    }

    public void setSourceType(String sourceType) {
        this.sourceType = sourceType;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
