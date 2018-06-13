package cn.easy.xinjing.utils;

/**
 * Created by 000404 on 2016/8/10.
 */
public class Constants {
    public static final String API_USER_KEY = "api_user";

    public static final String DISEASE_STATUS_ENUM_KEY = "DISEASE_STATUS_ENUM";
    public static final int DISEASE_STATUS_INIT = 1;
    public static final int DISEASE_STATUS_PUBLISH = 2;
    public static final int DISEASE_STATUS_NO_PUBLISH = 3;

    public static final String THERAPY_STATUS_ENUM_KEY = "THERAPY_STATUS_ENUM";
    public static final int THERAPY_STATUS_INIT = 1;
    public static final int THERAPY_STATUS_PUBLISH = 2;
    public static final int THERAPY_STATUS_NO_PUBLISH = 3;

    public static final String VR_ROOM_STATUS_ENUM_KEY = "VR_ROOM_STATUS_ENUM";
    public static final String VR_ROOM_APPOINTMENT_STATUS_KEY = "VR_ROOM_APPOINTMENT_STATUS_ENUM";
    //预约成功
    public static final int VR_ROOM_APPOINTMENT_STATUS_ORDER = 1;
    //取消预约
    public static final int VR_ROOM_APPOINTMENT_STATUS_CANCEL_ORDER = 2;
    //已完成
    public static final int VR_ROOM_APPOINTMENT_STATUS_FINISH = 3;

    public static final String VR_ROOM_APPOINTMENT_TYPE_ENUM_KEY = "VR_ROOM_APPOINTMENT_TYPE_ENUM";

    public static final String DOCTOR_STATUS_ENUM_KEY = "DOCTOR_STATUS_ENUM";
    public static final int DOCTOR_STATUS_INIT = 1;
    public static final int DOCTOR_STATUS_TO_CHECK = 2;
    public static final int DOCTOR_STATUS_NO_PASS = 3;
    public static final int DOCTOR_STATUS_NORMAL = 4;
    public static final int DOCTOR_STATUS_STOP = 9;

    /**
     * 医生文章状态 1:草稿\r\n2:待审核\r\n3:审核不通过 4:审核通过
     */
    public static final String DOCTOR_ARTICLE_STATUS_ENUM_KEY = "DOCTOR_ARTICLE_STATUS_ENUM";
    public static final int DOCTOR_ARTICLE_STATUS_DRAFT = 1;
    public static final int DOCTOR_ARTICLE_STATUS_TO_CHECK = 2;
    public static final int DOCTOR_ARTICLE_STATUS_NO_PASS = 3;
    public static final int DOCTOR_ARTICLE_STATUS_PASSED = 4;

    /**
     * 内容管理类型枚举
     */
    public static final String CONTENT_TYPE_KEY = "CONTENT_TYPE_ENUM";
    public static final int CONTENT_TYPE_VIDEO = 1;
    public static final int CONTENT_TYPE_AUDIO = 2;
    public static final int CONTENT_TYPE_PIC = 3;
    public static final int CONTENT_TYPE_ARTICLE = 4;
    public static final int CONTENT_TYPE_GAME = 6;
    public static final int CONTENT_TYPE_OUTSIDE_GAME = 7;
    public static final int CONTENT_TYPE_COMBO = 11;
    /**
     * 内容管理状态枚举
     */
    public static final String CONTENT_STATUS_KEY = "CONTENT_STATUS_ENUM";
    public static final int CONTENT_STATUS_INIT = 1;
    public static final int CONTENT_STATUS_PUBLISH = 2;
    public static final int CONTENT_STATUS_NO_PUBLISH = 3;

    public static final int USER_TYPE_DOCTOR = 1;
    public static final int USER_TYPE_PATIENT = 2;
    public static final int USER_TYPE_VR_ROOM = 3;
    public static final int USER_TYPE_APP_VR_ROOM = 4;
    public static final int USER_TYPE_APP_CONTROL_VR_ROOM = 5;
    public static final int USER_TYPE_APP_VR_ROOM_CASE = 6;
    public static final int USER_TYPE_CONTROLDOCTOR_APP = 7;
    public static final String CAPTCHA_KEY = "captcha";

