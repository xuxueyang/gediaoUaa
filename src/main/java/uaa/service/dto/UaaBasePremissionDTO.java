package uaa.service.dto;

import io.swagger.annotations.ApiModelProperty;

public class UaaBasePremissionDTO {
    @ApiModelProperty(value = "验证自己身份的code")
    private String verifyCode;

    @ApiModelProperty(value = "验证身份的用户名")
    private String loginName;

    @ApiModelProperty(value = "用code验证可以不用登陆。或者使用token")
    private String token;

    public String getVerifyCode() {
        return verifyCode;
    }

    public void setVerifyCode(String verifyCode) {
        this.verifyCode = verifyCode;
    }


    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
