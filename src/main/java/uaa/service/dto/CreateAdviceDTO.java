package uaa.service.dto;

import io.swagger.annotations.ApiModelProperty;

public class CreateAdviceDTO {
    @ApiModelProperty(name = "联系方式")
    private String contract;

    @ApiModelProperty(name = "建议内容")
    private String content;

    public String getContract() {
        return contract;
    }

    public void setContract(String contract) {
        this.contract = contract;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
