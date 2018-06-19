package uaa.service.dto.form;

import io.swagger.annotations.ApiModelProperty;
import uaa.service.dto.UaaBasePremissionDTO;

public class CreateDataDTO{
    @ApiModelProperty(value = "动态的JSON数据")
    private String data;

    private String formType;

    private String projectType;

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getProjectType() {
        return projectType;
    }

    public void setProjectType(String projectType) {
        this.projectType = projectType;
    }

    public String getFormType() {
        return formType;
    }

    public void setFormType(String formType) {
        this.formType = formType;
    }
}
