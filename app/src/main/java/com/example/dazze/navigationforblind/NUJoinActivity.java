package com.example.dazze.navigationforblind;

import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class NUJoinActivity extends AppCompatActivity {

    static final int SMS_RECEIVE_PERMISSION = 1;

    String senderAdd = ""; // 발신자의 연락처
    String authCodeR = ""; // SMS로 수신받은 초대코드
    String authCodeP = ""; // 보호자 초대코드

    EditText edtInsertCode;
    Button btnInsertCode;

//    ListView listView;
//    List idList = new ArrayList<>();
//    ArrayAdapter adapter;
    static boolean calledAlready = false;

    FirebaseDatabase database;
    DatabaseReference databaseRef;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nujoin);
        if(!calledAlready){
            FirebaseDatabase.getInstance().setPersistenceEnabled(true);
            calledAlready = true;
        }

        edtInsertCode = findViewById(R.id.edtInsertCode);
        btnInsertCode = findViewById(R.id.btnInsertCode);

//        listView = findViewById(R.id.lvIdList);
//
//        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1);
//        listView.setAdapter(adapter);

        database = FirebaseDatabase.getInstance();
        databaseRef = database.getReference("userInfo");
        mAuth = FirebaseAuth.getInstance();

        btnInsertCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkSmsCode();
            }
        });

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
                ActivityCompat.requestPermissions(this, new String[]{ android.Manifest.permission.READ_SMS}, SMS_RECEIVE_PERMISSION);
            }else{
                ActivityCompat.requestPermissions(this, new String[]{ android.Manifest.permission.READ_SMS}, SMS_RECEIVE_PERMISSION);
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
        String code_start = "[";
        String code_end = "]";

        pt_start = smsBody.indexOf(code_start);
        if(pt_start != -1){
            pt_end = smsBody.indexOf(code_end);
            if(pt_end != -1){
                authCodeR = smsBody.substring(pt_start + code_start.length(), pt_end);
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

    public void checkSmsCode(){
        if(authCodeR.equals(authCodeP)){
            Toast.makeText(getApplicationContext(), "인증 완료되었습니다.", Toast.LENGTH_SHORT).show();

            /*
            인증 완료시
            1. 노드 생성
            2. userInfo부분에 사용자의 핸드폰번호 추가
             */

            //데이터 등록
            databaseRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    //원래 코드! (밑에는 test용)
//                    NUserInfo minfo=new NUserInfo();
//                    minfo = dataSnapshot.child(senderAdd).getValue(NUserInfo.class);
//                    minfo.m_uphoneNum="(사용자번호)";//수정필요
//                    database.getReference().child("userInfo").child(senderAdd).setValue(minfo);

                    //test 용
                    NUserInfo minfo=new NUserInfo();
                    minfo = dataSnapshot.child("0").getValue(NUserInfo.class);
                    minfo.m_uphoneNum="(사용자번호)";//수정필요
                    database.getReference().child("userInfo").child("0").setValue(minfo);

                    //노드 생성(firebase에 데이터를 등록)
                    String tmpUID=minfo.m_uid;
                    String tmpPhone1=minfo.m_pphonNum;//보호자 번호
                    String tmpPhone2="(사용자번호)";//수정필요
                    NData data=new NData();
                    database.getReference().child("userData").child(tmpUID).child(tmpPhone1).setValue(data);//child 2개 생성 & data초기화(추후진행)
                    database.getReference().child("userData").child(tmpUID).child(tmpPhone2).setValue(data);


                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.w("TAG: ", "Failed to read value", databaseError.toException());
                }
            });

        }
        else{
            Toast.makeText(getApplicationContext(), "없는 초대번호 입니다.", Toast.LENGTH_SHORT).show();
        }
    }

    public void readSmsMessage() throws ParseException {
        Uri allMessage = Uri.parse("content://sms");
        ContentResolver cr = getContentResolver();
        Cursor cursor = cr.query(allMessage,
                new String[]{"_id", "thread_id", "address", "person", "date", "body"},
                null, null,
                "date DESC");

        int idxDate = cursor.getColumnIndex("date");
        int idxBody = cursor.getColumnIndex("body");
        int idxAddress = cursor.getColumnIndex("address");

        while(cursor.moveToNext()){
            long date = cursor.getLong(idxDate);
            String body = cursor.getString(idxBody);
            String address = cursor.getString(idxAddress);

            if(checkSmsDate(date) && readSmsCode(body)){
                senderAdd = address;

                databaseRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        authCodeP = dataSnapshot.child(senderAdd).child("m_inviteCode").getValue(String.class);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.w("TAG: ", "Failed to read value", databaseError.toException());
                    }
                });

                edtInsertCode.setText(authCodeR);
                break;
            }
            else{
                Toast.makeText(getApplicationContext(), "파싱 결과 없음", Toast.LENGTH_SHORT).show();
            }
        }
        cursor.close();
    }
}
