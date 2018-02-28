package com.example.dazze.navigationforblind;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by daseul on 2018-02-28.
 */

//경로의 정보

public class NWayInfoData {

    public List<NXY> m_XYList;//좌표의 List (위도,경도)
    public int m_idNum;//경로번호
    public int m_status;//경로의 상태값 (0 : 특이사항없음 1:수정중 )

    NWayInfoData(){
        m_XYList=new ArrayList();
    }

}
