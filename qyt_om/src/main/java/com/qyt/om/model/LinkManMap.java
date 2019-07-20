package com.qyt.om.model;

import com.qyt.om.response.LinkManBean;

/**
 * @author: sunzhibin
 * @date: 2019-07-14 17:41
 * @description:
 * @e-mail:
 */
public class LinkManMap {
    public String label;
    public LinkManBean linkManBean;

    public LinkManMap(String label, LinkManBean linkManBean) {
        this.label = label;
        this.linkManBean = linkManBean;
    }
}
