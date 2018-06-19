package uaa.domain.uaa;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import uaa.domain.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "uaa_form_data")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class UaaFormData extends BaseEntity  implements Serializable {
    @Id
    @Column(name = "ID")
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    @Column(name = "STATUS")
    private String status;

    @Column(name = "FORM_TYPE")
    private String formType;

    @Column(name = "PROJECT_TYPE")
    private String projectType;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getProjectType() {
        return projectType;
    }

    public void setProjectType(String projectType) {
        this.projectType = projectType;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    @Column(name = "DATA")
    private String data;

    public String getFormType() {
        return formType;
    }

    public void setFormType(String formType) {
        this.formType = formType;
    }
}
