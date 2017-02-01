package com.thinkmobiles.easyerp.domain.crm;

import com.thinkmobiles.easyerp.data.api.Rest;
import com.thinkmobiles.easyerp.data.model.crm.dashboard.ResponseGetCRMDashboardCharts;
import com.thinkmobiles.easyerp.data.model.crm.dashboard.detail.DashboardChartType;
import com.thinkmobiles.easyerp.data.services.DashboardService;
import com.thinkmobiles.easyerp.presentation.base.NetworkRepository;
import com.thinkmobiles.easyerp.presentation.screens.crm.dashboard.DashboardListContract;
import com.thinkmobiles.easyerp.presentation.screens.crm.dashboard.detail.DashboardDetailChartContract;
import com.thinkmobiles.easyerp.presentation.utils.Constants;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;

import java.util.List;

import rx.Observable;

/**
 * Created by Lynx on 1/16/2017.
 */

@EBean(scope = EBean.Scope.Singleton)
public class DashboardRepository extends NetworkRepository implements DashboardListContract.DashboardListModel, DashboardDetailChartContract.DashboardDetailChartModel {

    @Bean
    protected DashboardChartsLayerRepository dashboardChartsLayerRepository;

    private DashboardService dashboardService;

    public DashboardRepository() {
        dashboardService = Rest.getInstance().getDashboardService();
    }

    public Observable<List<ResponseGetCRMDashboardCharts>> getDashboardListCharts(/*Dashboard id: String*/) {
        return getNetworkObservable(dashboardService.getDashboardListCharts(Constants.CRM_DASHBOARD_BASE_ID));
    }

    @Override
    public Observable<?> getDashboardChartInfo(
            final String dataSet,
            final DashboardChartType chartType,
            final String filterDateFrom,
            final String filterDateTo) {
        return getNetworkObservable(dashboardChartsLayerRepository.getDashboardChartObservable(dataSet, chartType, filterDateFrom, filterDateTo));
    }

}