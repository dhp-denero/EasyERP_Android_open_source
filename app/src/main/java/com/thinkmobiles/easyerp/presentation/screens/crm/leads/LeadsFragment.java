package com.thinkmobiles.easyerp.presentation.screens.crm.leads;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatAutoCompleteTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.thinkmobiles.easyerp.R;
import com.thinkmobiles.easyerp.domain.crm.LeadsRepository;
import com.thinkmobiles.easyerp.presentation.adapters.crm.LeadsAdapter;
import com.thinkmobiles.easyerp.presentation.adapters.crm.SearchAdapter;
import com.thinkmobiles.easyerp.presentation.base.rules.ErrorViewHelper;
import com.thinkmobiles.easyerp.presentation.base.rules.SimpleListWithRefreshFragment;
import com.thinkmobiles.easyerp.presentation.dialogs.FilterDialogFragment;
import com.thinkmobiles.easyerp.presentation.holders.data.crm.FilterDH;
import com.thinkmobiles.easyerp.presentation.holders.data.crm.LeadDH;
import com.thinkmobiles.easyerp.presentation.listeners.EndlessRecyclerViewScrollListener;
import com.thinkmobiles.easyerp.presentation.screens.crm.leads.details.LeadDetailsFragment_;
import com.thinkmobiles.easyerp.presentation.utils.Constants;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterTextChange;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.OnActivityResult;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.OptionsMenuItem;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.res.StringRes;

import java.util.ArrayList;

/**
 * Created by Lynx on 1/16/2017.
 */
@EFragment(R.layout.fragment_simple_list_with_swipe_refresh)
public class LeadsFragment extends SimpleListWithRefreshFragment implements LeadsContract.LeadsView {

    private LeadsContract.LeadsPresenter presenter;
    private EndlessRecyclerViewScrollListener scrollListener;

    @Bean
    protected LeadsRepository leadsRepository;
    @Bean
    protected LeadsAdapter leadsAdapter;
    @Bean
    protected ErrorViewHelper errorViewHelper;

    @StringRes(R.string.list_is_empty)
    protected String string_list_is_empty;

    @ViewById(R.id.llErrorLayout)
    protected View errorLayout;

    @ViewById
    protected AppCompatAutoCompleteTextView actSearch;

    @Bean
    protected SearchAdapter searchAdapter;

    @OptionsMenuItem(R.id.menuFilterContactName)
    protected MenuItem menuContactName;

    @OptionsMenuItem(R.id.menuFilterAssignedTo)
    protected MenuItem menuAssignedTo;

    @OptionsMenuItem(R.id.menuFilterCreatedBy)
    protected MenuItem menuCreatedBy;

    @OptionsMenuItem(R.id.menuFilterSource)
    protected MenuItem menuSource;

    @OptionsMenuItem(R.id.menuFilterStage)
    protected MenuItem menuWorkflow;

    @AfterInject
    @Override
    public void initPresenter() {
        new LeadsPresenter(this, leadsRepository);
    }

    @Override
    public void setPresenter(LeadsContract.LeadsPresenter presenter) {
        this.presenter = presenter;
    }

