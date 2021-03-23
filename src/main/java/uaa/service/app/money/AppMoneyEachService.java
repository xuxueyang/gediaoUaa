package uaa.service.app.money;

import org.hibernate.envers.Audited;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.stereotype.Service;
import uaa.domain.money.AppMoneyEach;
import uaa.repository.money.MoneyEachRep;
import uaa.service.dto.app.money.AppMoneyEachDTO;
import uaa.service.dto.app.money.QueryAppMoneyEachDTO;

import java.time.ZonedDateTime;

/**
 * @author xuxy
 * @date 2021/3/23 下午2:32
 */
@Service
public class AppMoneyEachService {
    @Audited
    private MoneyEachRep moneyEachRep;


    public void createAndUpdate(AppMoneyEachDTO dto){
        AppMoneyEach target = null;
        if(dto.getId()!=null&&0<dto.getId()){
            target = moneyEachRep.getOne(dto.getId());
//            BeanUtils.copyProperties(dto, target);
            target.setBigType(dto.getBigType());
            target.setSmallType(dto.getSmallType());
            target.setSpend(dto.getSpend());
        }else{
            target = new AppMoneyEach();
            BeanUtils.copyProperties(dto, target);
            target.setCreatedDate(ZonedDateTime.now());
            target.setCreatedId("1");

        }
        target.setUpdatedDate(ZonedDateTime.now());
        if(target!=null)
            moneyEachRep.save(target);
    }

    public Page<AppMoneyEach> page(QueryAppMoneyEachDTO dto, Pageable pageable){
//        Specification specification = new Specifications<AppMoneyEachDTO>()
        Page<AppMoneyEach> all = moneyEachRep.findAll(pageable);
        return all;
    }
}