    public static final String COMMENT_TYPE_KEY = "COMMENT_TYPE_ENUM";
    public static final String COMMENT_STATUS_KEY = "COMMENT_STATUS_ENUM";
    public static final int COMMENT_TYPE_DOCTOR = 1;//医生
    public static final int COMMENT_TYPE_CONTENT = 2;//内容
    public static final int COMMENT_STATUS_ONLY_SELF = 1;
    public static final int COMMENT_STATUS_ALL_CANSEE = 2;
    public static final int COMMENT_STATUS_NONE_CANSEE = 3;

    /**
     * 订单状态
     */
    public static final String ORDER_STATUS_KEY = "ORDER_STATUS_ENUM";

    public static final int ORDER_STATUS_PROCESSING = 1;

    public static final int ORDER_STATUS_SUCCESS = 2;

    public static final int ORDER_STATUS_FAIL = 3;

    /**
     * 处方状态牧举
     */
    public static final String PRESCRIPTION_STATUS_KEY = "PRESCRIPTION_STATUS_ENUM";
    public static final int PRESCRIPTION_PAY_STATUS_UNPAY = 1;
    public static final int PRESCRIPTION_PAY_STATUS_PAID = 2;
    public static final int PRESCRIPTION_PAY_STATUS_PAIDOFF = 4;

    public static final String PRESCRIPTION_PAY_STATUS_KEY = "PRESCRIPTION_PAY_STATUS_ENUM";
    public static final int PRESCRIPTION_STATUS_INIT = 1;
    public static final int PRESCRIPTION_STATUS_IN_PROGRESS = 2;
    public static final int PRESCRIPTION_STATUS_FINISH = 3;
    public static final int PRESCRIPTION_STATUS_SUSPEND = 9;


    public static final int PRESCRIPTION_SOURCE_ONLINE = 1;
    public static final int PRESCRIPTION_SOURCE_OFFLINE = 2;
    public static final int PRESCRIPTION_SOURCE_OFFLINE2 = 3;//院内医生线下

    /**
     * 订单商品类型
     */
    public static final String ORDER_OBJECT_TYPE_PRESCRIPTION = "1";

    /**
     * 科室状态
     */
    public static final int SECTION_OFFICE_STATUS_INIT = 1;
    public static final int SECTION_OFFICE_STATUS_PUBLISH = 2;
    public static final int SECTION_OFFICE_STATUS_NO_PUBLISH = 3;

    public static final int YES = 1;
    public static final int NO = 0;

    /**
     * 科室状态枚举
     */
    public static final String SECTION_OFFICE_STATUS_ENUM_KEY = "SECTION_OFFICE_STATUS_ENUM";
    public static final int SECTION_OFFICE_STATUS_STATUS_INIT = 1;
    public static final int SECTION_OFFICE_STATUS_STATUS_PUBLISH = 2;
    public static final int SECTION_OFFICE_STATUS_STATUS_UN_PUBLISH = 3;

    /**
     * APP客户端枚举
     */
    public static final String APP_CLIENT_KEY = "APP_CLIENT_ENUM";
    public static final String APP_CLIENT_DOCTOR_IOS = "DOCTOR_IOS";
    public static final String APP_CLIENT_DOCTOR_ANDROID = "DOCTOR_ANDROID";
    public static final String APP_CLIENT_PATIENT_IOS = "PATIENT_IOS";
    public static final String APP_CLIENT_PATIENT_ANDROID = "PATIENT_ANDROID";
    public static final String APP_CLIENT_VR_ANDROID = "VR_ANDROID";

    public static final String APP_CLIENT_CONTROL_VR_ANDROID = "VR_CONTROL_ANDROID";
    public static final String APP_CLIENT_CONTROL_VR_IOS = "VR_CONTROL_IOS";

    public static final String VR_CONTROL_DOCTORANDROID = "VR_CONTROL_DOCTORANDROID";//VR室医生控制端安卓APP
    public static final String VR_CONTROL_DOCTORIOS = "VR_CONTROL_DOCTORIOS";//VR室医生控制端苹果APP

    /**
     * 客户端系统版本
     */
    public static final String SYSTEM_VERSION_KEY = "SYSTEM_VERSION_ENUM";
    public static final String SYSTEM_VERSION_IOS = "1";
    public static final String SYSTEM_VERSION_ANDROID = "2";

