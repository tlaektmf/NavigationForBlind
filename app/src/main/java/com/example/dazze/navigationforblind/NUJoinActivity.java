package com.example.dazze.navigationforblind;

import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class NUJoinActivity extends AppCompatActivity {

    static final int SMS_RECEIVE_PERMISSON = 1;

    String authCode = "";

    EditText edtInsertCode;
    Button btnInsertCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nujoin);

        edtInsertCode = findViewById(R.id.edtInsertCode);
        btnInsertCode = findViewById(R.id.btnInsertCode);

        checkSmsPermission();

        try {
            readSmsMessage();
        } catch (ParseException e) {
            e.printStackTrace();
        }
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

    public boolean checkSmsDate(long date) throws ParseException { // 날짜가 같다면 return true
        long now = System.currentTimeMillis();

        Date currentDate; // 현재 날짜 Date
        String currentDateStr; // 현재 날짜 String
        Date smsDate; // 수신 날짜 Date
        String smsDateStr; // 수신 날짜 String

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date currentTime = new Date(now);
        currentDateStr = simpleDateFormat.format(currentTime);
        Date smsTime = new Date(date);
        smsDateStr = simpleDateFormat.format(smsTime);

        currentDate = simpleDateFormat.parse(currentDateStr);
        smsDate = simpleDateFormat.parse(smsDateStr);

        int compare = currentDate.compareTo(smsDate);

        if(compare == 0)
            return true; // 날짜가 같다면 true
        else
            return false;
    }

    public boolean readSmsCode(String smsBody){    // 인증번호를 찾으면 return true
        int pt_start = -1;
        int pt_end = -1;
        String code_start = "인증번호[";
        String code_end = "]";

        pt_start = smsBody.indexOf(code_start);
        if(pt_start != -1){
            pt_end = smsBody.indexOf(code_end);
            if(pt_end != -1){
                authCode = smsBody.substring(pt_start + code_start.length(), pt_end);
                return true;
            }
            else{
                return false;
            }
        }
        else{
            return false;
        }
    }

    public void readSmsMessage() throws ParseException {
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

        while(cursor.moveToNext()){
            String sender = cursor.getString(idxSender);
            long date = cursor.getLong(idxDate);
            String sdate = formatter.format(new Date(date));
            String body = cursor.getString(idxBody);

            if(checkSmsDate(date) && readSmsCode(body)){
//                result.append(sdate + ": \n");
//                result.append(sender + "");
//                result.append(body + "\n");
                edtInsertCode.setText(authCode);
                break;
            }
            else{
                Toast.makeText(getApplicationContext(), "파싱 결과 없음", Toast.LENGTH_SHORT).show();
            }
        }
        cursor.close();
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
