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
    public static final Long TOKEN_VALID_TIME = 30*60L;

    //LogEach状态
    public static final String APP_LOG_STATUS_SAVE = "S";///保存状态
    public static final String APP_LOG_STATUS_DELETE = "D";//删除
    public static final String APP_LOG_STATUS_Y = "Y";//删除
    public static final String APP_LOG_STATUS_N = "N";//删除


    private Constants() {
    }
}
