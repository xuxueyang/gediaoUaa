package uaa.service.dto;

import io.swagger.annotations.ApiModelProperty;

public class CreateUaaUserDTO {
    @ApiModelProperty(name = "loginName")
    private String loginName;

    @ApiModelProperty(name = "password")
    private String password;

    @ApiModelProperty(name = "tel")
    private String tel;

    @ApiModelProperty(name = "email")
    private String email;



    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
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
}
