package com.example.dazze.navigationforblind;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import net.daum.mf.map.api.MapView;

public class NMapViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nmap_view);

        //xml에 선언된 map_view 레이아웃을 찾아온 후, 생성한 MapView객체 추가
        MapView mapView = new MapView(this);
        mapView.setDaumMapApiKey("63cfe0149022d302d1f2ce09919b176c");
        ViewGroup mapViewContainer = (ViewGroup) findViewById(R.id.map_view);
        mapViewContainer.addView(mapView);

        /*
        1. 보호자가 사용자에게 넘긴 경로의 번호 & 개수를 받는다
        2. 점의 좌표 (위도, 경도) 를 받아
        3. 점을 찍어주고 이어준다
         */




    }
}
