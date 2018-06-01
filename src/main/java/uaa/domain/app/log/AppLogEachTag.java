package uaa.domain.app.log;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.annotation.Id;
import uaa.domain.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;


@Entity
@Table(name = "app_log_EachTag")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class AppLogEachTag extends BaseEntity implements Serializable {

    @Id
    @Column(name = "ID")
    private String id;


    @Column(name = "BASE_ID")
    private String logEachId;


    @Column(name = "TAG_ID")
    private String tagId;

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
        return tagId;
    }

    public void setTagId(String tagId) {
        this.tagId = tagId;
    }
}
