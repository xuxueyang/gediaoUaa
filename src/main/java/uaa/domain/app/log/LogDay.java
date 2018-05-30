package uaa.domain.app.log;

import org.checkerframework.checker.units.qual.C;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import uaa.domain.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.time.Instant;

@Entity
@Table(name = "app_log_detail")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class LogDay extends BaseEntity implements Serializable{
    @Column(name = "CREATED_ID")
    private String createdId;

    @Column(name = "UPDATED_ID")
    private String updatedId;

    @Column(name = "ENCODE")
    private String encode;

    @Column(name = "BELONG_DATE")
    private Instant belongDate;

    @Column(name = "TITLE")

    public String getCreatedId() {
        return createdId;
    }

    public void setCreatedId(String createdId) {
        this.createdId = createdId;
    }

    public String getUpdatedId() {
        return updatedId;
    }

    public void setUpdatedId(String updatedId) {
        this.updatedId = updatedId;
    }

    public String getEncode() {
        return encode;
    }

    public void setEncode(String encode) {
        this.encode = encode;
    }

    public Instant getBelongDate() {
        return belongDate;
    }

    public void setBelongDate(Instant belongDate) {
        this.belongDate = belongDate;
    }
}
