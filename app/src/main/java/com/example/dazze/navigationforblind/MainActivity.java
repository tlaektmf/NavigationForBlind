package com.example.dazze.navigationforblind;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private Button btnGoPJoin;//보호자 회원가입
    private Button btnGoPLogin;//보호자 로그인
    private Button btnGoUJoin;//사용자 회원가입
    private Button btnGoULogin;//사용자 로그인
    private Button btnGoMapView;//맵뷰 보이기

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*
        1. 보호자 회원가입
        2. 보호자 로그인
        3. 사용자 회원가입
        4. mapview 보이기
         */

        btnGoPJoin=(Button)findViewById(R.id.goPJoin);
        btnGoPLogin=(Button)findViewById(R.id.goPLogin);
        btnGoUJoin=(Button)findViewById(R.id.goUJoin);
        btnGoMapView=(Button)findViewById(R.id.goMap);

        //

        btnGoPLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //데이터 확인
                Intent intent=new Intent(getBaseContext(),NPLoginActivity.class);
                startActivity(intent);

            }
        });

        btnGoPJoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //데이터 확인
                Intent intent=new Intent(getBaseContext(),NPJoinActivity.class);
                startActivity(intent);

            }
        });

        btnGoUJoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), NUJoinActivity.class);
                startActivity(intent);
            }
        });

        btnGoMapView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), NMapViewActivity.class);
                startActivity(intent);
            }
        });

    }
}
