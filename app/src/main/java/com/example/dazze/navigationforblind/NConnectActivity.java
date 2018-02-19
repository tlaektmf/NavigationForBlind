package com.example.dazze.navigationforblind;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.app.ActivityCompat;
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
        editUphone=(EditText)findViewById(R.id.uPhone);
        btnSendMessage=(Button)findViewById(R.id.sendMessage);//connect

        btnSendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(NConnectActivity.this,"연동버튼 누름",Toast.LENGTH_SHORT).show();
               // sms.sendTextMessage(editUphone.getText().toString(), null, inviteCode, null, null);
                // sms.sendTextMessage("01022841720", null, "보내라", null, null);
               // sendSms(inviteCode,pphonNum);

                Intent sendIntent = new Intent(Intent.ACTION_VIEW);
                String smsBody = inviteCode;
                Log.i("다슬 로그",smsBody+" "+editUphone.toString()+" "+pphonNum.toString());
                sendIntent.putExtra("sms_body", smsBody); // 보낼 문자
                sendIntent.putExtra("address1", editUphone.toString()); // 받는사람 번호
                sendIntent.putExtra("address2", pphonNum.toString()); // 보낸사람 번호
                sendIntent.setType("vnd.android-dir/mms-sms");
                startActivity(sendIntent);

            }
        });
    }

    public void sendSms(String _inviteCode, String _pphoneNum){
        //송신 인텐트
        PendingIntent sentPI=PendingIntent.getBroadcast(this,0,new Intent("SMS_SENT"),0);
        //수신 인텐트
        PendingIntent recvPI=PendingIntent.getBroadcast(this,0,new Intent("SMS_DELIVERED"),0);
        registerReceiver(mSentReceiver, new IntentFilter("SMS_SENT"));
        registerReceiver(mRecvReceiver, new IntentFilter("SMS_DELIVERED"));

       //ActivityCompat.requestPermissions(NConnectActivity.this,new String[]{Manifest.permission.SEND_SMS },1);
        //1개의 SMS 메세지를 전송
        /*
        1. destinationAddress ;받는 사람의 phone number
        2. scAddress
        3. text : 문자의 내용
        4. sentIntent : 문자 전송에 관한 pendingIntent , 전송확인결과
        5. deliveryIntent : 문자 도착에 관한 pendingIntent
         */
       sms.sendTextMessage(_pphoneNum, null, _inviteCode, null, null);

    }

    BroadcastReceiver mSentReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            switch (getResultCode()){
                case RESULT_OK://전송 성공
                    Toast.makeText(NConnectActivity.this, "SMS Send", Toast.LENGTH_SHORT).show();
                    break;
                case SmsManager.RESULT_ERROR_GENERIC_FAILURE://전송 실패
                    Toast.makeText(NConnectActivity.this, "ERROR_GENERIC_FAILURE", Toast.LENGTH_SHORT).show();
                    break;
                case SmsManager.RESULT_ERROR_NO_SERVICE://서비스 지역이 아님
                    Toast.makeText(NConnectActivity.this, "ERROR_NO_SERVICE", Toast.LENGTH_SHORT).show();
                    break;
                case SmsManager.RESULT_ERROR_NULL_PDU://PDU실패
                    Toast.makeText(NConnectActivity.this, "ERROR_NULL_PDU", Toast.LENGTH_SHORT).show();
                    break;
                case SmsManager.RESULT_ERROR_RADIO_OFF://무선이 꺼져있음
                    Toast.makeText(NConnectActivity.this, "ERROR_RADIO_OFF", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    BroadcastReceiver mRecvReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            switch (getResultCode()){
                case RESULT_OK://도착완료
                    Toast.makeText(NConnectActivity.this, "SMS Delivered", Toast.LENGTH_SHORT).show();
                    break;
                case RESULT_CANCELED://도착 안됨
                    Toast.makeText(NConnectActivity.this, "SMS Delivered Fail", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

}