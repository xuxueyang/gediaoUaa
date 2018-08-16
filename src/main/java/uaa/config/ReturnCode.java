package core;

public class ReturnCode {

    //正常返回2XXX
    public static final String DEFAULT_SUCCESS = "2000"; //成功
    public static final String CREATE_SUCCESS = "2001"; //创建成功
    public static final String UPDATE_SUCCESS = "2002"; //修改成功
    public static final String DELETE_SUCCESS = "2003"; //删除成功
    public static final String GET_SUCCESS = "2004"; //获取成功

    //权限验证错误 3XXX

    public static final String ERROR_AOTHEN_MISS_USERNAME = "3000"; //账号不存在
    public static final String ERROR_AOTHEN_MISS_TEL = "3001"; //未在系统里匹配到该用户(快速登录未匹配手机号码)
    public static final String ERROR_NO_PERMISSIONS_DELETE = "3002"; //没有权限删除资源
    public static final String ERROR_DEFAULT_USER_HAS_NO_ROLES = "3003"; //默认身份下没有角色
    public static final String ERROR_ACCT_IS_NOT_ACTIVATED = "3004"; //账号未激活
    public static final String ERROR_HAS_NOT_USERS = "3005"; //账号下未找到有效身份
    public static final String ERROR_PASSWORD_NOT_CORRECT_CODE = "3006"; //账号密码不正确
    public static final String ERROR_AUTHORIZATION = "3007"; //无效的Authorization
    public static final String ERROR_USER_HAS_LOGOUT = "3008"; //用户已经登出
    public static final String ERROR_HAVE_NO_PERMISSION_OPERATION = "3009"; //没有权限进行操作
    public static final String ERROR_HAVE_NO_PERMISSION_OPERATION_SYS_ROLE = "3010"; //没有权限操作系统角色
    public static final String ERROR_HAVE_NO_PERMISSION_OPERATION_OWNER_ROLE = "3011"; //没有权限操作owner角色
    public static final String ERROR_HAVE_NO_PERMISSION_OPERATION_MEMBER_ROLE = "3012"; //没有权限操作member角色
    public static final String ERROR_HAVE_NO_PERMISSION_OPERATION_OWNER_USER = "3013"; //没有权限操作owner用户
    public static final String ERROR_USER_HAVE_NO_SYS_ROLE = "3014"; //用户缺失系统角色
    public static final String ERROR_NO_PERMISSIONS_UPDATE = "3015"; //没有权限编辑资源



    //-----------------------对应的返回请求上的httpStatus 为401-------------------------------
    public static final String ERROR_TOKEN_INVALID = "3016"; //token无效

    public static final String ERROR_NOT_FIND_CODE_WITH_APP_ID_CODE = "3017"; //未知的appId
    public static final String ERROR_HEADER_NOT_FIND_APP_ID_CODE = "3018"; //header里没有找到参数appId
    public static final String ERROR_HEADER_SPACEURI_TENANT_CODE_IS_DEFFRENT_WITH_TOKEN_CODE = "3019";// token里的tenantCode与 spaceUri里的tenantCode不一致
    public static final String ERROR_NOT_FIND_USER_BY_SPACEURI_CODE = "3020"; //当前spaceUri下无法找到有效身份（切换身份时）
    public static final String ERROR_NOT_FIND_CODE_WITH_SPACE_URI_CODE = "3021"; //根据spaceUri未匹配到InstanceCode、TenantCode、SpaceCode
    public static final String ERROR_HEADER_NOT_FIND_SPACE_URI_CODE = "3022"; //header里没有找到参数spaceUri
    public static final String ERROR_HEADER_NOT_TENANT_CODE = "3023"; //不存在的空间

    //-----------------------对应的返回请求上的httpStatus 为401-------------------------------

    public static final String ERROR_ACCT_NEED_CHECK = "3030"; //账号需要审核

    //异常错误  4XXX
    public static final String ERROR_QUERY = "4000"; //查询失败
    public static final String ERROR_CREATE = "4001"; //创建失败
    public static final String ERROR_UPDATE = "4002"; //更新失败
    public static final String ERROR_DELETE = "4003"; //删除失败
    public static final String ERROR_SEND_SMS = "4004"; //短信发送失败
    public static final String ERROR_UPLOAD_FILE = "4005"; //文件上传失败
    public static final String ERROR_SEND_EMAIL = "4006"; //邮件发送失败
    public static final String ERROR_LOGIN = "4007"; // 登录失败

    public static final String ERROR_UN_KNOW_PROGRAM = "4999"; //包含以上异常


