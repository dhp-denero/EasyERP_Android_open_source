package com.thinkmobiles.easyerp.data.services;

import com.thinkmobiles.easyerp.data.model.crm.dashboard.ResponseGetCRMDashboardCharts;
import com.thinkmobiles.easyerp.presentation.utils.Constants;

import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * @author michael.soyma@thinkmobiles.com (Created on 1/16/2017.)
 */

public interface DashboardService {

    @GET(Constants.GET_DASHBOARD_CHARTS)
    Observable<ResponseGetCRMDashboardCharts> getDashboardListCharts(@Path("dashboardId") final String dashboardId, @Query("contentType") final String contentType);
}
