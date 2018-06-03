package uaa.domain.app.log;


import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import uaa.domain.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "app_log_detail")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class AppLogDetail extends BaseEntity implements Serializable {
    @Id
    @Column(name = "ID")
    private String id;


    @Column(name = "REMARKS")
    private String remarks;

    @Column(name = "BASE_ID")
    private String logEachId;

    @Column(name = "STATUS")
    private String status;

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLogEachId() {
        return logEachId;
    }

    public void setLogEachId(String logEachId) {
        this.logEachId = logEachId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
