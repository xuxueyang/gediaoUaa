package uaa.repository.app.blog;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import uaa.domain.app.blog.AppBlogBlog;

import java.util.List;

public interface AppBlogBlogRepository extends JpaRepository<AppBlogBlog,String>,JpaSpecificationExecutor<AppBlogBlog> {
    Page<AppBlogBlog> findAllByCreateIdAndStatusOrderByCreatedDate(Pageable pageable, String createdId, String status);
    Page<AppBlogBlog> findAllByStatusAndPermissionTypeIsNotOrderByCreatedDate(Pageable pageable,String status, String permissionTypeIsNot);

//    @Query(value = "select y.id, y.CATEGORY_ID,y.CREATED_DATE,y.CREATE_ID,y.PERMISSION_TYPE,y.PERMISSION_VERIFY,y.READ_COUNT,y.SOURCE_TYPE,y.`STATUS`,y.TITLE,y.TITLE_IMAGE_FILE_ID,y.UPDATED_DATE from app_blog_blog y where y.`STATUS` = ?1",nativeQuery = true)
//    List<AppBlogBlog> findByCategoryAndStatusAndNative(String save);
}
