package domain;

import java.util.HashMap;
import java.util.Map;

public class SenderInfo {
    // 发送邮件的服务器的IP和端口
    private String mailServerHost;
    private int mailServerPort;

    // 邮件发送者的地址
    private String fromAddress;
    private String fromUserName;

    // 邮件接收者的地址
    private String toAddress;
    private String toAddressName;

    // 邮件接收者的地址集合, Key - Address, Value - Name
    private Map<String, String> toAddressMap = new HashMap<String, String>();

    // 登陆邮件发送服务器的用户名和密码
    private String userName;
    private String password;

    // 是否需要身份验证
    private boolean validate = false;

    // 邮件主题
    private String subject;

    // 邮件的文本内容
    private String message;

    private String url;
    // 邮件的备用内容
    private String textMessage;
    // 邮件的HTML内容
    private String htmlMessage;

    // 邮件附件的文件名, 附件为本地资源
    private String[] attachmentPath;

    //邮件附件的URL, 附件为在线资源
    private String[] attachmentUrls;

    //附件描述
    private String attachmentDescription;

    //附件名称
    private String attachmentName;

    public String getMailServerHost() {
        return mailServerHost;
    }

    public void setMailServerHost(String mailServerHost) {
        this.mailServerHost = mailServerHost;
    }

    public int getMailServerPort() {
        return mailServerPort;
    }

    public void setMailServerPort(int mailServerPort) {
        this.mailServerPort = mailServerPort;
    }

    public boolean isValidate() {
        return validate;
    }

    public void setValidate(boolean validate) {
        this.validate = validate;
    }

    public String getFromAddress() {
        return fromAddress;
    }

    public void setFromAddress(String fromAddress) {
        this.fromAddress = fromAddress;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getToAddress() {
        return toAddress;
    }

    public void setToAddress(String toAddress) {
        this.toAddress = toAddress;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String[] getAttachmentPath() {
        return attachmentPath;
    }

    public void setAttachmentPath(String[] attachmentPath) {
        this.attachmentPath = attachmentPath;
    }

    public String[] getAttachmentUrls() {
        return attachmentUrls;
    }

    public void setAttachmentUrls(String[] attachmentUrls) {
        this.attachmentUrls = attachmentUrls;
    }

    public String getAttachmentDescription() {
        return attachmentDescription;
    }

    public void setAttachmentDescription(String attachmentDescription) {
        this.attachmentDescription = attachmentDescription;
    }

    public String getAttachmentName() {
        return attachmentName;
    }

    public void setAttachmentName(String attachmentName) {
        this.attachmentName = attachmentName;
    }

    public String getFromUserName() {
        return fromUserName;
    }

    public void setFromUserName(String fromUserName) {
        this.fromUserName = fromUserName;
    }

    public Map<String, String> getToAddressMap() {
        return toAddressMap;
    }

    public void setToAddressMap(Map<String, String> toAddressMap) {
        this.toAddressMap = toAddressMap;
    }

    public String getToAddressName() {
        return toAddressName;
    }

    public void setToAddressName(String toAddressName) {
        this.toAddressName = toAddressName;
    }

    public String getHtmlMessage() {
        return htmlMessage;
    }

    public void setHtmlMessage(String htmlMessage) {
        this.htmlMessage = htmlMessage;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTextMessage() {
        return textMessage;
    }

    public void setTextMessage(String textMessage) {
        this.textMessage = textMessage;
    }
}
