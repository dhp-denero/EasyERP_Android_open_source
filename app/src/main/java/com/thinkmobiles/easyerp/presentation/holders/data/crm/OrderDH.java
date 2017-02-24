package com.thinkmobiles.easyerp.presentation.holders.data.crm;

import com.thinkmobiles.easyerp.data.model.crm.order.Order;
import com.thinkmobiles.easyerp.presentation.base.rules.master.selectable.SelectableDHHelper;

/**
 * Created by Lynx on 1/16/2017.
 */

public final class OrderDH extends SelectableDHHelper {
    private final Order order;

    public OrderDH(Order order) {
        this.order = order;
    }

    public Order getOrder() {
        return order;
    }

    @Override
    public String getId() {
        return order.id;
    }
}
