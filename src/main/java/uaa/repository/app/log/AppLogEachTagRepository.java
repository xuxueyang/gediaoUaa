package uaa.repository.app.log;

import org.springframework.data.jpa.repository.JpaRepository;
import uaa.domain.app.log.AppLogEachTag;

/**
 * Created by UKi_Hi on 2018/6/4.
 */
public interface AppLogEachTagRepository extends JpaRepository<AppLogEachTag,String> {
}
