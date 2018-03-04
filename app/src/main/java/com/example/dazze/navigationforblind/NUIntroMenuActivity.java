package com.example.dazze.navigationforblind;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class NUIntroMenuActivity extends AppCompatActivity {

    Button goSelectWay;
    Button goFavoritesWay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nuintro_menu);

        goSelectWay = findViewById(R.id.goSelectWay);
        goFavoritesWay = findViewById(R.id.goFavoritesWay);

        goSelectWay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getBaseContext(), "경로 선택", Toast.LENGTH_SHORT);
            }
        });

        goSelectWay.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Intent intent = new Intent(getBaseContext(), NUSelectWayActivity.class);
                startActivity(intent);
                return true;
            }
        });

        goFavoritesWay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), NUFavoritesWayActivity.class);
                startActivity(intent);
            }
        });
    }
}
