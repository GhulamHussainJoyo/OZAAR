package com.example.ozaar.spare.parts;

import java.util.HashMap;

public class Parts
{
    private String expectedProfit;
    private String sell;
    private int total;
    private int flag;
    HashMap<String,Object> user_data;

    public Parts()
    {}

    public Parts(String expectedProfit, String sell, int total, int flag, HashMap<String, Object> user_data) {
        this.expectedProfit = expectedProfit;
        this.sell = sell;
        this.total = total;
        this.flag = flag;
        this.user_data = user_data;
    }

    public String getExpectedProfit() {
        return expectedProfit;
    }

    public void setExpectedProfit(String expectedProfit) {
        this.expectedProfit = expectedProfit;
    }

    public String getSell() {
        return sell;
    }

    public void setSell(String sell) {
        this.sell = sell;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public HashMap<String, Object> getUser_data() {
        return user_data;
    }

    public void setUser_data(HashMap<String, Object> user_data) {
        this.user_data = user_data;
    }
}
