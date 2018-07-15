package uaa.service.dto.app.log;

import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * Created by UKi_Hi on 2018/6/2.
 */
public class UpdateLogEachDTO  extends CreateLogEachDTO{
    //只更新LogEach的信息
    @ApiModelProperty(name = "ID")
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
