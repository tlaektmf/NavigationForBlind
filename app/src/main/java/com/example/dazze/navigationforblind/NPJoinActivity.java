package com.example.dazze.navigationforblind;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.nio.channels.NoConnectionPendingException;

public class NPJoinActivity extends AppCompatActivity {

    //변수선언 (이메일-비밀번호로 회원가입)
    private FirebaseAuth mAuth;
    private EditText editJoinID;
    private EditText editJoinPW;
    private EditText editPphone;
    private Button btnJoin;

    //realtime database
    private FirebaseDatabase database;//데이터 베이스 추가


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_npjoin);

        mAuth = FirebaseAuth.getInstance();
        editJoinID=(EditText)findViewById(R.id.PJoinID);
        editJoinPW=(EditText)findViewById(R.id.PJoinPW);
        editPphone=(EditText)findViewById(R.id.Pphone);
        btnJoin=(Button)findViewById(R.id.PJoin);

        btnJoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(NPJoinActivity.this,"회원가입 버튼 누름",Toast.LENGTH_SHORT).show();
                createUser(editJoinID.getText().toString(),editJoinPW.getText().toString());//회원생성함수 호출
            }
        });


    }//oncreate closed

    /**
     * 회원가입 : 비밀번호와 이메일로 회원 등록해주는 함수
     */
    private void createUser(String email, final String password){
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {

                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (!task.isSuccessful()) {
                            Log.i("다슬로그",editJoinID+" "+editJoinPW);
//                            // Sign in success, update UI with the signed-in user's information
//                            Log.d(TAG, "createUserWithEmail:success");
//                            FirebaseUser user = mAuth.getCurrentUser();
//                            updateUI(user);


                        } else {//회원가입이 성공했을 경우
                            // If sign in fails, display a message to the user.
//                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
//                            Toast.makeText(EmailPasswordActivity.this, "Authentication failed.",
//                                    Toast.LENGTH_SHORT).show();
//                            updateUI(null);

                            Toast.makeText(NPJoinActivity.this,"회원가입 성공",Toast.LENGTH_SHORT).show();
                           // finish();//홈으로 이동

                            /*
                            회원가입이 성공했을 경우
                            - 사용자의 이메일 주소
                            - 사용자의 핸드폰 번호
                            - 랜덤생성된 초대번호를 등록하고

                            보호자-사용자 연동화면으로 전환
                             */


                            //realtime database : 실시간으로 firebase에 데이터를 등록
                            database=FirebaseDatabase.getInstance();
                            NUserInfo minfo=new NUserInfo();
                            minfo.m_userEmail=mAuth.getCurrentUser().getEmail().toString();
                            minfo.m_pphonNum=editPphone.getText().toString();
                            minfo.m_uid=mAuth.getUid().toString();

                            //랜덤으로 초대번호 생성
                            String tmp="";
                            for(int i=mAuth.getUid().length()-1;i>=0;i--){
                                tmp+=mAuth.getUid().toString().charAt(i);
                            }
                            minfo.m_inviteCode=tmp;

                            //데이터 등록
                            database.getReference().child("userInfo").child(minfo.m_pphonNum.toString()).setValue(minfo);

                            //보호자-사용자 연동 화면으로 전환
                            Intent intent=new Intent(NPJoinActivity.this, NConnectActivity.class);
                            intent.putExtra("inviteCode",minfo.m_inviteCode);
                            intent.putExtra("pphoneNum",minfo.m_pphonNum);
                            startActivity(intent);

                        }

                        // ...
                    }
                });
    }

    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}


