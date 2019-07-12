package com.qyt.bm.base;

import android.content.Context;
import android.view.View;

import com.android.volley.Request;
import com.bangqu.lib.base.EshowFragment;
import com.qyt.bm.BmApplication;
import com.qyt.bm.utils.LogInfo;
import com.qyt.bm.utils.SharedPref;

import java.util.ArrayList;

import butterknife.Unbinder;

public class BaseFragment extends EshowFragment {

    protected SharedPref sharedPref;
    protected Unbinder unbinder;
    protected View rootView;
    protected ArrayList<Request> requests = new ArrayList<>();

    @Override
    public void onAttach(Context activity) {
        sharedPref = new SharedPref(getActivity());
        super.onAttach(activity);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (null != unbinder) {
            unbinder.unbind();
        }
        cancelAllRequest();
    }

    @Override
    protected <T> void addToRequestQueue(Request<T> req) {
        req.setTag(getRequestTag());
        requests.add(req);
        BmApplication.getInstance().getRequestQueue().add(req);
    }

    protected void cancelAllRequest() {
        for (Request request : requests) {
            if (request != null) {
                request.cancel();
            }
        }
        BmApplication.getInstance().getRequestQueue().cancelAll(getRequestTag());
    }

    protected String getRequestTag() {
        return getClass().getSimpleName();
    }
}