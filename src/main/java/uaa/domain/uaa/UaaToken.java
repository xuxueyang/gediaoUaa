package uaa.domain.uaa;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import uaa.domain.BaseEntity;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by UKi_Hi on 2018/5/29.
 */

@Entity
@Table(name = "uaa_token")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class UaaToken extends BaseEntity implements Serializable{
    @Id
    @Column(name = "ID")
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    @Column(name = "ACCESS_TOKEN")
    private String accesstoken;
    @Column(name = "REFRESH_TOKEN")
    private String refreshtoken;
    @Column(name = "VALID_TIME")
    private Long validtime;
    @Column(name = "CREATED_ID")
    private String createdid;


    public String getAccesstoken() {
        return accesstoken;
    }

    public void setAccesstoken(String accesstoken) {
        this.accesstoken = accesstoken;
    }

    public String getRefreshtoken() {
        return refreshtoken;
    }

    public void setRefreshtoken(String refreshtoken) {
        this.refreshtoken = refreshtoken;
    }

    public Long getValidtime() {
        return validtime;
    }

    public void setValidtime(Long validtime) {
        this.validtime = validtime;
    }

    public String getCreatedid() {
        return createdid;
    }

    public void setCreatedid(String createdid) {
        this.createdid = createdid;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
