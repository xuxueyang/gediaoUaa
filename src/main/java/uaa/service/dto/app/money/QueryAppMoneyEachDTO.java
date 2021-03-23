package uaa.service.dto.app.money;

import lombok.Data;

import java.time.ZonedDateTime;

/**
 * @author xuxy
 * @date 2021/3/23 下午2:51
 */
@Data
public class QueryAppMoneyEachDTO {
    private ZonedDateTime startTime;
    private ZonedDateTime endTime;
    private String smallType;
    private String bigType;

}
