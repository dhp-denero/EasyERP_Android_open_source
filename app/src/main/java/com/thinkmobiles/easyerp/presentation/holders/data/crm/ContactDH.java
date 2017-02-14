package com.thinkmobiles.easyerp.presentation.holders.data.crm;

import com.michenko.simpleadapter.RecyclerDH;
import com.thinkmobiles.easyerp.data.model.crm.leads.detail.Customer;

/**
 * Created by Lynx on 2/13/2017.
 */

public class ContactDH extends RecyclerDH {

    private Customer customer;

    public ContactDH(Customer customer) {
        this.customer = customer;
    }

    public Customer getCustomer() {
        return customer;
    }
}
