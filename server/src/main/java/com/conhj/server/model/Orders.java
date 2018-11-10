package com.conhj.server.model;

import java.math.BigDecimal;

public class Orders extends OrdersKey {
    private String orderdate;

    private BigDecimal totalprice;

    public String getOrderdate() {
        return orderdate;
    }

    public void setOrderdate(String orderdate) {
        this.orderdate = orderdate == null ? null : orderdate.trim();
    }

    public BigDecimal getTotalprice() {
        return totalprice;
    }

    public void setTotalprice(BigDecimal totalprice) {
        this.totalprice = totalprice;
    }
}