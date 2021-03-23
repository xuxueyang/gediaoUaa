package uaa.repository.money;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import uaa.domain.money.AppMoneyEach;

/**
 * @author xuxy
 * @date 2021/3/23 下午2:31
 */
public interface MoneyEachRep extends JpaRepository<AppMoneyEach,Integer>, JpaSpecificationExecutor<AppMoneyEach> {

}
