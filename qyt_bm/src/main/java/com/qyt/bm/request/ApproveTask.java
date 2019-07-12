package com.qyt.bm.request;

import com.google.gson.Gson;

import java.util.ArrayList;

/**
 * Created by Administrator on 2018-8-9 0009.
 */

public class ApproveTask {

    public String loginid;
    public ApproveSubmit appData = new ApproveSubmit();

    public class ApproveSubmit {
        public String isAgree;//1-同意，0-拒绝
        public String remark;//审核备注
        public String resMulti;//回收原因
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