    public static final int APP_TOKEN_STATUS_VALID = 1;
    public static final int APP_TOKEN_STATUS_INVALID = -1;
    /*职称*/
    public static final String PROFESSIONAL_TITLES_ENUM_KEY = "PROFESSIONAL_TITLES_ENUM";

    public static final int VR_ROOM_APP_TASK_STATUS_INIT = 1;
    public static final int VR_ROOM_APP_TASK_STATUS_PLAY = 2;
    public static final int VR_ROOM_APP_TASK_STATUS_END = 3;

    public static final int VR_ROOM_APP_TASK_TYPE_PLAY = 1;
    public static final int VR_ROOM_APP_TASK_TYPE_END = 2;

    public static final String USER_DOCTOR_APPROVE_STATUS_ENUM_KEY = "USER_DOCTOR_APPROVE_STATUS_ENUM";
    public static final int USER_DOCTOR_APPROVE_STATUS_TO_CHECK = 2;
    public static final int USER_DOCTOR_APPROVE_STATUS_NOT_PASS = 3;
    public static final int USER_DOCTOR_APPROVE_STATUS_PASS = 4;

    //广告轮播相关
    public static final String ADVERTISEMENT_TYPE_KEY = "ADVERTISEMENT_TYPE_ENUM";
    public static final String ADVERTISEMENT_STATUS_KEY = "ADVERTISEMENT_STATUS_ENUM";
    public static final int ADVERTISEMENT_STATUS_INIT = 1;
    public static final int ADVERTISEMENT_STATUS_PUBLISH = 2;

    public static final String USER_VR_ROOM_TYPE_KEY = "USER_VR_ROOM_TYPE_ENUM";
    public static final int USER_VR_ROOM_TYPE_DESKTOP = 1;
    public static final int USER_VR_ROOM_TYPE_APP_CONTROL = 2;
    public static final int USER_VR_ROOM_TYPE_APP = 3;

    public static final String CAPITAL_FLOW_FEE_TYPE_KEY = "CAPITAL_FLOW_FEE_TYPE_ENUM";
    public static final int CAPITAL_FLOW_FEE_TYPE_RECHARGE = 1;
    public static final int CAPITAL_FLOW_FEE_TYPE_WITHDRAW = 2;
    public static final int CAPITAL_FLOW_FEE_TYPE_CONSULTATE_FEE = 3;
    public static final int CAPITAL_FLOW_FEE_TYPE_PRESCRIPTION_FEE = 4;
    public static final int CAPITAL_FLOW_FEE_TYPE_OTHER = 5;

    public static final String CAPITAL_FLOW_TYPE_KEY = "CAPITAL_FLOW_TYPE_ENUM"; //收支类型
    public static final int CAPITAL_FLOW_TYPE_INCOME = 1;
    public static final int CAPITAL_FLOW_TYPE_EXPEND = 2;

    public static final String CAPITAL_FLOW_STATUS_KEY = "CAPITAL_FLOW_STATUS_ENUM";
    public static final int CAPITAL_FLOW_STATUS_PROCESSING = 1;
    public static final int CAPITAL_FLOW_STATUS_SUCCESS = 2;
    public static final int CAPITAL_FLOW_STATUS_FAIL = 3;

    public static final String PAY_WAY_KEY = "PAY_WAY_ENUM";
    public static final int PAY_WAY_WEIXIN = 1;
    public static final int PAY_WAY_ALIPAY = 2;


    public static final String PRESCRIPTION_CONTENT_STATUS_KEY = "PRESCRIPTION_CONTENT_STATUS_ENUM";
    public static final int PRESCRIPTION_CONTENT_STATUS_INIT = 1;
    public static final int PRESCRIPTION_CONTENT_STATUS_IN_PROGRESS = 2;
    public static final int PRESCRIPTION_CONTENT_STATUS_FINISH = 3;
    public static final int PRESCRIPTION_CONTENT_STATUS_SUSPEND = 9;

    /**
     * 医院管理的状态 fanchengzhi
     */
    public static final String USER_HOSPITAL_APPROVE_STATUS_ENUM = "USER_HOSPITAL_APPROVE_STATUS_ENUM";
    /**
     * 心景头条的状态 fanchengzhi
     */
    public static final String USER_FRONTPAGE_APPROVE_STATUS_ENUM = "USER_FRONTPAGE_APPROVE_STATUS_ENUM";
}
