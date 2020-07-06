package uaa.domain.app.log;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import uaa.domain.BaseEntity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@Table(name = "app_log_plan_day")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class AppLogPlanDay extends BaseEntity implements Serializable {

    @Id
    @Column(name = "ID")
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @GeneratedValue(strategy = GenerationType.IDENTITY)//mysql数据库主键策略
    private String id;

    @Column(name = "CREATED_ID")
    private String createdId;

    @Column(name = "UPDATED_ID")
    private String updatedID;

    @Column(name = "BELONG_DATE")
    private String belongDate;

    @Column(name = "desc")
    @ApiModelProperty("描述，可以作为标题")
    private String desc;

    @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
    @JoinColumn(name = "BASE_ID", referencedColumnName = "ID", insertable = false, updatable = false)
    @org.hibernate.annotations.OrderBy(clause = "UPDATED_DATE")
    private Set<AppLogPlanDayItem> tags = new HashSet<>();


}
