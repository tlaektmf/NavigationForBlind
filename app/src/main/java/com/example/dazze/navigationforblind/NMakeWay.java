package com.example.dazze.navigationforblind;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import net.daum.mf.map.api.CameraUpdateFactory;
import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapPointBounds;
import net.daum.mf.map.api.MapPolyline;
import net.daum.mf.map.api.MapReverseGeoCoder;
import net.daum.mf.map.api.MapView;

import java.util.ArrayList;

public class NMakeWay extends AppCompatActivity implements MapView.MapViewEventListener,MapView.OpenAPIKeyAuthenticationResultListener, MapView.CurrentLocationEventListener, MapReverseGeoCoder.ReverseGeoCodingResultListener,MapView.POIItemEventListener {

    private Button btnSendWay;//경로 전송하기
    private boolean flag=true;

    private MapPolyline polyline;
    private ArrayList mMapPointList;//보호자가 찍은 점들을 리스트로 관리

    //위치 정보
    double get_latitude = 0;
    double get_longitude = 0;
    double now_latitude;
    double now_longitude;

    //firebase
    FirebaseDatabase database;
    DatabaseReference databaseRef;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nmake_way);

        //xml에 선언된 map_view 레이아웃을 찾아온 후, 생성한 MapView객체 추가
        MapView mapView = new MapView(this);
        mapView.setDaumMapApiKey("63cfe0149022d302d1f2ce09919b176c");
        ViewGroup mapViewContainer = (ViewGroup) findViewById(R.id.map_make);
        mapViewContainer.addView(mapView);

        /*
        기본 설정 setting
         */
        mapView.setMapViewEventListener(this); // this에 MapView.MapViewEventListener 구현.
        mapView.setPOIItemEventListener(this);

        // 1. 중심점 변경
        mapView.setMapCenterPoint(MapPoint.mapPointWithGeoCoord(37.543682, 127.077555), true);
        // 2. 줌 레벨 변경
        mapView.setZoomLevel(9, true);
        // 3. 줌 인
        mapView.zoomIn(true);
        // 4. 줌 아웃
        mapView.zoomOut(true);

        //5. 마커 설정(추후 마무리)
//        MapPOIItem marker = new MapPOIItem();
//        marker.setItemName("Default Marker");
//        marker.setTag(0);
//        marker.setMapPoint();
//        marker.setMarkerType(MapPOIItem.MarkerType.BluePin); // 기본으로 제공하는 BluePin 마커 모양.
//        marker.setSelectedMarkerType(MapPOIItem.MarkerType.RedPin); // 마커를 클릭했을때, 기본으로 제공하는 RedPin 마커 모양.
//        mapView.addPOIItem(marker);

        btnSendWay=(Button)findViewById(R.id.sendWay);

        polyline= new MapPolyline();
        polyline.setTag(1000);
        polyline.setLineColor(Color.argb(128, 255, 51, 0)); // Polyline 컬러 지정.

        mMapPointList=new ArrayList();

        // 지도뷰의 중심좌표와 줌레벨을 Polyline이 모두 나오도록 조정.
        MapPointBounds mapPointBounds = new MapPointBounds(polyline.getMapPoints());
        int padding = 100; // px
        mapView.moveCamera(CameraUpdateFactory.newMapPointBounds(mapPointBounds, padding));

        //보호자가 '경로전송버튼'을 누르기 전까지 진행
