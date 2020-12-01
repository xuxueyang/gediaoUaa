package uaa.service.dto.app.log;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * Created by UKi_Hi on 2018/6/3.
 */
@Data
public class CreateLogEachDTO {
    @ApiModelProperty(name = "标题")
    private String title;

    @ApiModelProperty(name = "消息")
    private String message;

    @ApiModelProperty(name = "状态")
    private String status;


    @ApiModelProperty(name = "所属的日期")
    private String belongDate;


    @ApiModelProperty(name = "token")
    private String token;

    @ApiModelProperty(name = "userId")
    private String userId;

    @ApiModelProperty(name = "tags的ID")
    private List<String> tags;

    @ApiModelProperty(name = "番茄工作法的状态")
    private String tomatoType;

}
