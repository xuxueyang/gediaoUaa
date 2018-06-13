package uaa.service.dto.message;

import uaa.service.dto.UaaBasePremissionDTO;

public class UpdateMessageDTO extends UaaBasePremissionDTO {
    private String id;

    private String value;

    private String ps;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPs() {
        return ps;
    }

    public void setPs(String ps) {
        this.ps = ps;
    }
}
