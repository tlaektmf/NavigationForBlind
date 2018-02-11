package com.example.dazze.navigationforblind;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class NPJoinActivity extends AppCompatActivity {

    //변수선언 (이메일-비밀번호로 회원가입)
    private FirebaseAuth mAuth;
    private EditText editJoinID;
    private EditText editJoinPW;
    private EditText editPphone;
    private Button btnJoin;

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
    private void createUser(String email, String password){
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) {
//                            // Sign in success, update UI with the signed-in user's information
//                            Log.d(TAG, "createUserWithEmail:success");
//                            FirebaseUser user = mAuth.getCurrentUser();
//                            updateUI(user);


                        } else {
                            // If sign in fails, display a message to the user.
//                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
//                            Toast.makeText(EmailPasswordActivity.this, "Authentication failed.",
//                                    Toast.LENGTH_SHORT).show();
//                            updateUI(null);
                            Toast.makeText(NPJoinActivity.this,"회원가입 성공",Toast.LENGTH_SHORT).show();
                            finish();
                        }

                        // ...
                    }
                });
    }

    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}

