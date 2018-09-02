package uaa.service.dto.dict;


import uaa.service.dto.UaaBasePremissionDTO;

/**
 * Created by UKi_Hi on 2018/9/2.
 */
public class UpdateDictValeDTO extends UaaBasePremissionDTO {
    private String id;
    private String value;
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }


}
