package domain;

import java.util.HashMap;
import java.util.Map;

public class SenderInfo {
    // �����ʼ��ķ�������IP�Ͷ˿�
    private String mailServerHost;
    private int mailServerPort;

    // �ʼ������ߵĵ�ַ
    private String fromAddress;
    private String fromUserName;

    // �ʼ������ߵĵ�ַ
    private String toAddress;
    private String toAddressName;

    // �ʼ������ߵĵ�ַ����, Key - Address, Value - Name
    private Map<String, String> toAddressMap = new HashMap<String, String>();

    // ��½�ʼ����ͷ��������û���������
    private String userName;
    private String password;

    // �Ƿ���Ҫ�����֤
    private boolean validate = false;

    // �ʼ�����
    private String subject;

    // �ʼ����ı�����
    private String message;

    private String url;
    // �ʼ��ı�������
    private String textMessage;
    // �ʼ���HTML����
    private String htmlMessage;

    // �ʼ��������ļ���, ����Ϊ������Դ
    private String[] attachmentPath;

    //�ʼ�������URL, ����Ϊ������Դ
    private String[] attachmentUrls;

    //��������
    private String attachmentDescription;

    //��������
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
