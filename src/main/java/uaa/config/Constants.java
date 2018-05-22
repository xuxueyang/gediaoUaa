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

    private Constants() {
    }
}
