package uaa.domain.app.log;

import lombok.Data;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import uaa.domain.BaseEntity;

import javax.persistence.*;
import java.io.Serializable;
import java.time.ZonedDateTime;


@Entity
@Data
@Table(name = "app_log_plan_day_item")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class AppLogPlanDayItem extends BaseEntity implements Serializable {

    @Id
    @Column(name = "ID")
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @GeneratedValue(strategy = GenerationType.IDENTITY)//mysql数据库主键策略
    private String id;


    @Column(name = "BASE_ID")
    private String playDayId;

    @Column(name = "status")
    private Boolean status;//完成和未完成

    @Column(name = "desc")
    private String desc;//描述，任务

    @Column(name = "startTime")
    private ZonedDateTime startTime;//如果没有，则默认采用创建时间

    @Column(name = "endTime")
    private ZonedDateTime endTime;//完成的时候如果没有endTime那么设置endTime



}
