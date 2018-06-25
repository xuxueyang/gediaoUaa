package uaa.service.dto;

import io.swagger.annotations.ApiModelProperty;

public class CreateUaaUserDTO {
    @ApiModelProperty(name = "loginName")
    private String loginName;

    @ApiModelProperty(name = "password")
    private String password;

    @ApiModelProperty(name = "tel")
    private String tel;

    @ApiModelProperty(name = "昵称")
    private String nickName;

    @ApiModelProperty(name = "email")
    private String email;

    @ApiModelProperty(name = "验证码.如果为空则默认是密码")
    private String verifyCode;

    @ApiModelProperty(name = "不同项目用户标识，目前只支持青龙会成员注册")
    private String projectType;

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

    public String getVerifyCode() {
        return verifyCode;
    }

    public void setVerifyCode(String verifyCode) {
        this.verifyCode = verifyCode;
    }

    public String getProjectType() {
        return projectType;
    }

    public void setProjectType(String projectType) {
        this.projectType = projectType;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }
}