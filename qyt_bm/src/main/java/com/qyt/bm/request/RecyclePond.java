package com.qyt.bm.request;

import java.util.ArrayList;

public class RecyclePond {

    public String pondId;
    public String maintainKeeperID;
    public ArrayList<RecycleDevice> deviceList = new ArrayList<>();

    public static class RecycleDevice {
        public int deviceId;
        public String identifier;
    }

}
