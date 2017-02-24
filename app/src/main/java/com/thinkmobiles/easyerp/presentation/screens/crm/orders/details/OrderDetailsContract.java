package com.thinkmobiles.easyerp.presentation.screens.crm.orders.details;

import com.thinkmobiles.easyerp.data.model.crm.order.detail.ResponseGetOrderDetails;
import com.thinkmobiles.easyerp.data.model.user.organization.ResponseGetOrganizationSettings;
import com.thinkmobiles.easyerp.presentation.base.BaseModel;
import com.thinkmobiles.easyerp.presentation.base.BaseView;
import com.thinkmobiles.easyerp.presentation.base.rules.content.ContentPresenter;
import com.thinkmobiles.easyerp.presentation.base.rules.content.ContentView;
import com.thinkmobiles.easyerp.presentation.holders.data.crm.AttachmentDH;
import com.thinkmobiles.easyerp.presentation.holders.data.crm.HistoryDH;
import com.thinkmobiles.easyerp.presentation.holders.data.crm.ProductDH;

import java.util.ArrayList;

import rx.Observable;


public interface OrderDetailsContract {

    interface OrderDetailsView extends BaseView<OrderDetailsPresenter>, ContentView {
        void showHistory(boolean enable);

        void setOrderStatusName(String orderStatus);
        void setOrderStatus(String orderStatus);
        void setCompanyName(String companyName);
        void setCompanyAddress(String companyAddress);
        void setOrderName(String orderName);
        void setSupplierName(String supplierName);
        void setSupplierAddress(String supplierAddress);
        void setExpectedDate(String expectedDate);
        void setOrderDate(String orderDate);
        void setSubTotal(String subTotal);
        void setDiscount(String discount);
        void setTaxes(String taxes);
        void setTotal(String total);
        void setPrepaid(String prepaid);
        void setNameBeneficiary(String nameBeneficiary);
        void setBank(String bank);
        void setBankAddress(String bankAddress);
        void setBankIBAN(String bankIBAN);
        void setSwiftCode(String swiftCode);
        void setOwnerName(String ownerName);
        void setOwnerSite(String ownerSite);
        void setOwnerEmail(String ownerEmail);
        void setAttachments(ArrayList<AttachmentDH> attachmentDHs);
        void setAdvice(String advice);
        void setHistory(ArrayList<HistoryDH> history);
        void setProducts(ArrayList<ProductDH> products);
    }

    interface OrderDetailsPresenter extends ContentPresenter {
        void changeNotesVisibility();
    }

    interface OrderDetailsModel extends BaseModel {
        Observable<ResponseGetOrderDetails> getOrderDetails(String orderId);
        Observable<ResponseGetOrganizationSettings> getOrganizationSettings();
    }
}
