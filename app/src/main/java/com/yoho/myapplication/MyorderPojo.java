package com.yoho.myapplication;

public class MyorderPojo {

    private String product_id;
    private String overimage;
    private String id;
    private String datetime;
    private  String p_name;

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getP_name() {
        return p_name;
    }

    public void setP_name(String p_name) {
        this.p_name = p_name;
    }

    public String getOverimage() {
        return overimage;
    }

    public void setOverimage(String overimage) {
        this.overimage = overimage;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }
}
