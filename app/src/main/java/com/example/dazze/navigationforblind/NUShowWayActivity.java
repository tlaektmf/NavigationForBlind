package com.example.dazze.navigationforblind;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class NUShowWayActivity extends AppCompatActivity {

    /**(추후에 수정할 사항)
     * 보호자가 사용자에게 넘겨준 경로의 개수 count값이 0이 될 때까지
     * 점의 좌표들을 불러와서 점을 찍어 보여준다
     * fragment의 수는 동적으로 생성하도록 함
     */
    
    private Button btnShow1;//경로1
    private Button btnShow2;//경로2

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nushow_way);

        btnShow1=(Button)findViewById(R.id.show1);
        btnShow2=(Button)findViewById(R.id.show2);

        btnShow1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getBaseContext(),NPJoinActivity.class);
                startActivity(intent);

            }
        });

        btnShow2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getBaseContext(),NPJoinActivity.class);
                startActivity(intent);

            }
        });


    }
}
