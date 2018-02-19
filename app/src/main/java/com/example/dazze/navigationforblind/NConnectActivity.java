package com.example.dazze.navigationforblind;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

//보호자 사용자 연동 액티비티
public class NConnectActivity extends AppCompatActivity {

    private EditText editUphone;
    private Button btnSendMessage;
    final SmsManager sms = SmsManager.getDefault();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nconnect);

        /*
        1. 연동할 보호자의 핸드폰 번호를 입력
        2. 해당 번호로 초대번호 문자 전송
         */

        Intent intent=getIntent();
        final String inviteCode=intent.getStringExtra("inviteCode");
        final String pphonNum=intent.getStringExtra("pphoneNum");

        Log.i("다슬로그",inviteCode+pphonNum);
        editUphone=(EditText)findViewById(R.id.Pphone);
        btnSendMessage=(Button)findViewById(R.id.sendMessage);//connect

        btnSendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(NConnectActivity.this,"연동버튼 누름",Toast.LENGTH_SHORT).show();
               // sms.sendTextMessage(editUphone.getText().toString(), null, inviteCode, null, null);
                sms.sendTextMessage("01022841720", null, "보내라", null, null);
            }
        });
    }

}
