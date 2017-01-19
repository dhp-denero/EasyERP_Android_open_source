package com.thinkmobiles.easyerp.presentation.utils;

/**
 * Created by Lynx on 1/13/2017.
 */

public abstract class Constants {
    public static final String BASE_URL                     = "http://testdemo.easyerp.com/";
    public static final String HEADER_SET_COOKIE            = "Set-Cookie";
    public static final String HEADER_COOKIE                = "Cookie";

    //Login
    public static final String POST_LOGIN                   = "users/login";
    //End Login

    //CRM
    public static final String CRM_DASHBOARD_BASE_ID        = "582bfabf5a43a4bc2524bf09";

    //User
    public static final String GET_CURRENT_USER             = "users/current";

    public static final String GET_LEADS                    = "leads";
    public static final String GET_DASHBOARD_CHARTS         = "customDashboard/{dashboardId}";
    // End CRM

    public static final int DELAY_CLICK                     = 600;

    public enum ErrorCodes {
        OK,
        FIELD_EMPTY,
        FIELD_INVALID
    }
}
