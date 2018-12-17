package uaa.service.dto.app.blog;

/**
 * Created by UKi_Hi on 2018/12/16.
 */
public class AppBlogSaveDto {

    private String id;
    private String title;
    private String content;
    private String token;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
