package com.example.yqx.smsfilter2;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;

class SmsRecevier extends BroadcastReceiver {
    public SmsRecevier() {
        Log.v("TAG", "SmsRecevier create");
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.v("TAG", "SmsRecevier onReceive");
        Object[] pdus = (Object[]) intent.getExtras().get("pdus");
        if (pdus != null && pdus.length > 0) {
            SmsMessage[] messages = new SmsMessage[pdus.length];
            for (int i = 0; i < pdus.length; i++) {
                byte[] pdu = (byte[]) pdus[i];
                messages[i] = SmsMessage.createFromPdu(pdu);
            }
            for (SmsMessage message : messages) {
                String content = message.getMessageBody();// 得到短信内容
                String sender = message.getOriginatingAddress();// 得到发信息的号码
                if (sender.equals("110")) {
                    abortBroadcast();// 中止发送
                    Log.e("TAG", "此号码为黑名单号码，已拦截!");
                }
                Date date = new Date(message.getTimestampMillis());
                SimpleDateFormat format = new SimpleDateFormat(
                        "yyyy-MM-dd HH:mm:ss");
                String sendContent = format.format(date) + ":" + sender + "--"
                        + content;
                SmsManager smsManager = SmsManager.getDefault();// 发信息时需要的
                smsManager.sendTextMessage("", null, sendContent, null,
                        null);// 转发给
                Log.v("TAG", sendContent);
            }
        }
    }
}


