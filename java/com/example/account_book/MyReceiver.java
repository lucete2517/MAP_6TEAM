package com.example.account_book;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MyReceiver extends BroadcastReceiver {

    private static final String TAG = "MyReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "onReceive() called");

        Bundle bundle = intent.getExtras();
        SmsMessage[] messages = parseSmsMessage(bundle);

        if(messages.length > 0){
            String sender = messages[0].getOriginatingAddress();
            String content = messages[0].getMessageBody().toString();
            Date date = new Date(messages[0].getTimestampMillis());

            Log.d(TAG, "sender: " + sender);
            Log.d(TAG, "content: " + content);
            Log.d(TAG, "date: " + date);
        }
    }

    private SmsMessage[] parseSmsMessage(Bundle bundle){
        // PDU: Protocol Data Units
        Object[] objs = (Object[]) bundle.get("pdus");
        SmsMessage[] messages = new SmsMessage[objs.length];

        for(int i=0; i<objs.length; i++){
            messages[i] = SmsMessage.createFromPdu((byte[])objs[i]);
        }

        return messages;
    }

    private static SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private void sendToActivity(Context context, String sender, String content, Date date){
        Intent intent = new Intent(context, SmsActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                |Intent.FLAG_ACTIVITY_SINGLE_TOP
                |Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("sender", sender);
        intent.putExtra("content", content);
        intent.putExtra("date", format.format(date));
        context.startActivity(intent);
    }

//    private void requirePerms(){
//        String[] permissions = {Manifest.permission.RECEIVE_SMS};
//        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS);
//        if(permissionCheck == PackageManager.PERMISSION_DENIED){
//            ActivityCompat.requestPermissions(this, permissions, 1);
//        }
//    }
}