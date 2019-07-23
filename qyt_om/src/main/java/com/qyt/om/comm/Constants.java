package com.qyt.om.comm;

import android.content.Context;
import android.text.TextUtils;

import com.bumptech.glide.request.RequestOptions;
import com.qyt.om.R;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by 任瑞刚
 * <p>
 * 应用常量定义类
 */
public class Constants {

    //配置数据
    public static final int SPLASH_TIME = 1000;//启动页停留时间
    public static final int PAGE_SIZE = 10;
    //存储数据
    public static final String LOGIN_ID = "login_id";  //登录id
    public static final String LOGIN_MOBILE = "login_mobile";  //登录手机号
    public static final String USER_TOKEN = "user_token";  //token信息
    public static final String USER_INFO = "user_info";  //用户信息
    public static final String GETUI_CLIENTID = "gt_clientid";  //个推ClientId
    public static final String VERSIONCODE = "versioncode";  //引导页
    public static final String IGNORE_VERSION = "ignore_version";  //忽略版本更新
    public static final String BIND_DEVICE = "bind_device";  //绑定设备名称
    //页面跳转参数
    public static final String INTENT_OBJECT = "intent_object";  //页面跳转参数
    public static final String INTENT_FLAG = "intent_flag";  //页面跳转参数

    /**
     * 设备类型
     */
    public static final String DEVICE_TYPE_KD326 = "KD326";
    public static final String DEVICE_TYPE_QY601 = "QY601";

    public static final RequestOptions REQUEST_OPTIONS = new RequestOptions().placeholder(R.mipmap.contacts_header).error(R.mipmap.contacts_header);

    public static final HashMap<String, Integer> TASK_ICONS = new HashMap<String, Integer>() {
        {
            put("install", R.mipmap.icon_message_install);
            put("maintain", R.mipmap.icon_message_stick);
            put("repair", R.mipmap.icon_message_repair);
            put("recycling", R.mipmap.icon_message_return);
            put("", R.mipmap.icon_message_notice);
            put("sys", R.mipmap.icon_message_system);
            put("", R.mipmap.icon_message_back);
            put("warning", R.mipmap.icon_message_alarm);
        }
    };

    public static final HashMap<String, String> TASK_TITLES = new HashMap<String, String>() {
        {
            put("install", "设备安装");
            put("maintain", "定期维护");
            put("repair", "故障报修");
            put("recycling", "设备回收");
            put("", "消息提醒");
            put("sys", "系统消息");
            put("", "还款提醒");
            put("warning", "告警通知");
        }
    };
    /*设备状态描述*/
    public static final HashMap<Integer, String> DEVICE_STATE = new HashMap<Integer, String>() {
        {
            put(-1, "数据解析异常");
            put(0, "正常");
            put(1, "告警限1");
            put(2, "告警限2");
            put(3, "不在线告警");
            put(4, "超过上下限告警");
        }
    };

    public static String showDeviceStatus(Context context, String workStatus) {
        if (TextUtils.isEmpty(workStatus)) {
            return "--";
        }
        if (!workStatus.contains(",")) {
            return deviceStatus(context, workStatus);
        }
        String[] status = workStatus.split(",");
        StringBuilder stringBuilder = new StringBuilder();
        for (String state : status) {
            stringBuilder.append(deviceStatus(context, state));
        }
        int index = stringBuilder.lastIndexOf("、");
        if (index > 0) {
            return stringBuilder.subSequence(0, index).toString();
        }
        return "--";
    }

    private static String deviceStatus(Context context, String workstatus) {
        switch (workstatus) {
            case "-1": //数据解析异常(-1)
                return "--";
            case "0": //正常(0)
                return context.getString(R.string.text_normal);
            case "3": //不在线告警(3)
                return context.getString(R.string.status_device_offline);
            case "1": //告警限1(1)
                return context.getString(R.string.status_data_alarm1);
            case "2": //告警限2(2)
                return context.getString(R.string.status_data_alarm2);
            case "5": //设备告警(5)
                return context.getString(R.string.status_device_alarm);
            case "10": //断电告警(10)
                return context.getString(R.string.status_outages_alarm);
            default:
                return context.getString(R.string.text_normal);
        }
    }
}
