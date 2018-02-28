package com.example.dazze.navigationforblind;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by daseul on 2018-02-28.
 */

//찍은 경로의 데이터

public class NWayInfoData {

    public int idNum;//경로번호
    List<NXY> mXYList;//좌표의 List

    NWayInfoData(){

        mXYList=new ArrayList();
    }

}
