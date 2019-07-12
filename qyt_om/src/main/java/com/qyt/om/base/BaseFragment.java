package com.qyt.om.base;

import android.content.Context;
import android.view.View;

import com.android.volley.Request;
import com.bangqu.lib.base.EshowFragment;
import com.qyt.om.OmApplication;
import com.qyt.om.utils.LogInfo;
import com.qyt.om.utils.SharedPref;


import java.util.ArrayList;

import butterknife.Unbinder;


public abstract class BaseFragment extends EshowFragment {

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
        OmApplication.getInstance().getRequestQueue().add(req);
    }

    protected void cancelAllRequest() {
        for (Request request : requests) {
            if (request != null) {
                request.cancel();
            }
        }
        OmApplication.getInstance().getRequestQueue().cancelAll(getRequestTag());
    }

    protected String getRequestTag() {
        return getClass().getSimpleName();
    }

}