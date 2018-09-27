package uaa.domain.uaa;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import uaa.domain.BaseEntity;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by UKi_Hi on 2018/9/2.
 */

@Entity
@Table(name = "uaa_dict_value")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class UaaDictValue  extends BaseEntity implements Serializable {
    @Id
    @Column(name = "ID")
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    @Column(name = "VALUE")
    private String value;

    @Column(name = "DICT_ID")
    private String dictId;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getDictId() {
        return dictId;
    }

    public void setDictId(String dictId) {
        this.dictId = dictId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }



}
