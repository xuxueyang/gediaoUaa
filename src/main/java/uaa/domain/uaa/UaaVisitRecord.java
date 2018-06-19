package uaa.domain.uaa;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.Instant;

@Entity
@Table(name = "uaa_visit_record")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class UaaVisitRecord {
    //目前就简单的ip、时间、id、即可吧
    @Id
    @Column(name = "ID")
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    @Column(name = "CREATED_DATE")
    private Instant createdDate;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }



    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    @Column(name = "IP")
    private String ip;

    public Instant getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }
}
