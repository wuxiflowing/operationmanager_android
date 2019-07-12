package com.qyt.bm.request;

import com.google.gson.Gson;

import java.util.ArrayList;

/**
 * 作者：rrg
 * 描述：意见反馈
 */
public class AppCsm {
    public String loginId;
    public String title;
    public String type;
    public String context;
    public ArrayList<String> attachfile = new ArrayList<>();

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
