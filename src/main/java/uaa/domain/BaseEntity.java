package uaa.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.envers.Audited;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.io.Serializable;
import java.time.Instant;

/**
 * Created by UKi_Hi on 2018/5/29.
 */
@MappedSuperclass
@Audited
public abstract class BaseEntity implements Serializable {


    @CreatedDate
    @Column(name = "CREATED_DATE", nullable = false)
    private Instant createdDate = Instant.now();

    @LastModifiedDate
    @Column(name = "UPDATED_DATE")
    private Instant updatedDate = Instant.now();

    @Column(name = "VERSION")
    private String version;

    @Column(name = "TENANT_CODE")
    private String tenantCode;

    public Instant getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Instant createdDate) {
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

    public Instant getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Instant updatedDate) {
        this.updatedDate = updatedDate;
    }
}
