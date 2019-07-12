package com.qyt.om.response;

import java.util.List;

/**
 * Created by Administrator on 2018-8-29 0029.
 * Describe #
 */

public class RawDeviceInfo {

    public String id;
    public String name;

    public List<ChildDeviceListBean> childDeviceList;

    public static class ChildDeviceListBean {
        /**
         * id : 1
         * identifier : 000001
         * name : 1号控制器
         * type : KD326
         * dissolvedOxygen : 0
         * temperature : 0
         * ph : 0
         * enabled : true
         * automatic : false
         * workStatus : 3
         * oxyLimitUp : 9
         * oxyLimitDownOne : 8
         * oxyLimitDownTwo : 7
         * alertlineOne : 6
         * alertlineTwo : 5
         * aeratorControls : [{"port":1},{"port":1}]
         * channelA : 控制1參數配置
         * statusA : 開
         * hasAmmeterA : true
         * ammeterTypeA : 控制1電表型號
         * ammeterIdA : 1
         * powerA : 25 功率
         * voltageUpA : 100
         * voltageDownA : 50
         * electricCurrentUpA : 250
         * electricCurrentDownA : 100
         * channelB : 控制2參數配置
         * statusB : 關
         * hasAmmeterB : false
         * ammeterTypeB : 控制2電表型號
         * ammeterIdB : 1
         * powerB : 45 功率
         * voltageUpB : 60
         * voltageDownB : 40
         * electricCurrentUpB : 700
         * electricCurrentDownB : 450
         * scheduled : false
         */

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
        public String channelA;
        public String statusA;
        public String hasAmmeterA;
        public String ammeterTypeA;
        public String ammeterIdA;
        public String powerA;
        public String voltageUpA;
        public String voltageDownA;
        public String electricCurrentUpA;
        public String electricCurrentDownA;
        public String channelB;
        public String statusB;
        public String hasAmmeterB;
        public String ammeterTypeB;
        public String ammeterIdB;
        public String powerB;
        public String voltageUpB;
        public String voltageDownB;
        public String electricCurrentUpB;
        public String electricCurrentDownB;
        public boolean scheduled;
        public List<AeratorControlsBean> aeratorControls;

        public static class AeratorControlsBean {
            /**
             * port : 1
             */

            public int port;
        }
    }
}
