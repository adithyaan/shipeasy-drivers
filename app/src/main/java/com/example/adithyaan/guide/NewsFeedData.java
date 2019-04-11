package com.example.adithyaan.guide;

import java.util.HashMap;

/**
 * Created by ADITHYA AN on 03-05-2018.
 */

public class NewsFeedData
{
    String from_location,to_location,customer_name,goods_descp;
    HashMap<String,HashMap<String,String>> bids;


   /* public NewsFeedData(String from_location, String to_location, String customer_name, String goods_descp,HashMap bids) {
        this.from_location = from_location;
        this.to_location = to_location;
        this.customer_name = customer_name;
        this.goods_descp = goods_descp;
        this.bids = bids;
    }*/

    public String getFrom_location() {
        return from_location;
    }

    public String getTo_location() {
        return to_location;
    }

    public String getCustomer_name() {
        return customer_name;
    }

    public String getGoods_descp() {
        return goods_descp;
    }
}
