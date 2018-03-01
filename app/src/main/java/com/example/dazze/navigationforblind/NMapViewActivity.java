package com.example.dazze.navigationforblind;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import net.daum.mf.map.api.CameraUpdateFactory;
import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapPointBounds;
import net.daum.mf.map.api.MapPolyline;
import net.daum.mf.map.api.MapView;

import java.util.ArrayList;

public class NMapViewActivity extends AppCompatActivity  implements MapView.OpenAPIKeyAuthenticationResultListener, MapView.MapViewEventListener {


    private FirebaseDatabase database;
    private DatabaseReference databaseRef;
    private FirebaseAuth mAuth;
    private ArrayList mMapPointList;//보호자가 찍은 점들을 리스트로 관리

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nmap_view);

        //xml에 선언된 map_view 레이아웃을 찾아온 후, 생성한 MapView객체 추가
        final MapView mapView = new MapView(this);
        mapView.setDaumMapApiKey("63cfe0149022d302d1f2ce09919b176c");
        ViewGroup mapViewContainer = (ViewGroup) findViewById(R.id.map_view);
        //mapViewContainer.addView(mapView);

        /*
        화면에 경로를 보여주기만 하고 다른 기능은 막아둠
        시각장애인 입장에서 보면, "출발지-도착지 에 해당되는 경로입니다" 부분에 들어가는 activity에 속함
        즉 이 activity들은 모두 tts로 대체하여 시각장애인에게 제공함

        1. 점의 좌표 (위도, 경도) 를 받아
        2. 점을 찍어주고 이어준다
         */
        database = FirebaseDatabase.getInstance();
        databaseRef = database.getReference("userData");
        mAuth = FirebaseAuth.getInstance();

        mMapPointList=new ArrayList();

        databaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                NWayInfoData data=new NWayInfoData();
                data = dataSnapshot.child(mAuth.getCurrentUser().getUid().toString()).child("공유데이터").child("m_way").child("0").getValue(NWayInfoData.class);

                for(int i=0;i<data.m_XYList.size();i++){
                    mMapPointList.add(MapPoint.mapPointWithGeoCoord(Double.parseDouble(data.m_XYList.get(i).m_latitude), Double.parseDouble(data.m_XYList.get(i).m_longitude)));
                }


                //기존에 polyline이 있는지의 여부 판단->없을 경우 재생성
                MapPolyline existingPolyline = mapView.findPolylineByTag(2000);
                if (existingPolyline != null) {//기존에 있을 경우
                    mapView.removePolyline(existingPolyline);
                    Log.i("다슬로그","기존에 이미 POLYLINE있음");
                }

                //polyline 만듦
                MapPolyline polyline = new MapPolyline(mMapPointList.size());
                //polyline 기본설정
                polyline.setTag(2000);
                polyline.setLineColor(Color.argb(128, 255, 51, 0)); // Polyline 컬러 지정.

                for(int i=0;i<mMapPointList.size();i++){
                        polyline.addPoint((MapPoint) mMapPointList.get(i));
                }

                mapView.addPolyline(polyline);//mapview에 polyline 추가

                // 지도뷰의 중심좌표와 줌레벨을 Polyline이 모두 나오도록 조정.
                MapPointBounds mapPointBounds = new MapPointBounds(polyline.getMapPoints());
                int padding = 100; // px
                mapView.moveCamera(CameraUpdateFactory.newMapPointBounds(mapPointBounds, padding));

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("TAG: ", "Failed to read value", databaseError.toException());
            }
        });

        mapViewContainer.addView(mapView);//맵뷰에 화면을 띄운다

    }

    @Override
    public void onMapViewInitialized(MapView mapView) {

    }

    @Override
    public void onMapViewCenterPointMoved(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewZoomLevelChanged(MapView mapView, int i) {

    }

    @Override
    public void onMapViewSingleTapped(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewDoubleTapped(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewLongPressed(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewDragStarted(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewDragEnded(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewMoveFinished(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onDaumMapOpenAPIKeyAuthenticationResult(MapView mapView, int i, String s) {

    }
}
