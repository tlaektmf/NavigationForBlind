package com.example.dazze.navigationforblind;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by daseul on 2018-02-28.
 */

//찍은 경로의 데이터

public class NWayInfoData {

    public int m_idNum;//경로번호
    List<NXY> m_XYList;//좌표의 List

    NWayInfoData(){

        m_XYList=new ArrayList();
    }

}
