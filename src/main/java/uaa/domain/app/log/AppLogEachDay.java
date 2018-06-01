package uaa.domain.app.log;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import uaa.domain.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "app_log_EachDay")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class AppLogEachDay extends BaseEntity implements Serializable {

    @Id
    @Column(name = "ID")
    private String id;


    @Column(name = "BASE_ID")
    private String logEachId;


    @Column(name = "DAY_ID")
    private String dayId;

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

    public String getTagId() {
        return dayId;
    }

    public void setTagId(String dayId) {
        this.dayId = dayId;
    }
}
