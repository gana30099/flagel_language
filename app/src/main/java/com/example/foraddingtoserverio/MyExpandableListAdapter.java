package com.example.foraddingtoserverio;

import java.util.List;
import java.util.Map;

public class MyExpandableListAdapter  {

    private List<String> groupList = null;
    private Map<String, List<String>> childListMap = null;
    private MainActivity2 ipPlusPortMain;

    public MyExpandableListAdapter(List<String> groupList, Map<String, List<String>> childListMap, MainActivity2 ip ) {
        this.groupList = groupList;
        this.childListMap = childListMap;
        this.ipPlusPortMain = ip;
    }


}
