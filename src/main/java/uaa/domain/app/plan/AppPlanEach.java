package uaa.domain.app.plan;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import uaa.domain.BaseEntity;

import javax.persistence.*;
import java.io.Serializable;
import java.time.ZonedDateTime;

/**
 * @author xuxy
 * @date 2021/3/26 上午9:10
 *
 *
 * 记录，每天要做的事情（默认12点前为单位）
 *
 * 1。要做什么，描述
 * 2。是否完成
 * 3。时间段
 * 4。-自动计算预计时间
 * 5。完成时候要输入大概时间范围
 * 6。随时反馈（先验，要求列的计划）
 *
 *
 */

@Entity
@Table(name = "app_plan_each")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
//@Data
public class AppPlanEach extends BaseEntity implements Serializable {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    //隶属日期-自动根据，完成时间来预计
    @Column(name = "belong_date")
    private String belongDate;

    //备注的任务
    @Column(name = "ps")
    private String ps;

    @Column(name = "over",length = 1,nullable = false,columnDefinition="int default 0")
    private Boolean over = false;

    //暂时不考虑长任务，这个只是任务的细分（但是理论上，可以建立任务集，把任务绑定到对应的任务上）
    @Column(name = "link_plan_long_id")
    private Integer linkPlanLongId;

    @Column(name = "start_time")
    private ZonedDateTime startTime;

    @Column(name = "end_time")
    private ZonedDateTime endTime;

    //反思的结果
    @Column(name = "re")
    private String re;
}
