package com.qyt.bm.response;

import java.util.List;

/**
 * 作者：rrg
 * 描述：
 */
public class RecycleTaskData {

    public String txtCenMagName;
    public String txtHK;
    public String txtPondPhone;
    public String txtFormImgSrc;
    public String txtPondAddr;
    public String rdoRCYesSec;
    public String txtHKPhone;
    public String txtFarmerName;
    public String txtCSMembNo;
    public String latitude;
    public String txtParAnsID;
    public String txtKey;
    public String tarRemarks;
    public String txtFarmerAddr;
    public String txtCSMembName;
    public String txtEndDate;
    public String txtHKID;
    public String txtFormNo;
    public String taskState;
    public String txtPondID;
    public String rdoYes;
    public String txtFarmerID;
    public String txtHKName;
    public String tarReson;
    public String txtStartDate;
    public String txtCenterID;
    public String txtCenter;
    public String tarDmtRemaks;
    public String longitude;
    public String txtCenMagID;
    public String rdoRCNoSec;
    public String rdoNo;
    public String txtPondName;
    public String picture;
    public String txtFarmerPhone;
    public String txtMatnerMembNo;
    public String txtMatnerMembName;
    public String tarExplain;
    public String region;
    public String tarCustomerReson;
    public String txtDamageImgSrc;
    public String txtReceiptImgSrc;
    public String txtResMulti;
    public List<TabPondsBean> tabPonds;
    public List<TarEqpsBean> tarEqps;
    public List<?> tabPerPond;
    public String rdoGood;
    public String rdoBad;
    public String txtMessageState;

    public static class TabPondsBean {
        /**
         * ITEM10 : 00027E
         * ITEM11 : false
         * ITEM12 : 0.0
         * ITEM13 :
         * ITEM6 : 费雄 MEM14261533925781418
         * ITEM5 : MEM14261533925781418
         * ITEM4 : 3796
         * ITEM3 :
         * ITEM9 : 30.782318936749242
         * ITEM8 : 120.14829905834577
         * ITEM7 : PND11110000000000464
         * ROWINDEX : 0
         * ITEM2 : 湖州市南浔区
         * ITEM1 : 庆渔堂实验鱼塘1
         */

        public String ITEM10;
        public String ITEM11;
        public String ITEM12;
        public String ITEM13;
        public String ITEM6;
        public String ITEM5;
        public String ITEM4;
        public String ITEM3;
        public String ITEM9;
        public String ITEM8;
        public String ITEM7;
        public int ROWINDEX;
        public String ITEM2;
        public String ITEM1;
    }

    public static class TarEqpsBean {
        /**
         * ROWINDEX : 0
         * ITEM2 : 00027E
         * ITEM1 : 3796
         * ITEM5 : 庆渔堂实验鱼塘1
         * ITEM4 :
         */

        public int ROWINDEX;
        public String ITEM2;
        public String ITEM1;
        public String ITEM5;
        public String ITEM4;
    }
}
