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
@Data
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

}