//        while (flag){
//            btnSendWay.setOnClickListener(new View.OnClickListener() {//경로전송버튼을 누를경우
//                @Override
//                public void onClick(View v) {
//                /*
//                1. 경로들의 위도,경도 좌표가 모두 firebase에 데이터로 전송
//                - 사용자도 볼수 있도록 설정
//                - 모든 상태값 변경
//                 */
//                    flag=false;
//                }
//            });
//        }

        //데이터 test
        btnSendWay.setOnClickListener(new View.OnClickListener() {//경로전송버튼을 누를경우
                @Override
                public void onClick(View v) {
                /*
                1. 경로들의 위도,경도 좌표가 모두 firebase에 데이터로 전송
                - 사용자도 볼수 있도록 설정
                - 모든 상태값 변경
                 */

                //일단 임시로 좌표값 설정
                    mMapPointList.add(MapPoint.mapPointWithGeoCoord(37.543682, 127.077555));
                    mMapPointList.add(MapPoint.mapPointWithGeoCoord(37.543736, 127.076801));
                    mMapPointList.add(MapPoint.mapPointWithGeoCoord(37.545369,127.076477));
                    mMapPointList.add(MapPoint.mapPointWithGeoCoord(37.545035,127.075318));
                    mMapPointList.add(MapPoint.mapPointWithGeoCoord(37.545422,127.074127));
                    mMapPointList.add(MapPoint.mapPointWithGeoCoord(37.546215,127.074337));

                    NWayInfoData way=new NWayInfoData();
                    way.m_idNum=1;//첫번째 경로 정보 입니다.
                    way.m_status=0;//경로의 상태값 설정(default)
                    way.m_XYList.add(new NXY(37.543682+"",127.077555+""));
                    way.m_XYList.add(new NXY(37.543736+"",127.076801+""));
                    way.m_XYList.add(new NXY(37.545369+"",127.076477+""));

                    NWayInfoData way2=new NWayInfoData();
                    way2.m_idNum=2;//두번째 경로 정보 입니다.
                    way2.m_status=0;//경로의 상태값 설정(default)
                    way2.m_XYList.add(new NXY(37.545035+"",127.075318+""));
                    way2.m_XYList.add(new NXY(37.545422+"",127.074127+""));
                    way2.m_XYList.add(new NXY(37.546215+"",127.074337+""));

                    NData data=new NData();
                    data.m_way.add(way);
                    data.m_way.add(way2);

                    //이 값들을 firebase에 저장
                    database = FirebaseDatabase.getInstance();
                    //databaseRef = database.getReference("userInfo");
                    mAuth = FirebaseAuth.getInstance();
                    String userUID=mAuth.getCurrentUser().getUid().toString();
                    database.getReference().child("userData").child(userUID).child("공유데이터").setValue(data);

                   // Log.i("다슬로그",data.m_way.get(0).m_XYList.get(0).m_latitude);

                }
            });


    }

    @Override
    public void onReverseGeoCoderFoundAddress(MapReverseGeoCoder mapReverseGeoCoder, String s) {

    }

    @Override
    public void onReverseGeoCoderFailedToFindAddress(MapReverseGeoCoder mapReverseGeoCoder) {

    }

    @Override
    public void onCurrentLocationUpdate(MapView mapView, MapPoint currentLocation, float accuracyInMeters) {
        //현재 위치 정보
        MapPoint.GeoCoordinate mapPointGeo = currentLocation.getMapPointGeoCoord();
        now_latitude = mapPointGeo.latitude;
        now_longitude = mapPointGeo.longitude;
       Toast.makeText(NMakeWay.this,String.format("MapView onCurrentLocationUpdate (%f,%f) accuracy (%f)", mapPointGeo.latitude, mapPointGeo.longitude, accuracyInMeters),Toast.LENGTH_LONG).show();
    }

    @Override
    public void onCurrentLocationDeviceHeadingUpdate(MapView mapView, float v) {

    }

    @Override
    public void onCurrentLocationUpdateFailed(MapView mapView) {

    }

    @Override
    public void onCurrentLocationUpdateCancelled(MapView mapView) {

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
        //사용자가 지도 위를 터치한 경우 호출된다.



    }

    @Override
    public void onMapViewDoubleTapped(MapView mapView, MapPoint mapPoint) {
        //사용자가 지도 위 한 지점을 더블 터치한 경우 호출된다
    }

    @Override
    public void onMapViewLongPressed(MapView mapView, MapPoint mapPoint) {
        //사용자가 지도 위 한 지점을 길게 누른 경우(long press) 호출된다.
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
    public void onPOIItemSelected(MapView mapView, MapPOIItem mapPOIItem) {

    }

    @Override
    public void onCalloutBalloonOfPOIItemTouched(MapView mapView, MapPOIItem mapPOIItem) {

    }

    @Override
    public void onCalloutBalloonOfPOIItemTouched(MapView mapView, MapPOIItem mapPOIItem, MapPOIItem.CalloutBalloonButtonType calloutBalloonButtonType) {

    }

    @Override
    public void onDraggablePOIItemMoved(MapView mapView, MapPOIItem mapPOIItem, MapPoint mapPoint) {

    }

    @Override
    public void onDaumMapOpenAPIKeyAuthenticationResult(MapView mapView, int i, String s) {

    }
}
