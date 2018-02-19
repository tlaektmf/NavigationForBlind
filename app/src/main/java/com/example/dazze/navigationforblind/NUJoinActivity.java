package com.example.dazze.navigationforblind;

import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

public class NUJoinActivity extends AppCompatActivity {

    static final int SMS_RECEIVE_PERMISSON = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nujoin);

        checkSmsPermission();
        readSmsMessage();
    }

    public void checkSmsPermission(){
        int permissonCheck= ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_SMS);

        if(permissonCheck == PackageManager.PERMISSION_GRANTED){
            Toast.makeText(getApplicationContext(), "SMS 수신권한 있음", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(getApplicationContext(), "SMS 수신권한 없음", Toast.LENGTH_SHORT).show();

            //권한설정 dialog에서 거부를 누르면
            //ActivityCompat.shouldShowRequestPermissionRationale 메소드의 반환값이 true가 된다.
            //단, 사용자가 "Don't ask again"을 체크한 경우
            //거부하더라도 false를 반환하여, 직접 사용자가 권한을 부여하지 않는 이상, 권한을 요청할 수 없게 된다.
            if(ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.READ_SMS)){
                //이곳에 권한이 왜 필요한지 설명하는 Toast나 dialog를 띄워준 후, 다시 권한을 요청한다.
                Toast.makeText(getApplicationContext(), "SMS권한이 필요합니다", Toast.LENGTH_SHORT).show();
                ActivityCompat.requestPermissions(this, new String[]{ android.Manifest.permission.READ_SMS}, SMS_RECEIVE_PERMISSON);
            }else{
                ActivityCompat.requestPermissions(this, new String[]{ android.Manifest.permission.READ_SMS}, SMS_RECEIVE_PERMISSON);
            }
        }
    }

    public void readSmsMessage(){
        Uri allMessage = Uri.parse("content://sms");
        ContentResolver cr = getContentResolver();
        Cursor cursor = cr.query(allMessage,
                new String[]{"_id", "thread_id", "address", "person", "date", "body"},
                null, null,
                "date DESC");

        int idxSender = cursor.getColumnIndex("address");
        int idxDate = cursor.getColumnIndex("date");
        int idxBody = cursor.getColumnIndex("body");

        StringBuilder result = new StringBuilder();
        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd HH:mm");

        int count = 0;
        while(cursor.moveToNext()){
            String sender = cursor.getString(idxSender);
            long date = cursor.getLong(idxDate);
            String sdate = formatter.format(new Date(date));
            String body = cursor.getString(idxBody);

            result.append(sdate + ": \n");
            result.append(sender + "");
            result.append(body + "\n");

            if(count++ == 100){
                break;
            }
        }
        cursor.close();

        TextView txtResult = (TextView)findViewById(R.id.SMS);
        txtResult.setText(result);
    }

    public class Message{
        String msgBody; // 문자내용
        String msgDate; // 시간
        String msgSender; // 발신자 번호

        public Message() {
        }

        public String getMsgBody() {
            return msgBody;
        }

        public void setMsgBody(String msgBody) {
            this.msgBody = msgBody;
        }

        public String getMsgDate() {
            return msgDate;
        }

        public void setMsgDate(String msgDate) {
            this.msgDate = msgDate;
        }

        public String getMsgSender() {
            return msgSender;
        }

        public void setMsgSender(String msgSender) {
            this.msgSender = msgSender;
        }
    }
}
