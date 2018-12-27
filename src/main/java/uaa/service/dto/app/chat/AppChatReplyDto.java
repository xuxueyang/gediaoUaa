package uaa.service.dto.app.chat;

/**
 * Created by UKi_Hi on 2018/12/23.
 */
public class AppChatReplyDto {
    private String id;
    private String content;
    private String replyId;
    private String replyType;
    private String createdDate;
    private String createdId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }



    public String getReplyId() {
        return replyId;
    }

    public void setReplyId(String replyId) {
        this.replyId = replyId;
    }

    public String getReplyType() {
        return replyType;
    }

    public void setReplyType(String replyType) {
        this.replyType = replyType;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreatedId() {
        return createdId;
    }

    public void setCreatedId(String createdId) {
        this.createdId = createdId;
    }
}
