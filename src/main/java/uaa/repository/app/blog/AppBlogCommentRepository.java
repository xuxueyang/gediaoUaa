package uaa.repository.app.blog;

import org.springframework.data.jpa.repository.JpaRepository;
import uaa.domain.app.blog.AppBlogComment;

public interface AppBlogCommentRepository extends JpaRepository<AppBlogComment,String> {

}
