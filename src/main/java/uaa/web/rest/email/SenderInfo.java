//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package uaa.web.rest.email;

import java.util.HashMap;
import java.util.Map;

public class SenderInfo {
    private String mailServerHost;
    private int mailServerPort;
    private String fromAddress;
    private String fromUserName;
    private String toAddress;
    private String toAddressName;
    private Map<String, String> toAddressMap = new HashMap();
    private String userName;
    private String password;
    private boolean validate = false;
    private String subject;
    private String message;
    private String url;
    private String textMessage;
    private String htmlMessage;
    private String[] attachmentPath;
    private String[] attachmentUrls;
    private String attachmentDescription;
    private String attachmentName;

    public SenderInfo() {
    }

    public String getMailServerHost() {
        return this.mailServerHost;
    }

    public void setMailServerHost(String mailServerHost) {
        this.mailServerHost = mailServerHost;
    }

    public int getMailServerPort() {
        return this.mailServerPort;
    }

    public void setMailServerPort(int mailServerPort) {
        this.mailServerPort = mailServerPort;
    }

    public boolean isValidate() {
        return this.validate;
    }

    public void setValidate(boolean validate) {
        this.validate = validate;
    }

    public String getFromAddress() {
        return this.fromAddress;
    }

    public void setFromAddress(String fromAddress) {
        this.fromAddress = fromAddress;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getToAddress() {
        return this.toAddress;
    }

    public void setToAddress(String toAddress) {
        this.toAddress = toAddress;
    }

    public String getUserName() {
        return this.userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getSubject() {
        return this.subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String[] getAttachmentPath() {
        return this.attachmentPath;
    }

    public void setAttachmentPath(String[] attachmentPath) {
        this.attachmentPath = attachmentPath;
    }

    public String[] getAttachmentUrls() {
        return this.attachmentUrls;
    }

    public void setAttachmentUrls(String[] attachmentUrls) {
        this.attachmentUrls = attachmentUrls;
    }

    public String getAttachmentDescription() {
        return this.attachmentDescription;
    }

    public void setAttachmentDescription(String attachmentDescription) {
        this.attachmentDescription = attachmentDescription;
    }

    public String getAttachmentName() {
        return this.attachmentName;
    }

    public void setAttachmentName(String attachmentName) {
        this.attachmentName = attachmentName;
    }

    public String getFromUserName() {
        return this.fromUserName;
    }

    public void setFromUserName(String fromUserName) {
        this.fromUserName = fromUserName;
    }

    public Map<String, String> getToAddressMap() {
        return this.toAddressMap;
    }

    public void setToAddressMap(Map<String, String> toAddressMap) {
        this.toAddressMap = toAddressMap;
    }

    public String getToAddressName() {
        return this.toAddressName;
    }

    public void setToAddressName(String toAddressName) {
        this.toAddressName = toAddressName;
    }

    public String getHtmlMessage() {
        return this.htmlMessage;
    }

    public void setHtmlMessage(String htmlMessage) {
        this.htmlMessage = htmlMessage;
    }

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTextMessage() {
        return this.textMessage;
    }

    public void setTextMessage(String textMessage) {
        this.textMessage = textMessage;
    }
}
