package uaa.domain.uaa;


import org.springframework.data.annotation.Id;
import uaa.domain.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "uaa_graph")
public class UaaGraph extends BaseEntity implements Serializable{
    @Id
    @Column(name = "ID")
    private String id;

    @Column(name = "VALUE")
    private String value;

    @Column(name = "TYPE")
    private String type;


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
