package uaa.domain;


import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import uaa.config.Constants;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.Instant;

@Entity
@Table(name = "qlh_user")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class UaaUser  extends BaseEntity implements Serializable {
    @Pattern(regexp = Constants.LOGIN_REGEX)
    @Size(min = 1, max = 80)
    @Column(name = "LOGIN_NAME", unique = true, nullable = false)
    private String name;


    @Column(name = "TEL")
    private String tel;

    @Size(min = 5, max = 60)
    @Column(name = "EMAIL", unique = true)
    private String email;

    @JsonIgnore
    @NotNull
    @Column(name = "PASSWORD")
    private String password;

    @Column(name = "LOGIN_IP")
    private String loginip;

    @Column(name = "LOGIN_DATE")
    private Instant logindate;

    @Column(name = "STATUS")
    private String status;

    @Column(name = "TYPE")
    private String type;

    @Column(name = "ACCT_VERFIED")
    private String acctverfied;

    @Column(name = "IMAGE")
    private String image;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getLoginip() {
        return loginip;
    }

    public void setLoginip(String loginip) {
        this.loginip = loginip;
    }



    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAcctverfied() {
        return acctverfied;
    }

    public void setAcctverfied(String acctverfied) {
        this.acctverfied = acctverfied;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Instant getLogindate() {
        return logindate;
    }

    public void setLogindate(Instant logindate) {
        this.logindate = logindate;
    }
}
