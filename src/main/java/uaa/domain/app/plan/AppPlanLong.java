package uaa.domain.app.plan;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import uaa.domain.BaseEntity;

import javax.persistence.*;
import java.io.Serializable;
import java.time.ZonedDateTime;

/**
 * @author xuxy
 * @date 2021/3/26 上午9:21
 *
 * 长任务
 * 目前，只需要增删查改，设定一个名称，让plan绑定一下即可
 * 最多设定一下，范围（时间范围）
 *
 */

@Entity
@Table(name = "app_plan_long")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
//@Data
public class AppPlanLong extends BaseEntity implements Serializable {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name")
    private String name;

    @Column(name = "start_time")
    private ZonedDateTime startTime;

    @Column(name = "end_time")
    private ZonedDateTime endTime;

}
