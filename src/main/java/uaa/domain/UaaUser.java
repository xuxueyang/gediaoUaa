package uaa.domain;


import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.validator.constraints.Email;
import uaa.config.Constants;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Entity
@Table(name = "qlh_user")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class UaaUser  implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Pattern(regexp = Constants.LOGIN_REGEX)
    @Size(min = 1, max = 80)
    @Column(name = "LOGIN_NAME",length = 80, unique = true, nullable = false)
    private String name;


    @Column(name = "TEL")
    private String tel;

    @Size(min = 5, max = 60)
    @Column(name = "EMAIL",length = 60, unique = true)
    private String email;

    @JsonIgnore
    @NotNull
    @Size(min = 8, max = 20)
    @Column(name = "PASSWORD", length = 200)
    private String password;


}
