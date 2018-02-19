package com.example.dazze.navigationforblind;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

public class NUJoinActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nujoin);

        readSmsMessage();
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
