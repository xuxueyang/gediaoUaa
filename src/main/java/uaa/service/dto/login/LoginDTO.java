package uaa.service.dto.login;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel
public class LoginDTO {
    @ApiModelProperty(name = "登录名")
    private String name;

    @ApiModelProperty(name = "密码")
    private String password;

    @ApiModelProperty(value = "图形验证码")
    private String graphCaptchaCode;

    @ApiModelProperty(value = "图形验证码编号")

    private String graphCaptchaCodeId;

    @ApiModelProperty(name = "登陆的空间")
    private String tenantCode;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getGraphCaptchaCode() {
        return graphCaptchaCode;
    }

    public void setGraphCaptchaCode(String graphCaptchaCode) {
        this.graphCaptchaCode = graphCaptchaCode;
    }

    public String getGraphCaptchaCodeId() {
        return graphCaptchaCodeId;
    }

    public void setGraphCaptchaCodeId(String graphCaptchaCodeId) {
        this.graphCaptchaCodeId = graphCaptchaCodeId;
    }

    public String getTenantCode() {
        return tenantCode;
    }

    public void setTenantCode(String tenantCode) {
        this.tenantCode = tenantCode;
    }
}
