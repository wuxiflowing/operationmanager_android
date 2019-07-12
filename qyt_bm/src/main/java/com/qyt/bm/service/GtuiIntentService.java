package com.qyt.bm.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.gson.Gson;
import com.igexin.sdk.GTIntentService;
import com.igexin.sdk.PushManager;
import com.igexin.sdk.message.GTCmdMessage;
import com.igexin.sdk.message.GTNotificationMessage;
import com.igexin.sdk.message.GTTransmitMessage;
import com.qyt.bm.R;
import com.qyt.bm.activity.InstallTaskDetailActivity;
import com.qyt.bm.activity.MainActivity;
import com.qyt.bm.activity.RepairTaskDetailActivity;
import com.qyt.bm.comm.Constants;
import com.qyt.bm.utils.SharedPref;

import java.util.Random;

/**
 * Created by Administrator on 2018-8-17 0017.
 */

public class GtuiIntentService extends GTIntentService {

    public GtuiIntentService() {

    }

    @Override
    public void onReceiveServicePid(Context context, int pid) {
    }

    @Override
    public void onReceiveMessageData(Context context, GTTransmitMessage msg) {
        String taskid = msg.getTaskId();
        String messageid = msg.getMessageId();
        byte[] payload = msg.getPayload();
        // 第三方回执调用接口，actionid范围为90000-90999，可根据业务场景执行
        boolean result = PushManager.getInstance().sendFeedbackMessage(context, taskid, messageid, 90001);
        if (result && payload != null) {
            String data = new String(payload);
            Log.e(TAG, "receiver payload = " + data);
            MessageModel messageModel = new Gson().fromJson(data, MessageModel.class);
            showNotification(context, messageModel);
        }
    }

    private void showNotification(Context mContext, MessageModel messageModel) {
        // 获取通知服务对象NotificationManager
        NotificationManager notiManager = (NotificationManager)
                getSystemService(mContext.NOTIFICATION_SERVICE);
        // 创建Notification对象
        NotificationCompat.Builder builder = new NotificationCompat.Builder(mContext);
        builder.setContentTitle(messageModel.title)    // 通知标题
                .setContentText(messageModel.description)  // 通知内容
                .setSmallIcon(R.drawable.push_small);    // 通知小图标
        builder.setDefaults(Notification.DEFAULT_SOUND);    // 设置声音/震动等
        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                0,   //请求码
                getSkipIntent(mContext, messageModel), //意图对象
                PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pendingIntent);
        builder.setAutoCancel(true);
        Notification notification = builder.build();
        // 设置通知的点击行为：自动取消/跳转等
        // 通过NotificationManager发送通知
        notiManager.notify(new Random().nextInt(), notification);
    }

    private Intent getSkipIntent(Context context, MessageModel item) {
        Intent intent = new Intent();
        switch (item.model) {
            case "install":
                intent.setClass(context, InstallTaskDetailActivity.class);
                break;
            case "repair":
                intent.setClass(context, RepairTaskDetailActivity.class);
                break;
            default:
                intent.setClass(context, MainActivity.class);
        }
        Bundle bundle = new Bundle();
        bundle.putString(Constants.INTENT_OBJECT, item.modelVal);
        bundle.putString(Constants.INTENT_FLAG, "prepare");
        intent.putExtras(bundle);
        return intent;
    }

    @Override
    public void onReceiveClientId(Context context, String clientid) {
        Log.e(TAG, "onReceiveClientId -> " + "clientid = " + clientid);
        SharedPref sharedPref = new SharedPref(context);
        sharedPref.setString(Constants.GETUI_CLIENTID, clientid);
    }

    @Override
    public void onReceiveOnlineState(Context context, boolean online) {
    }

    @Override
    public void onReceiveCommandResult(Context context, GTCmdMessage cmdMessage) {
    }

    @Override
    public void onNotificationMessageArrived(Context context, GTNotificationMessage msg) {
    }

    @Override
    public void onNotificationMessageClicked(Context context, GTNotificationMessage msg) {
    }
}
