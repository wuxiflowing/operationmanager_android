package com.bangqu.lib.volley;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.google.gson.JsonObject;

/**
 * Created by renruigang on 2015/8/25.
 */
public abstract class ResponseCallBackRaw<T> {

    private static final String NO_INTERNET = "无网络连接~！";
    private static final String GENERIC_SERVER_DOWN = "连接服务器失败~！";
    private static final String GENERIC_SERVER_ERROR = "服务器异常~！";
    private static final String GENERIC_ERROR = "网络异常,请稍后再试~！";

    public Response.ErrorListener errorListener;
    public Response.Listener<JsonObject> listener;

    public ResponseCallBackRaw() {
        listener = new Response.Listener<JsonObject>() {
            @Override
            public void onResponse(JsonObject response) {
                if (response == null) return;
                onSuccessResponse(response.toString());
            }
        };
        errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                int code = error.networkResponse != null ? error.networkResponse.statusCode : -1;
                onVolleyError(code, getErrorMessage(error));
            }
        };
    }

    public abstract void onSuccessResponse(String response);

    public abstract void onFailResponse(String msg);

    public abstract void onVolleyError(int code, String msg);

    private String getErrorMessage(Object error) {
        if (error instanceof TimeoutError) {
            return GENERIC_SERVER_DOWN;
        } else if (isNetworkProblem(error)) {
            return NO_INTERNET;
        } else if (isServerProblem(error)) {
            VolleyError err = (VolleyError) error;
            if (err.networkResponse != null) {
                int state = err.networkResponse.statusCode;
                if (state > 499 && state < 600) {
                    return GENERIC_SERVER_ERROR;
                }
            }
        }
        return GENERIC_ERROR;
    }

    private boolean isNetworkProblem(Object error) {
        return (error instanceof NetworkError) || (error instanceof NoConnectionError);
    }

    private static boolean isServerProblem(Object error) {
        return (error instanceof ServerError) || (error instanceof AuthFailureError);
    }

}
