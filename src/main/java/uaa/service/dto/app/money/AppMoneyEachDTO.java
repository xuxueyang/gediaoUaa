package uaa.service.dto.app.money;

import lombok.Data;

import javax.persistence.Column;

/**
 * @author xuxy
 * @date 2021/3/23 下午2:34
 */
//@Data
public class AppMoneyEachDTO {
    private Integer id;


    private Float spend;

    private String  bigType;

    private String smallType;

    private String createdId;
    private String ps;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Float getSpend() {
        return spend;
    }

    public void setSpend(Float spend) {
        this.spend = spend;
    }

    public String getBigType() {
        return bigType;
    }

    public void setBigType(String bigType) {
        this.bigType = bigType;
    }

    public String getSmallType() {
        return smallType;
    }

    public void setSmallType(String smallType) {
        this.smallType = smallType;
    }

    public String getCreatedId() {
        return createdId;
    }

    public void setCreatedId(String createdId) {
        this.createdId = createdId;
    }

    public String getPs() {
        return ps;
    }

    public void setPs(String ps) {
        this.ps = ps;
    }
}
