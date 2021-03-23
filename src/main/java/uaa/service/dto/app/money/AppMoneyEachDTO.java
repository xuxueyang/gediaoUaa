package uaa.service.dto.app.money;

import lombok.Data;

import javax.persistence.Column;

/**
 * @author xuxy
 * @date 2021/3/23 下午2:34
 */
@Data
public class AppMoneyEachDTO {
    private Integer id;


    private Float spend;

    private String  bigType;

    private String smallType;

    private String createdId;

}
