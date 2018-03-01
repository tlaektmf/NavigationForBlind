package com.example.dazze.navigationforblind;

/**
 * Created by daseul on 2018-02-28.
 */

public class NXY {
    public String m_latitude;//위도값
    public String m_longitude;//경도값

    NXY(){

    }
    NXY(String latitude,String longitude){
        m_latitude= latitude;
        m_longitude=longitude;
    }
}
