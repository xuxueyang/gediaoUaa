package uaa.config;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Application constants.
 */
public final class Constants {
    public enum ProjectType{
        GEDIAO,QLH,NULL
    }
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
    public static final String APP_LOG_STATUS_SAVE = Constants.SAVE;///保存状态
    public static final String APP_LOG_STATUS_DELETE = Constants.DELETE;//删除
    public static final String APP_LOG_STATUS_Y = "Y";//完成
    public static final String APP_LOG_STATUS_N = "N";//未完成

    //LogDay的状态
    public static final String APP_LOG_DAY_TYPE_TEL = "TEL";//技术文档积累
    public static final String APP_LOG_DAY_TYPE_DIARY = "DIARY";//日志

    //File的文件状态
    public static final String FILE_STATUS_SAVE = Constants.SAVE;//保存状态
    public static final String FILE_STATUS_DELETE = Constants.DELETE;//删除状态

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
    public static final String MESSAGE_STATUS_DELETE = Constants.DELETE;//删除
    public static final String MESSAGE_STATUS_SAVE  = Constants.SAVE;//保存
    //空间状态
    public static final String TENANT_STATUS_DELETE = Constants.DELETE;//删除
    public static final String TENANT_STATUS_SAVE  = Constants.SAVE;//保存
    //用户状态
    public static final String USER_STATUS_SAVE = Constants.SAVE;///保存状态
    public static final String USER_STATUS_DELETE = Constants.DELETE;//删除
    //表单状态
    public static final String TASK_FORM_DATA_WAIT = "WAIT";///待审核
    public static final String TASK_FORM_DATA_PASS = "PASS";///保存状态

    public static final String VERSION = "0";

    //状态
    public static final String SAVE = "S";///保存状态
    public static final String DELETE = "D";//删除

    public enum LogStatus{
        //静态、动态、删除、保存
        STATIC,
        DYNAMIC,
        DELETE,
        SAVE
    }
    //DICT 的 type
    public enum DictType{
        APP_GEDIAO_LOG_MESSAGE_STATUS,
    }
    //Http 的 type
    public enum HttpType{
        POST,
        GET,
        OPTIONS,
        DELETE,
        PUT
    }
    private Constants() {
    }
    public interface LogEach_Type{
        String UNFinished = "1";//未完成的ID
        String Mem = "6";//备忘录的ID
        String JOURNAL = "9";//流水账的ID
        String FINISHED = "2";//已完成的ID
    }

    public  enum  PERMISSION_TYPE{
        //权限类型
        OnlyOne , //仅自己可见
        KeyCan , //通关验证密码，可见
        NoLimit //不需要限制，均可见
    }
    public  enum sourceType{
        Owner,//原创
        Transfer//转载
    }
    private static  String[] SourceType = null;
    public static String[] getSourceType() {
        if(SourceType==null){
            sourceType[] values = sourceType.values();
            List<String> SourceTypes = new ArrayList<>();
            for(int i=0;i<values.length;i++){
                SourceTypes.add(values[i].name());
            }
            SourceType = (String[])SourceTypes.toArray();
        }
        return SourceType;
    }
    private static  String[] PermissionType = null;

    public static String[] getPermissionType() {
        if(PermissionType==null){
            PERMISSION_TYPE[] permission_values = PERMISSION_TYPE.values();
            List<String> permission_types = new ArrayList<>();
            for(int i=0;i<permission_values.length;i++){
                permission_types.add(permission_values[i].name());
            }
            PermissionType = (String[])permission_types.toArray();
        }
        return PermissionType;
    }
//    @Test
//    public void testToArray(){
//        getPermissionType();
//    }

}
