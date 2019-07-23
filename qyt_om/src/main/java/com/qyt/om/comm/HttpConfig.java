package com.qyt.om.comm;

import com.qyt.om.BuildConfig;

/**
 * Created by 任瑞刚
 * <p>
 * 接口定义类
 */
public class HttpConfig {

    //    private static final String HTTP_HOST = "http://api.celefish.com:8080";
    private static final String HTTP_HOST = "http://apitest.celefish.com:8080";
    /*版本更新*/
    public static final String VERSION_UPDATE = HTTP_HOST + "/RESTAdapter/checkAPP/Dq1k4xjV0x8IAxgWzQLo54";
    /*登录*/
    public static final String LOGIN = HTTP_HOST + "/RESTAdapter/app/login";
    /*修改密码*/
    public static final String UPDATE_PW = HTTP_HOST + "/RESTAdapter/app/updatePW";
    /*消息信息*/
    public static final String MESSAGE_LIST = HTTP_HOST + "/RESTAdapter/app/mytask/{loginid}/messageList";
    /*查询任务消息*/
    public static final String TASK_MESSAGE_LIST = HTTP_HOST + "/RESTAdapter/app/mytask/{loginid}/taskMessageList";
    /*更新任务消息*/
    public static final String ISREAD_SYS_MESSAGE = HTTP_HOST + "/RESTAdapter/app/mytask/{loginid}/{type}/isRead";
    /*养殖户信息*/
    public static final String CUSTOMER_LIST = HTTP_HOST + "/RESTAdapter/app/mytask/{loginid}/customerList";
    /*省市区列表*/
    public static final String CITY_AREA_LIST = HTTP_HOST + "/RESTAdapter/app/formData/root/areaList";
    /*工作Banner图*/
    public static final String BANNER_LIST = HTTP_HOST + "/RESTAdapter/app/getBanner";
    /*获取任务数量*/
    public static final String TASK_COUNT_LIST = HTTP_HOST + "/RESTAdapter/app/mytask/{loginid}/taskList";
    /*查询安装任务，install、repair、recycling*/
    public static final String TASK_INSTALL_LIST = HTTP_HOST + "/RESTAdapter/mytask/{loginid}/list";
    /*查询任务详情*/
    public static final String TASK_DETAIL = HTTP_HOST + "/RESTAdapter/mytask/{taskid}/data";
    /*确认接单*/
    public static final String TASK_CONFIRM = HTTP_HOST + "/RESTAdapter/mytask/{taskid}/getTask";
    /*获取设备配置*/
    public static final String DEVICE_TEST_CONFIG = HTTP_HOST + "/RESTAdapter/device/{id}";
    /*设备是否安装判断*/
    public static final String DEVICE_INSTALL_CHECK = HTTP_HOST + "/RESTAdapter/app/device/{id}/deviceCheck";
    /*获取设备配置,带鱼塘*/
    public static final String DEVICE_TEST_RAWDATA = HTTP_HOST + "/RESTAdapter/device/{id}/rawdata";
    /*获取设备感测资料*/
    public static final String GET_DEVICE_RAWDATA = HTTP_HOST + "/v1/device/identifier/{id}/rawdata";
    /*测试在线*/
    public static final String DEVICE_TEST_CONNECT = HTTP_HOST + "/RESTAdapter/app/device/{id}/onlineCheck";
    /*测试开关*/
    public static final String DEVICE_TEST_SWITCH = HTTP_HOST + "/RESTAdapter/app/device/{id}/updateCommand";
    /*设备校准*/
    public static final String DEVICE_TEST_RESET = HTTP_HOST + "/RESTAdapter/app/device/{id}/reset/install";
    /*故障原因*/
    public static final String REPAIR_ISSUE_LIST = HTTP_HOST + "/RESTAdapter/app/formData/issueList/repair";
    /*故障维护内容*/
    public static final String REPAIR_CONTEXT_LIST = HTTP_HOST + "/RESTAdapter/app/formData/repairList/repair";
    /*定期维护内容*/
    public static final String MAINTAIN_CONTEXT_LIST = HTTP_HOST + "/RESTAdapter/app/formData/repairList/maintain";
    /*获取养殖户鱼塘信息*/
    public static final String GET_CUSTOMER_PONDS = HTTP_HOST + "/RESTAdapter/app/mytask/{customerId}/pondData";
    /*图片上传*/
    public static final String UPLOAD_IMAGE = HTTP_HOST + "/RESTAdapter/{loginid}/uploadImage";
    /*提交任务工单*/
    public static final String CONFIRM_TASK_FINISH = HTTP_HOST + "/RESTAdapter/device/{taskid}/submit";
    /*更新任务消息*/
    public static final String ISREAD_MESSAGE = HTTP_HOST + "/RESTAdapter/app/mytask/{msgId}/isRead";
    /*运维管家鱼塘设备*/
    public static final String MAINTAINKEEPER_DEVICE_LIST = HTTP_HOST + "/RESTAdapter/app/mytask/{maintainKeeperID}/maintainKeeperID/pondData/devDetail";
    /*设备回收*/
    public static final String DEL_UNBIND_SERVICE = HTTP_HOST + "/RESTAdapter/app/device/{dIdentifier}/recycle/from/group/{gIdentifier}";
    /*意见反馈*/
    public static final String POST_APPCSM = HTTP_HOST + "/RESTAdapter/app/createAppCSM";
    /*任务查询*/
    public static final String GET_QUERY_TASK = HTTP_HOST + "/RESTAdapter/queryTask/{loginid}/list/";

