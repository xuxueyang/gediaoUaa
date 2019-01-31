package uaa.repository.app.blog;

import org.springframework.data.jpa.repository.JpaRepository;
import uaa.domain.app.blog.AppBlogCategory;

/**
 * Created by xuxy on 2019/1/31.
 */
public interface AppBlogCategoryRepository extends JpaRepository<AppBlogCategory,String> {

    AppBlogCategory findOneByName(String name);
}
