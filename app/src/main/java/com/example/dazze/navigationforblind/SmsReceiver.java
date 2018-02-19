package com.example.dazze.navigationforblind;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;

/**
 * Created by yangj on 2018-02-19.
 */

public class SmsReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        String str = ""; // 출력할 문자열 저장
        if(bundle != null){ // 수신된 내용이 있으면
            Object [] pdus = (Object[])bundle.get("puds");

            SmsMessage[] msgs = new SmsMessage[pdus.length];
            for(int i=0;i<msgs.length;i++){
                msgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                str += msgs[i].getOriginatingAddress() + "에게 문자왔음 , "
                        + msgs[i].getMessageBody().toString();
            }
        }
    }
}