    /*获取新设备QY601配置*/
    public static final String DEVICE_TEST_CONFIG_NEW = HTTP_HOST + "/RESTAdapter/device/new/{id}";
//    /*鱼塘基础信息列表*/
//    public static final String PONDS_INFO_LIST = HTTP_HOST + "/RESTAdapter/app/mytask/{customerId}/customerPondData";

    /*查询新设备信息、包含鱼塘信息*/
    public static final String DEVICE_DETAIL = HTTP_HOST + "/RESTAdapter/newDevice/{identifierID}/rawdata";
    /*鱼塘基础信息列表(运维管家ID)*/
    public static final String PONDS_INFO_LIST = HTTP_HOST + "/RESTAdapter/app/mytask/{maintainKeeperID}/maintainKeeperID/pondData/newDevDetail";
    /*鱼塘基础信息列表(运维管家ID和搜索条件)*/
    public static final String PONDS_INFO_LIST_FILTER = HTTP_HOST + "/RESTAdapter/app/mytask/{maintainKeeperID}/maintainKeeperID/pondData/newDevDetail/{queryValue}";
    /*存储新设备信息,设备配置使用*/
    public static final String DEVICE_TEST_CONFIG2 = HTTP_HOST + "/RESTAdapter/newDevice/{identifierID}";

    /*添加联系人*/
    public static final String LINKMAN_ADD = HTTP_HOST + "/RESTAdapter/linkMan/{farmerId}/save";
    /*查询联系人列表*/
    public static final String LINKMAN_GET = HTTP_HOST + "/RESTAdapter/linkMan/farmerId/{farmerId}";
    /*设备连线方式配对*/
    public static final String DEVICE_LINK_PAIR = HTTP_HOST + "/RESTAdapter/app/device/{identifierID}/devicePair";
    /*打开设备某一路开关*/
    public static final String DEVICE_CONTROL_SWITCH = HTTP_HOST + "/v1/deviceMonitor/core/device/identifier/{identifier}/control/{cch}/switch/{state}";
    /*查询鱼塘的联系人*/
    public static final String LINKMAN_ADDED_POND = HTTP_HOST + "/RESTAdapter/linkMan/deviceId/{deviceId}";
    /*QY601校准*/
    public static final String DEVICE_NEW_RESET = HTTP_HOST + "/RESTAdapter/app/newDevice/{identifierID}/reset/install";
    /*设备连线方式切换*/
    public static final String DEVICE_CONNECT_TYPE_CHANGE = HTTP_HOST + "/RESTAdapter/app/device/{identifierID}/deviceModeChange/ch/{ch}/pairType/{pairType}";


}
