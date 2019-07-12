package com.qyt.om.request;

import com.google.gson.Gson;

/**
 * Created by Administrator on 2018-7-28 0028.
 */

public class UploadModel {
    public String imageName;
    public String imageData;
    public String type;

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
