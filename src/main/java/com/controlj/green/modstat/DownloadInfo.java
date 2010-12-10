package com.controlj.green.modstat;

import java.util.Date;

public class DownloadInfo {
    private String product;
    private Date time;
    private String operator;
    private String address;

    public DownloadInfo(String product, Date time, String operator, String address) {
        this.product = product;
        this.time = time;
        this.operator = operator;
        this.address = address;
    }

    public String getProduct() {
        return product;
    }

    public Date getTime() {
        return time;
    }

    public String getOperator() {
        return operator;
    }

    public String getAddress() {
        return address;
    }
}
