package uaa.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * Created by UKi_Hi on 2018/5/29.
 */

@Entity
@Table(name = "qlh_token")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class UaaToken extends BaseEntity implements Serializable{
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
}
