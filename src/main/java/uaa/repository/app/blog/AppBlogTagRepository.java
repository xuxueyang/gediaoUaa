package uaa.repository.app.blog;

import org.springframework.data.jpa.repository.JpaRepository;
import uaa.domain.app.blog.AppBlogTag;

import java.util.List;

/**
 * Created by UKi_Hi on 2018/6/4. 12.18
 */
public interface AppBlogTagRepository extends JpaRepository<AppBlogTag,String> {
    List<AppBlogTag>  findAllByBaseIdAndStatus(String baseId,String status);
}
