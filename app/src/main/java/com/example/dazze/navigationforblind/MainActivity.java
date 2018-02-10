package com.example.dazze.navigationforblind;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private Button btnPJoin;//보호자 회원가입
    private Button btnPLogin;//보호자 로그인
    private Button btnUJoin;
    private Button btnULogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*
        1. 보호자 회원가입
        2. 보호자 로그인
         */

        btnPJoin=(Button)findViewById(R.id.PJoin);
        btnPLogin=(Button)findViewById(R.id.PLogin);


        //

        btnPLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //데이터 확인
                Intent intent=new Intent(getBaseContext(),NPLoginActivity.class);
                startActivity(intent);

            }
        });

        btnPJoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //데이터 확인
                Intent intent=new Intent(getBaseContext(),NPJoinActivity.class);
                startActivity(intent);

            }
        });



    }
}