    //非逻辑验证错误 50XX：字段   51XX：资源
    public static final String ERROR_FIELD_EMPTY = "5001"; //字段不能为空
    public static final String ERROR_FIELD_FORMAT = "5002"; //字段格式不正确
    public static final String ERROR_GRAPH_CODE = "5003"; //图形验证码不正确
    public static final String ERROR_SMS_CODE = "5004"; //手机验证码不正确
    public static final String ERROR_ATTACHMENT_URL_BLANK = "5005"; //附件URL为空
    public static final String ERROR_LINK_CODE = "5006"; //链接不正确
    public static final String ERROR_LINK_EXPIRED_CODE = "5007"; //链接过期
    public static final String ERROR_FIELD_NOT_EMPTY = "5008"; //字段必须为空
    public static final String ERROR_FIELD_STRING_LENGTH = "5009"; //字段的长度不符合
    public static final String ERROR_CAPTCHA_CODE = "5010"; //验证码不正确
    public static final String ERROR_FIELD_UPDATE = "5011"; //字段不能更改
    public static final String ERROR_CONFIG = "5012"; //配置异常
    public static final String ERROR_FIELD_RANGE = "5013"; //字段的值不在规定范围内

    //逻辑验证错误 6XXX
    public static final String ERROR_ACCT_NOT_EXIST_CODE = "6000"; //账号不存在
    public static final String ERROR_FIELD_EXIST_CODE = "6001"; //字段已经存在
    public static final String ERROR_DELETE_NOT_EXIST_CODE = "6003"; //删除的资源不存在
    public static final String ERROR_RESOURCE_NOT_EXIST_CODE = "6004"; //资源不存在
    public static final String ERROR_RESOURCE_HAS_CHILDREN_CODE = "6005"; //资源有子元素，无法删除
    public static final String ERROR_RESOURCE_IS_USED_CODE = "6006"; //资源已被使用，无法删除
    public static final String ERROR_CURRENT_USER_NOT_EXIST = "6007"; //当前用户不存在（token中未取到userId）
    public static final String ERROR_INVALID_STATUS_CODE = "6008"; //资源不是有效状态
    public static final String ERROR_RESOURCE_NOT_ALLOWED_DELETE = "6009"; //资源不允许被删除
    public static final String ERROR_RESOURCE_NOT_ALLOWED_UPDATE = "6010"; //资源不允许被修改
    public static final String ERROR_RESOURCE_EXIST_CODE = "6011"; //资源已经存在
    public static final String ERROR_RESOURCE_NUMBER_UPPER_LIMIT = "6012"; //资源数目上限
    public static final String ERROR_NOT_INVITED = "6013"; //用户未被邀请
    public static final String ERROR_USER_NOT_EXIST_CODE = "6013"; //用户身份(user)不存在
    public static final String ERROR_PERMISSION_NOT_EXIST_CODE = "6014"; //权限不存在
    public static final String ERROR_ROLE_NOT_EXIST_CODE = "6015"; //角色不存在
    public static final String ERROR_ROLE_HAS_USERS_CODE = "6016"; //角色下有用户，不允许删除角色
    public static final String ERROR_UPDATE_SYS_ROLE_CODE = "6017"; //系统角色，不允许删除或更新
    public static final String ERROR_CHECK_NO_CHECK_STATUS_CODE = "6018"; //记录不是待审批状态，无法进行审批
    public static final String ERROR_SPACE_IS_NOT_ACTIVATED = "6019"; //空间不是激活状态
    public static final String ERROR_TENANT_HAS_NO_SPACE_CODE = "6020"; //租户下不存在空间
    public static final String ERROR_ACCT_ID_UNMATCHED = "6021"; //账号名与账号ID不匹配
    public static final String ERROR_USER_ID_UNMATCHED = "6022"; //账号名与用户ID不匹配
    public static final String ERROR_DATA_HAS_CHILDREN_CODE = "6023"; //数据存在子节点
    public static final String ERROR_ORGANIZATION_SPACE_HAS_EXIST = "6024";//企业空间已经被注册
    public static final String ERROR_LOGIN_CONFIG_NOT_SUPPORT_EMAIL_CODE = "6030";//不支持EMAIL登录注册
    public static final String ERROR_LOGIN_CONFIG_NOT_SUPPORT_PHONE_CODE = "6031";//不支持手机登录注册
    public static final String ERROR_LOGIN_CONFIG_NOT_SUPPORT_PHONE_CODE_CODE = "6032";//不支持快速登录
    public static final String ERROR_LOGIN_CONFIG_SUPPORT_ADMIN_INVITATION_CODE = "6033";//只支持管理员邀请
    public static final String ERROR_INVITATION_ROLE_CODE = "6034";//邀请用户的身份只能是会员(member)
    public static final String ERROR_LOGIN_CONFIG_NOT_SUPPORT_INVITATION_CODE = "6035";//不支持邀请
    public static final String ERROR_LOGIN_CONFIG_NOT_SUPPORT_EMAIL_NVITATION_CODE = "6036";//不支持邮箱方式邀请
    public static final String ERROR_LOGIN_CONFIG_NOT_SUPPORT_PHONE_NVITATION_CODE = "6037";//不支持手机方式邀请
    public static final String ERROR_LOGIN_CONFIG_NOT_SUPPORT_REGISTY_CHECK_CODE = "6038";//不支持注册审核
    public static final String ERROR_CONFIRM_PASSWORD_CODE = "6039";//确认密码与密码值不相等
    public static final String ERROR_LOGIN_CONFIG_NOT_SUPPORT_NAME_CODE = "6040";//不支持用户名登录
    public static final String ERROR_PARENT_ID_NOT_EXIST_CODE = "6041";//父节点不存在
    public static final String ERROR_INVITATION_NOT_EXIST_CODE = "6042";//邀请ID不存在
    public static final String ERROR_INVITATION_DISABLE_CODE = "6043";  //邀请失效
    public static final String ERROR_SUPER_ADMIN_EXISTS_CODE = "6044";  //超级管理员已存在
    public static final String ERROR_ROLE_IS_NOT_SUPER_ADMIN_CODE = "6045";  //不是超级管理员
    public static final String ERROR_MODIFY_SUPER_ADMIN_CODE = "6046";  //不能对超级管理员进行操作
    public static final String ERROR_DISABLE_ENABLE_ROLE_CODE = "6047";  //当前的角色不能进行启用禁用操作
    public static final String ERROR_EAMIL_VERIFIED_CODE = "6048";  //用户邮箱已认证
    public static final String ERROR_CONTENT_CHANGE_STATUS_PUBLISHED_TO_DRAFT = "6049";  //不允许由发布状态修改为草稿状态
    public static final String ERROR_USER_HAS_GROUP = "6050"; // 用户已分配组织架构
    public static final String ERROR_RESOURCE_UPLOADING = "6051"; // 发布失败、资源上传中
    public static final String ERROR_EAMIL_NOT_VERIFIED_CODE = "6052";  //用户邮箱未认证
    public static final String ERROR_STATRTIME_IS_GREATER_THAN_ENDTIME = "6053"; //开始时间大于结束时间
    public static final String ERROR_LOGIN_CONFIG_NOT_SUPPORT_IC_CARD_CODE = "6054";//不支持身份证登录
    public static final String ERROR_LOGIN_CONFIG_NOT_SUPPORT_CREDIT_CODE = "6055";//不支持企业信用码登录
    public static final String ERROR_UPDATE_SELF_ROLE_CODE = "6056";//管理员不能取消自己
    public static final String ERROR_STATUS_FROM_R_TO_DA = "6057";//(审批拒绝)不能修改为(禁用)
    public static final String ERROR_ACTIVITY_TIME_GREATER_THAN_REGISTRATION_TIME = "6058";//报名时间不能大于活动时间
    public static final String ERROR_ACTIVITY_ENDED_EDIT = "6059";//活动已经结束,不能编辑
    public static final String ERROR_ACTIVITY_PROCESSING_EDIT_ONLY_EXTEND_TIME  = "6060";//活动进行中，只能延长报名时间或活动时间
    public static final String ERROR_ACTIVITY_REG_USERNAME_MUST  = "6061";//活动报名，必须输入用户名
    public static final String ERROR_ACTIVITY_REG_TEL_MUST  = "6062";//活动报名，必须输入手机
    public static final String ERROR_ACTIVITY_REG_EMAIL_MUST  = "6063";//活动报名，必须输入邮箱
    public static final String ERROR_ACTIVITY_REG_EXCEED_LIMIT_SUM  = "6064";//活动报名，不能超过总票数
    public static final String ERROR_ACTIVITY_REG_EXCEED_PER_LIMIT_SUM  = "6065";//活动报名，不能超过单人票数
    public static final String ERROR_ACTIVITY_REG_NOT_IN_REG_TIME  = "6066";//活动报名，必须在报名时间内
    public static final String ERROR_ACTIVITY_REG_CANCEL_KEY_INVALID  = "6067";//活动报名，必须在报名时间内
    public static final String ERROR_ERROR_ORDER_NO_HAS_DISABLED  = "6068";//订单已经取消，不能重复取消
    public static final String ERROR_DELETE_REASON_MAST  = "6069";//删除理由必填

    public static final String ERROR_PUBLISH  = "6070";//禁止发布
    public static final String ERROR_PUBLISH_VERSION  = "6071";//禁止发布版本
    public static final String ERROR_UN_PUBLISH = "6072";//未发布
    public static final String ERROR_APP_CONFIG = "6073"; //应用配置错误

    public static final String ERROR_APP_NOT_EXIST_CODE = "6999"; //应用不存在


}
