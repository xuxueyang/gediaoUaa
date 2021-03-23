package uaa.web.rest.app.money;

import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import uaa.config.ReturnCode;
import uaa.service.app.money.AppMoneyEachService;
import uaa.service.dto.app.money.AppMoneyEachDTO;
import uaa.service.dto.app.money.QueryAppMoneyEachDTO;
import uaa.web.rest.BaseResource;

/**
 * @author xuxy
 * @date 2021/3/23 下午3:06
 */
@RestController
@RequestMapping("api/money")
public class AppMoneyEachResource extends BaseResource {
    private AppMoneyEachService moneyEachService;

    @GetMapping("/page")
    public ResponseEntity page(QueryAppMoneyEachDTO dto, Pageable pageable){
        return prepareReturnResult(ReturnCode.DEFAULT_SUCCESS,moneyEachService.page(dto,pageable));
    }
    @PostMapping("")
    public ResponseEntity createOrUpdate(@RequestBody AppMoneyEachDTO dto){
        moneyEachService.createAndUpdate(dto);
        return prepareReturnResult(ReturnCode.DEFAULT_SUCCESS,null);
    }
    //todo 根据日期查询，根据类别查询统计
}
