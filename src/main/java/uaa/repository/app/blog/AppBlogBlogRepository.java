package uaa.repository.app.blog;

import org.springframework.data.jpa.repository.JpaRepository;
import uaa.domain.app.blog.AppBlogBlog;

import java.util.List;

public interface AppBlogBlogRepository extends JpaRepository<AppBlogBlog,String> {
    List<AppBlogBlog> findAllByCreateIdAndStatus(String createdId,String status);
    List<AppBlogBlog> findAllByStatusAndPermissionTypeIsNot(String status, String permissionTypeIsNot);
}