    @AfterViews
    protected void initUI() {
        errorViewHelper.init(errorLayout, view -> presenter.subscribe());

        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        scrollListener = new EndlessRecyclerViewScrollListener(llm) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                presenter.loadNextPage(page);
            }
        };
        listRecycler.setLayoutManager(llm);
        listRecycler.setAdapter(leadsAdapter);
        listRecycler.addOnScrollListener(scrollListener);
        leadsAdapter.setOnCardClickListener((view, position, viewType) ->
                presenter.selectItemLead(leadsAdapter.getItem(position), position)
        );

        actSearch.setAdapter(searchAdapter);
        actSearch.setOnItemClickListener((adapterView, view, i, l) ->
                presenter.filterByContactName(searchAdapter.getItem(i))
        );
        actSearch.setOnClickListener((v) -> actSearch.setText(""));
        actSearch.setOnKeyListener((v, keyCode, event) -> {
            if (event.getKeyCode() == KeyEvent.KEYCODE_ENTER
                    && event.getAction() == KeyEvent.ACTION_DOWN) {

                presenter.filterBySearchContactName(actSearch.getText().toString());

                hideKeyboard();
                actSearch.dismissDropDown();
                return true;
            }
            return false;
        });

        presenter.subscribe();
    }

    @AfterTextChange(R.id.actSearch)
    protected void afterSearchChanged(Editable editable) {
        if (editable.length() > 1) {
            searchAdapter.getFilter().filter(editable.toString());
        }
    }

    @Override
    public void displayLeads(ArrayList<LeadDH> leadDHs, boolean needClear) {
        if (needClear)
            leadsAdapter.setListDH(leadDHs);
        else
            leadsAdapter.addListDH(leadDHs);
    }

    @Override
    public void showProgress(boolean isShow) {
        if (isShow) {
            errorViewHelper.hideError();
            displayProgress(true);
            swipeContainer.setRefreshing(false);
        } else {
            errorViewHelper.hideError();
            displayProgress(false);
            swipeContainer.setRefreshing(false);
        }
    }

    @Override
    public void changeSelectedItem(int oldPosition, int newPosition) {
        leadsAdapter.replaceSelectedItem(oldPosition, newPosition);
    }

    @Override
    public void showEmptyState() {
        leadsAdapter.setListDH(new ArrayList<>());
        errorViewHelper.showErrorMsg(string_list_is_empty, ErrorViewHelper.ErrorType.LIST_EMPTY);
    }

    @Override
    public void displayError(String msg, ErrorViewHelper.ErrorType errorType) {
        displayProgress(false);
        swipeContainer.setRefreshing(false);

        final String resultMsg = errorType.equals(ErrorViewHelper.ErrorType.LIST_EMPTY) ? string_list_is_empty : msg;
        if (getCountItemsNow() == 0)
            errorViewHelper.showErrorMsg(resultMsg, errorType);
        else Toast.makeText(getContext(), resultMsg, Toast.LENGTH_LONG).show();
    }

    @Override
    public void openLeadDetailsScreen(String leadId) {
        if (leadId != null) {
            mActivity.replaceFragmentContentDetail(LeadDetailsFragment_.builder()
                    .leadId(leadId)
                    .build());
        } else {
            mActivity.replaceFragmentContentDetail(null);
        }
    }

    @Override
    protected boolean needProgress() {
        return true;
    }

    @Override
    public void onRefresh() {
        scrollListener.resetState();
        presenter.refresh();
    }

    @Override
    public int getCountItemsNow() {
        return leadsAdapter.getItemCount();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.unsubscribe();
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        if (presenter != null && presenter.isEnabledFilters()) {
            menu.findItem(R.id.menuFilter_MB).setVisible(true);
        }
    }

    @OptionsItem(R.id.menuFilterContactName)
    protected void clickContactName() {
        presenter.changeFilter(Constants.REQUEST_CODE_FILTER_CONTACT_NAME);
    }

    @OptionsItem(R.id.menuFilterStage)
    protected void clickStage() {
        presenter.changeFilter(Constants.REQUEST_CODE_FILTER_WORKFLOW);
    }

    @OptionsItem(R.id.menuFilterCreatedBy)
    protected void clickcreatedBy() {
        presenter.changeFilter(Constants.REQUEST_CODE_FILTER_CREATED_BY);
    }

    @OptionsItem(R.id.menuFilterAssignedTo)
    protected void clickAssignedTo() {
        presenter.changeFilter(Constants.REQUEST_CODE_FILTER_ASSIGNED_TO);
    }

    @OptionsItem(R.id.menuFilterSource)
    protected void clickSource() {
        presenter.changeFilter(Constants.REQUEST_CODE_FILTER_SOURCE);
    }

    @OptionsItem(R.id.menuFilterRemoveAll)
    protected void clickRemoveAll() {
        presenter.removeAll();
    }

    @Override
    public void setContactNames(ArrayList<FilterDH> contactNames) {
        searchAdapter.setItems(contactNames);
    }

    @Override
    public void setTextToSearch(String text) {
        actSearch.setText(text);
        actSearch.setSelection(text.length());
        hideKeyboard();
    }

    @Override
    public void showFilters() {
        actSearch.setVisibility(View.VISIBLE);
        mActivity.invalidateOptionsMenu();
    }

    @Override
    public void selectContactNameInFilters(boolean isSelected) {
        menuContactName.setChecked(isSelected);
    }

    @Override
    public void selectWorkflowInFilters(boolean isSelected) {
        menuWorkflow.setChecked(isSelected);
    }

    @Override
    public void selectAssignedToInFilters(boolean isSelected) {
        menuAssignedTo.setChecked(isSelected);
    }

    @Override
    public void selectCreatedByInFilters(boolean isSelected) {
        menuCreatedBy.setChecked(isSelected);
    }

    @Override
    public void selectSourceInFilters(boolean isSelected) {
        menuSource.setChecked(isSelected);
    }

    @Override
    public void showFilterDialog(ArrayList<FilterDH> filterDHs, int requestCode) {
        FilterDialogFragment dialogFragment = new FilterDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(Constants.KEY_FILTER_LIST, filterDHs);
        dialogFragment.setArguments(bundle);
        dialogFragment.setTargetFragment(this, requestCode);
        dialogFragment.show(getFragmentManager(), getClass().getName());
    }

    @OnActivityResult(Constants.REQUEST_CODE_FILTER_CONTACT_NAME)
    protected void resultContactName(int resultCode,
                                     @OnActivityResult.Extra(value = Constants.KEY_FILTER_LIST) ArrayList<FilterDH> filterDHs) {
        if (resultCode == Activity.RESULT_OK) {
            presenter.filterByListContactNames(filterDHs);
        } else {
            presenter.removeFilterContactName();
        }
    }

    @OnActivityResult(Constants.REQUEST_CODE_FILTER_WORKFLOW)
    protected void resultWorkflows(int resultCode,
                                     @OnActivityResult.Extra(value = Constants.KEY_FILTER_LIST) ArrayList<FilterDH> filterDHs) {
        if (resultCode == Activity.RESULT_OK) {
            presenter.filterByListWorkflow(filterDHs);
        } else {
            presenter.removeFilterWorkflow();
        }
    }

    @OnActivityResult(Constants.REQUEST_CODE_FILTER_ASSIGNED_TO)
    protected void resultAssignedTo(int resultCode,
                                     @OnActivityResult.Extra(value = Constants.KEY_FILTER_LIST) ArrayList<FilterDH> filterDHs) {
        if (resultCode == Activity.RESULT_OK) {
            presenter.filterByListAssignedTo(filterDHs);
        } else {
            presenter.removeFilterAssignedTo();
        }
    }

    @OnActivityResult(Constants.REQUEST_CODE_FILTER_CREATED_BY)
    protected void resultCreatedBy(int resultCode,
                                     @OnActivityResult.Extra(value = Constants.KEY_FILTER_LIST) ArrayList<FilterDH> filterDHs) {
        if (resultCode == Activity.RESULT_OK) {
            presenter.filterByListCreatedBy(filterDHs);
        } else {
            presenter.removeFilterCreatedBy();
        }
    }

    @OnActivityResult(Constants.REQUEST_CODE_FILTER_SOURCE)
    protected void resultSource(int resultCode,
                                     @OnActivityResult.Extra(value = Constants.KEY_FILTER_LIST) ArrayList<FilterDH> filterDHs) {
        if (resultCode == Activity.RESULT_OK) {
            presenter.filterByListSource(filterDHs);
        } else {
            presenter.removeFilterSource();
        }
    }
}
