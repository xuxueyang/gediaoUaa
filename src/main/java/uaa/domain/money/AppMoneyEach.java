package uaa.domain.money;

import lombok.Data;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import uaa.domain.BaseEntity;
import uaa.domain.app.log.AppLogDetail;
import uaa.domain.app.log.AppLogEachTag;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "app_money_each")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
//@Data
public class AppMoneyEach extends BaseEntity implements Serializable {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;


    /**
     *  需要记录花费的类别
     *  需要记录花费的时间
     *  需要记录金额
     *
     */
    @Column(name = "spend",length = 10)
    private Float spend;

    @Column(name = "big_type",length = 20)
    private String  bigType;

    @Column(name = "small_type",length = 20)
    private String smallType;

    @Column(name = "created_id",length = 60)
    private String createdId;
    @Column(name = "ps")
    private String ps;

    @Column(name = "deleted",length = 1)
    private Boolean deleted;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Float getSpend() {
        return spend;
    }

    public void setSpend(Float spend) {
        this.spend = spend;
    }

    public String getBigType() {
        return bigType;
    }

    public void setBigType(String bigType) {
        this.bigType = bigType;
    }

    public String getSmallType() {
        return smallType;
    }

    public void setSmallType(String smallType) {
        this.smallType = smallType;
    }

    public String getCreatedId() {
        return createdId;
    }

    public void setCreatedId(String createdId) {
        this.createdId = createdId;
    }

    public String getPs() {
        return ps;
    }

    public void setPs(String ps) {
        this.ps = ps;
    }


    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }
}
