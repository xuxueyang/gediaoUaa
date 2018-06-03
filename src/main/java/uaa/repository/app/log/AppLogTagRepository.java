package uaa.repository.app.log;

import org.springframework.data.jpa.repository.JpaRepository;
import uaa.domain.app.log.AppLogTag;

/**
 * Created by UKi_Hi on 2018/6/4.
 */
public interface AppLogTagRepository extends JpaRepository<AppLogTag,String> {

}
