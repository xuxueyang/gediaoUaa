package uaa.domain.app.log;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import uaa.domain.BaseEntity;
import uaa.domain.uaa.UaaUser;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "app_log_each")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class AppLogEach extends BaseEntity implements Serializable {

    @Id
    @Column(name = "ID")
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    @Column(name = "TOMATO_TYPE")
    private String tomatoType;

    @Column(name = "CREATED_ID")
    private String createdId;

    @Column(name = "UPDATED_ID")
    private String updatedID;

    @Column(name = "ENCODE")
    private String encode;

    @Column(name = "BELONG_DATE")
    private String belongDate;

    @Column(name = "MESSAGE")
    private String message;

    @Column(name = "TITLE")
    private String title;

    @Column(name = "STATUS")
    private String status;

    @Column(name = "TYPE")
    private String type;

    @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
    @JoinColumn(name = "BASE_ID", referencedColumnName = "ID", insertable = false, updatable = false)
    @org.hibernate.annotations.OrderBy(clause = "UPDATED_DATE")
    private Set<AppLogDetail> details = new HashSet<>();
    //
//    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
//    @JoinColumn(name = "CREATED_ID", referencedColumnName = "ID", insertable = false, updatable = false)
//    private UaaUser uaaUser;
    @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
    @JoinColumn(name = "BASE_ID", referencedColumnName = "ID", insertable = false, updatable = false)
    @org.hibernate.annotations.OrderBy(clause = "UPDATED_DATE")
    private Set<AppLogEachTag> tags = new HashSet<>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTomatoType() {
        return tomatoType;
    }

    public void setTomatoType(String tomatoType) {
        this.tomatoType = tomatoType;
    }

    public String getCreatedId() {
        return createdId;
    }

    public void setCreatedId(String createdId) {
        this.createdId = createdId;
    }

    public String getUpdatedID() {
        return updatedID;
    }

    public void setUpdatedID(String updatedID) {
        this.updatedID = updatedID;
    }

    public String getEncode() {
        return encode;
    }

    public void setEncode(String encode) {
        this.encode = encode;
    }

    public String getBelongDate() {
        return belongDate;
    }

    public void setBelongDate(String belongDate) {
        this.belongDate = belongDate;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Set<AppLogDetail> getDetails() {
        return details;
    }

    public void setDetails(Set<AppLogDetail> details) {
        this.details = details;
    }

    public Set<AppLogEachTag> getTags() {
        return tags;
    }

    public void setTags(Set<AppLogEachTag> tags) {
        this.tags = tags;
    }

//    public UaaUser getUaaUser() {
//        return uaaUser;
//    }
//
//    public void setUaaUser(UaaUser uaaUser) {
//        this.uaaUser = uaaUser;
//    }
}
