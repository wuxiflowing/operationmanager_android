package com.qyt.bm.comm;

import com.qyt.bm.BuildConfig;

/**
 * Created by 任瑞刚
 * <p>
 * 接口定义类
 */
public class HttpConfig {

    //private static final String HTTP_HOST = "http://api.celefish.com:8080";
    private static final String HTTP_HOST = "http://apitest.celefish.com:8080";
    /*版本更新*/
    public static final String VERSION_UPDATE = HTTP_HOST + "/RESTAdapter/checkAPP/rBJkrQ87lHA8WB8q2KTwg5";
    /*登录*/
    public static final String LOGIN = HTTP_HOST + "/RESTAdapter/app/login";
    /*修改密码*/
    public static final String UPDATE_PW = HTTP_HOST + "/RESTAdapter/app/updatePW";
    /*消息信息*/
    public static final String MESSAGE_LIST = HTTP_HOST + "/RESTAdapter/app/mytask/{loginid}/messageList";
    /*查询任务消息*/
    public static final String TASK_MESSAGE_LIST = HTTP_HOST + "/RESTAdapter/app/mytask/{loginid}/taskMessageList";
    /*养殖户信息*/
    public static final String CUSTOMER_LIST = HTTP_HOST + "/RESTAdapter/app/mytask/{loginid}/customerList";
    /*省市区列表*/
    public static final String CITY_AREA_LIST = HTTP_HOST + "/RESTAdapter/app/formData/{areaTypeID}/areaList";
    /*工作Banner图*/
    public static final String BANNER_LIST = HTTP_HOST + "/RESTAdapter/app/getBanner";
    /*获取任务数量*/
    public static final String TASK_COUNT_LIST = HTTP_HOST + "/RESTAdapter/app/mytask/{loginid}/taskList";
    /*查询安装任务*/
    public static final String TASK_INSTALL_LIST = HTTP_HOST + "/RESTAdapter/mytask/{loginid}/list";
    /*查询安装任务详情*/
    public static final String TASK_INSTALL_DETAIL = HTTP_HOST + "/RESTAdapter/mytask/{taskid}/data";
    /*图片上传*/
    public static final String UPLOAD_IMAGE = HTTP_HOST + "/RESTAdapter/{loginid}/uploadImage";
    /*提交开户资料，任务创建*/
    public static final String CREATE_TASK_CUSTOMER = HTTP_HOST + "/RESTAdapter/app/mytask/createTask/{PROID}";
    /*获取养殖户相关信息*/
    public static final String GET_CUSTOMER_DATA = HTTP_HOST + "/RESTAdapter/app/mytask/{customerId}/customerData";
    /*获取养殖户鱼塘信息*/
    public static final String GET_CUSTOMER_PONDS = HTTP_HOST + "/RESTAdapter/app/mytask/{customerId}/pondData";
    /*获取设备清单*/
    public static final String INSTALL_DEVICE_LIST = HTTP_HOST + "/RESTAdapter/app/device/list/install";
    /*运维人员列表*/
    public static final String OPERATOR_LIST = HTTP_HOST + "/RESTAdapter/app/maMember/{loginid}";
    /*获取养殖户合同*/
    public static final String CONTRACT_LIST = HTTP_HOST + "/RESTAdapter/app/mytask/contractList/farmerId/{farmerId}";
    /*更新任务消息*/
    public static final String ISREAD_MESSAGE = HTTP_HOST + "/RESTAdapter/app/mytask/{msgId}/isRead";
    /*更新任务消息*/
    public static final String ISREAD_SYS_MESSAGE = HTTP_HOST + "/RESTAdapter/app/mytask/{loginid}/{type}/isRead";
    /*获取设备配置*/
    public static final String DEVICE_TEST_CONFIG = HTTP_HOST + "/RESTAdapter/device/{id}";
    /*获取设备配置,带鱼塘*/
    public static final String DEVICE_TEST_RAWDATA = HTTP_HOST + "/RESTAdapter/device/{id}/rawdata";
    /*获取设备感测资料*/
    public static final String GET_DEVICE_RAWDATA = HTTP_HOST + "/v1/device/identifier/{id}/rawdata";
    /*获取回收原因*/
    public static final String GET_RECYCLING_DATA = HTTP_HOST + "/RESTAdapter/app/formData/recycleList/recycling";
    /*意见反馈*/
    public static final String POST_APPCSM = HTTP_HOST + "/RESTAdapter/app/createAppCSM";
    /*提交任务工单*/
    public static final String CONFIRM_TASK_FINISH = HTTP_HOST + "/RESTAdapter/device/{taskid}/submit";
    /*电话重复验证*/
    public static final String CHECKPHONENUMBER = HTTP_HOST + "/RESTAdapter/app/checkPhoneNumber/";
    /*身份证重复验证*/
    public static final String CHECKIDCARD = HTTP_HOST + "/RESTAdapter/app/checkIdCard/";
    /*任务查询*/
    public static final String GET_QUERY_TASK = HTTP_HOST + "/RESTAdapter/queryTask/{loginid}/list/";
}
