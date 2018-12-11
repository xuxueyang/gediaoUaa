package uaa.repository.app.blog;

import org.springframework.data.jpa.repository.JpaRepository;
import uaa.domain.app.blog.AppBlogBlog;

public interface AppBlogBlogRepository extends JpaRepository<AppBlogBlog,String> {

}
