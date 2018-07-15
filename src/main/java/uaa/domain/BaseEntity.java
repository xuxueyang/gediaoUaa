package uaa.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.envers.Audited;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.io.Serializable;
import java.time.Instant;
import java.time.ZonedDateTime;

/**
 * Created by UKi_Hi on 2018/5/29.
 */
@MappedSuperclass
@Audited
public abstract class BaseEntity implements Serializable {


    @CreatedDate
    @Column(name = "CREATED_DATE")
    private ZonedDateTime createdDate = ZonedDateTime.now();

    @LastModifiedDate
    @Column(name = "UPDATED_DATE")
    private ZonedDateTime updatedDate = ZonedDateTime.now();

    @Column(name = "VERSION")
    private String version;

    @Column(name = "TENANT_CODE")
    private String tenantCode;

    public ZonedDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(ZonedDateTime createdDate) {
        this.createdDate = createdDate;
    }


    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getTenantCode() {
        return tenantCode;
    }

    public void setTenantCode(String tenantCode) {
        this.tenantCode = tenantCode;
    }

    public ZonedDateTime getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(ZonedDateTime updatedDate) {
        this.updatedDate = updatedDate;
    }
}
