package uaa.repository.app.blog;

import org.springframework.data.jpa.repository.JpaRepository;
import uaa.domain.app.chat.AppChatContent;

import java.util.List;

/**
 * Created by UKi_Hi on 2018/12/23.
 */
public interface AppChatContentRepository extends JpaRepository<AppChatContent,String> {

}
