package uaa.config;

/**
 * Application constants.
 */
public final class Constants {

    // Regex for acceptable logins
    public static final String LOGIN_REGEX = "^[_'.@A-Za-z0-9-]*$";

    public static final String SYSTEM_ACCOUNT = "system";
    public static final String ANONYMOUS_USER = "anonymoususer";
    public static final String DEFAULT_LANGUAGE = "en";
    //email templates
    public static final String EMAIL_TEMPLATE_TEST_SEND = "testSendEmail";
    public static final String EMAIL_VAR_URL = "url";
    public static final String TOKEN_BODY_SEPARATOR = "_";

    public static final String ACCESS_TOKEN = "access_token";
    public static final String REFRESH_TOKEN = "refresh_token";
    public static final String USERINFO = "userinfo";

    //token超时时间
    public static final Long TOKEN_VALID_TIME = 30*30*60L;

    public static final Long IP_RECORD_TIME = 30*60L;


    //LogEach状态
    public static final String APP_LOG_STATUS_SAVE = "S";///保存状态
    public static final String APP_LOG_STATUS_DELETE = "D";//删除
    public static final String APP_LOG_STATUS_Y = "Y";//完成
    public static final String APP_LOG_STATUS_N = "N";//未完成

    //LogDay的状态
    public static final String APP_LOG_DAY_TYPE_TEL = "TEL";//技术文档积累
    public static final String APP_LOG_DAY_TYPE_DIARY = "DIARY";//日志

    //File的文件状态
    public static final String FILE_STATUS_SAVE = "S";//保存状态
    public static final String FILE_STATUS_DELETE = "D";//删除状态

    /**
     * 消息
     */
    //消息的工程类别
    public static final String PROJECT_TYPE_UE4_XY = "UE4_XY";//虚幻四夏夜游戏开发进度消息
    public static final String PROJECT_TYPE_QINGLONGHUI = "QLH";//青龙会开发进度消息
    public static final String PROJECT_TYPE_PERSONAL_MESSAGE = "PERSONAL_MESSAGE";//个人消息

    //消息的类别——已完善-待完善——bug

//    public enum MESSAGE_TYPE{
//        TODO,
//        DONE,
//        BUG,
//        QLH_AUTHOR_SAY
//    }
    public static final String MESSAGE_TYPE_TODO = "TODO";//待完善
    public static final String MESSAGE_TYPE_DONE  = "DONE";//已完成
    public static final String MESSAGE_TYPE_BUG = "BUG";//bug
    public static final String MESSAGE_TYPE_MEMBER_SAY = "MEMBER_SAY";//作者吐槽——青龙会
    public static final String MESSAGE_TYPE_PAD = "PAD"; //备忘录
    //消息的状态
    public static final String MESSAGE_STATUS_DELETE = "DELETE";//删除
    public static final String MESSAGE_STATUS_SAVE  = "SAVE";//保存
    //用户状态
    public static final String USER_STATUS_SAVE = "S";///保存状态
    public static final String USER_STATUS_DELETE = "D";//删除
    //表单状态
    public static final String TASK_FORM_DATA_WAIT = "WAIT";///待审核
    public static final String TASK_FORM_DATA_PASS = "PASS";///保存状态

    private Constants() {
    }
}
