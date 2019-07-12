package com.qyt.bm.comm;

import com.bumptech.glide.request.RequestOptions;
import com.qyt.bm.R;

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
    public static final String USER_TOKEN = "user_token";  //token信息
    public static final String USER_INFO = "user_info";  //用户信息
    public static final String GETUI_CLIENTID = "gt_clientid";  //个推ClientId
    //页面跳转参数
    public static final String INTENT_OBJECT = "intent_object";  //页面跳转参数
    public static final String INTENT_FLAG = "intent_flag";  //页面跳转参数
    public static final String LETTER_LIST = "#ABCDEFGHIJKLMNOPQRSTUVWXYZ";  //26字母导航
    public static final String CUSTOMER_CREATE = "PRO00001527661500018";//开户
    public static final String CUSTOMER_SIGN = "PRO00221527906936118";//签约
    public static final String INSTALL_CREATE = "PRO00011527662267836";//安装
    public static final String REPAIR_CREATE = "PRO00281532487186962";//报修
    public static final String RECYCLE_CREATE = "PRO00031527817388054";//回收
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
}
