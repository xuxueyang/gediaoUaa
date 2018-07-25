package uaa.web.rest.util.excel.vo.app.log;


import io.swagger.annotations.ApiModelProperty;

/**
 * 导出Each的DTO
 */
public class LogEachInVO{
    @ApiModelProperty(name = "用户ID")
    private String userId;
    @ApiModelProperty(name = "token")
    private String token;




    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
