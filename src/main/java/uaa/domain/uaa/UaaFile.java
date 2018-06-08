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
@Table(name = "uaa_file")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class UaaFile extends BaseEntity implements Serializable {
    @Id
    @Column(name = "ID")
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    @Column(name = "CREATED_ID")
    private String createdId;

    @Column(name = "UPDATED_ID")
    private String updatedId;

    @Column(name = "SERVICE_IP")
    private String serviceIp;

    @Column(name = "REL_FILE_PATH")
    private String relFilePath;

    @Column(name = "ROOT_FILE_PATH")
    private String rootFilePath;

    @Column(name = "SIZE")
    private String size;

    @Column(name = "NAME")
    private String name;


    @Column(name = "MD5")
    private String md5;

    @Column(name = "STATUS")
    private String status;

    @Column(name = "VERIFY_CODE")
    private String verifyCode;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCreatedId() {
        return createdId;
    }

    public void setCreatedId(String createdId) {
        this.createdId = createdId;
    }

    public String getUpdatedId() {
        return updatedId;
    }

    public void setUpdatedId(String updatedId) {
        this.updatedId = updatedId;
    }

    public String getServiceIp() {
        return serviceIp;
    }

    public void setServiceIp(String serviceIp) {
        this.serviceIp = serviceIp;
    }

    public String getRelFilePath() {
        return relFilePath;
    }

    public void setRelFilePath(String relFilePath) {
        this.relFilePath = relFilePath;
    }

    public String getRootFilePath() {
        return rootFilePath;
    }

    public void setRootFilePath(String rootFilePath) {
        this.rootFilePath = rootFilePath;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }

    public String getVerifyCode() {
        return verifyCode;
    }

    public void setVerifyCode(String verifyCode) {
        this.verifyCode = verifyCode;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
//    ID	varchar	45	0	0	0	0	0	0		0		utf8	utf8_general_ci		-1	0
//    REL_FILE_PATH	varchar	255	0	-1	0	0	0	0		0		utf8	utf8_general_ci		0	0
//    SERVICE_IP	varchar	255	0	-1	0	0	0	0		0		utf8	utf8_general_ci		0	0
//    ROOT_FILE_PATH	varchar	255	0	-1	0	0	0	0		0		utf8	utf8_general_ci		0	0
//    VERSION	varchar	11	0	-1	0	0	0	0		0		utf8	utf8_general_ci		0	0
//    CREATED_DATE	datetime	0	0	-1	0	0	0	0		0					0	0
//    UPDATED_DATE	datetime	0	0	-1	0	0	0	0		0					0	0
//    CREATED_ID	varchar	45	0	-1	0	0	0	0		0		utf8	utf8_general_ci		0	0
//    UPDATED_ID	varchar	45	0	-1	0	0	0	0		0		utf8	utf8_general_ci		0	0
