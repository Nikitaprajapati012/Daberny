package com.example.raviarchi.daberny.Activity.Model;

import java.util.ArrayList;

/**
 * Created by Archirayan on 16-Jun-17.
 * Archirayan Infotech pvt Ltd
 * dilip.bakotiya@gmail.com || info@archirayan.com
 */
public class getContactListPojo {

    String status,msg;
    ArrayList<dataPojo> data;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public ArrayList<dataPojo> getData() {
        return data;
    }

    public void setData(ArrayList<dataPojo> data) {
        this.data = data;
    }
}
