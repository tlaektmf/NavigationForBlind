package com.example.dazze.navigationforblind;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by daseul on 2018-02-28.
 */

//보호자와 사용자가 모두 공유하는 데이터

public class NData {

   // public String tmpString="보호자,사용자 공유데이터";

    public List<NWayInfoData> m_way;

    NData(){
        m_way=new ArrayList();

    }
}
