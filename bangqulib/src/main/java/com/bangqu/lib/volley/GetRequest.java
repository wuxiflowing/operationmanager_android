package com.bangqu.lib.volley;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.Gson;
import com.google.gson.JsonElement;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by renruigang on 2015/8/25.
 */
public class GetRequest<T extends JsonElement> extends Request<T> {

    private final Listener<T> mListener;
    private Gson mGson;
    private final String TYPE_UTF8_CHARSET = "charset=UTF-8";
    /*Header校验字符串*/
    public static final String AUTHORIZATION = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJmNCJ9.b5tLg9q3YQ0r8JCC0CvZlDJq9CtT0wCSS9IrPBFYwUOAY3XqpnGNYWscZKkI8AcVwKPBK6A7gUD4TNBnwEn4qw";

    public GetRequest(String url, ResponseCallBack responseCallBack) {
        super(Method.GET, url, responseCallBack.errorListener);
        mGson = new Gson();
        mListener = responseCallBack.listener;
    }

    public GetRequest(String url, String del, ResponseCallBack responseCallBack) {
        super(Method.DELETE, url, responseCallBack.errorListener);
        mGson = new Gson();
        mListener = responseCallBack.listener;
    }

    public GetRequest(String url, boolean put, ResponseCallBack responseCallBack) {
        super(Method.PUT, url, responseCallBack.errorListener);
        mGson = new Gson();
        mListener = responseCallBack.listener;
    }

    @Override
    protected Response parseNetworkResponse(NetworkResponse response) {
        try {
            String type = response.headers.get("Content-Type");
            if (type == null) {
                type = TYPE_UTF8_CHARSET;
                response.headers.put("Content-Type", type);
            } else if (!type.contains("UTF-8")) {
                type += ";" + TYPE_UTF8_CHARSET;
                response.headers.put("Content-Type", type);
            }
            String jsonString = new String(response.data,
                    HttpHeaderParser.parseCharset(response.headers));
            Log.e("Response", jsonString);
            return Response.success(mGson.fromJson(jsonString, JsonElement.class), HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        }
    }

    @Override
    protected void deliverResponse(T response) {
        mListener.onResponse(response);
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        Map<String, String> params = new HashMap<>();
        params.put("Authorization", AUTHORIZATION);
        return params;
    }

}