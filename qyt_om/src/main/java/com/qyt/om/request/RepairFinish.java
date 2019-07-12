package com.qyt.om.request;

import java.util.ArrayList;

/**
 * Created by Administrator on 2018-8-14 0014.
 */

public class RepairFinish {

    public String pondAddr;
    public String longitude;
    public String latitude;
    public String oldDeviceId;
    public String newDeviceId;
    public String selfYes;// 1-养殖户自行解决，0-现场解决
    public String resMulti;//故障原因
    public String resOth;//故障原因描述
    public String conMulti;//维修内容
    public String conOth;//维修内容描述
    public String remarks;//备注
    public ArrayList<String> repairUrls = new ArrayList<>();
    public ArrayList<String> payUrls = new ArrayList<>();

}
