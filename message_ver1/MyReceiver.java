package com.example.message;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;


public class MyReceiver extends BroadcastReceiver {
    private static final String SMS_RECEIVED = "android.provider.Telephony.SMS_RECEIVED";
    private static final String TAG = "SmsReceiver";
    private static SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    public void onReceive(Context context, Intent intent) {
        if(intent.getAction().equals(SMS_RECEIVED)) {
            Log.d(TAG, "onReceiver()호출"); // Bundle을 이용해서 메세지 내용 가져옴

            Bundle bundle = intent.getExtras();
            SmsMessage[] messages = parseSmsMessage(bundle); // 메세지가 있을 경우 내용을 로그로 출력
            if (messages.length > 0) { // 메세지 내용 가져오기
                String sender = messages[0].getOriginatingAddress();
                String contents = messages[0].getMessageBody().toString();
                Date receiveDate = new Date(messages[0].getTimestampMillis());

                sendToActivity(context, sender, contents, receiveDate); // 액티비티로 메세지 내용 전달
            }
        }
    }

    private void sendToActivity(Context context, String sender, String contents, Date receivedDate) {
        Intent intent = new Intent(context, MainActivity.class);

        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK| Intent.FLAG_ACTIVITY_SINGLE_TOP| Intent.FLAG_ACTIVITY_CLEAR_TOP);
        // FLAG 설정

        intent.putExtra("sender", sender);
        intent.putExtra("contents", contents);
        intent.putExtra("receivedDate", format.format(receivedDate));
        // 메세지 내용을 Extra에 삽입

        context.startActivity(intent);
    }

    private SmsMessage[] parseSmsMessage(Bundle bundle) {
        Object[] ob = (Object[]) bundle.get("pdus");
        SmsMessage[] messages = new SmsMessage[ob.length];

        for(int i= 0; i < ob.length; i++) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                String format = bundle.getString("format");
                messages[i] = SmsMessage.createFromPdu((byte[]) ob[i], format);
            } else {
                messages[i] = SmsMessage.createFromPdu((byte[]) ob[i]);
            }
        }

        return messages;
    }
}
