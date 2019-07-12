package com.qyt.om.response;

import java.util.ArrayList;

/**
 * Created by Administrator on 2018-8-13 0013.
 */

public class DeviceConfigInfo {

    public int id;
    public String identifier;
    public String name;
    public String type;
    public String dissolvedOxygen;
    public String temperature;
    public String ph;
    public boolean enabled;
    public boolean automatic;
    public int workStatus;
    public String oxyLimitUp;
    public String oxyLimitDownOne;
    public String oxyLimitDownTwo;
    public String alertlineOne;
    public String alertlineTwo;
    public boolean scheduled;
    public ArrayList<AeratorControlItem> aeratorControlList;

}
