package uaa.service.app.money;

import io.swagger.models.auth.In;
import org.apache.commons.lang.StringUtils;
import org.hibernate.envers.Audited;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.stereotype.Service;
import uaa.domain.money.AppMoneyEach;
import uaa.repository.money.MoneyEachRep;
import uaa.service.dto.app.money.AppMoneyEachDTO;
import uaa.service.dto.app.money.QueryAppMoneyEachDTO;

import javax.persistence.criteria.*;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author xuxy
 * @date 2021/3/23 下午2:32
 */
@Service
public class AppMoneyEachService {
    @Autowired
    private MoneyEachRep moneyEachRep;


    public void createAndUpdate(AppMoneyEachDTO dto){
        AppMoneyEach target = null;
        if(dto.getId()!=null&&0<dto.getId()){
            target = moneyEachRep.getOne(dto.getId());
//            BeanUtils.copyProperties(dto, target);
            target.setBigType(dto.getBigType());
            target.setSmallType(dto.getSmallType());
            target.setSpend(dto.getSpend());
            target.setPay(dto.getPay());
        }else{
            target = new AppMoneyEach();
            target.setDeleted(false);
            BeanUtils.copyProperties(dto, target);
            target.setCreatedDate(ZonedDateTime.now());
            target.setCreatedId("1");

        }
        target.setUpdatedDate(ZonedDateTime.now());
        if(target!=null)
            moneyEachRep.save(target);
    }

    public Page<AppMoneyEach> page(QueryAppMoneyEachDTO dto, Pageable pageable){

        Page<AppMoneyEach> all = moneyEachRep.findAll(new Specification<AppMoneyEach>() {
            @Override
            public Predicate toPredicate(Root<AppMoneyEach> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
//                List<Predicate> predicates = new ArrayList<>();
                Predicate deleted = criteriaBuilder.equal(root.get("deleted").as(Boolean.class), false);
//                predicates.add(deleted);
//                predicates.add(criteriaBuilder.equal(root.get("pay").as(Boolean.class),true));
//                Order dataOrder = criteriaBuilder.desc(root.get("created_date"));
                criteriaQuery.orderBy(criteriaBuilder.desc(root.get("createdDate").as(ZonedDateTime.class)));

                return criteriaBuilder.and(deleted);
//                return criteriaQuery.orderBy(dataOrder).where(deleted).getRestriction();
            }
        },pageable);
        return all;
    }

    public Object getDetail(Integer id) {
        if(id==null || id==0){
            return null;
        }
        return moneyEachRep.findOne(id);
    }

    public Object deleteById(Integer id) {
        AppMoneyEach one = moneyEachRep.findOne(id);
        if(one!=null){
            one.setDeleted(true);
            moneyEachRep.saveAndFlush(one);
        }
        return null;
    }
}
