package uaa.repository.app.blog;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import uaa.domain.app.blog.AppBlogBlog;

import java.util.List;

public interface AppBlogBlogRepository extends JpaRepository<AppBlogBlog,String> {
    Page<AppBlogBlog> findAllByCreateIdAndStatusOrderByCreatedDate(Pageable pageable, String createdId, String status);
    Page<AppBlogBlog> findAllByStatusAndPermissionTypeIsNotOrderByCreatedDate(Pageable pageable,String status, String permissionTypeIsNot);
}
