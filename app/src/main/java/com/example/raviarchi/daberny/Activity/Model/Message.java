package com.example.raviarchi.daberny.Activity.Model;

/**
 * Created by
 * (Ravi archi = "Archirayan Infotech")
 * ($COMPANY = "Archirayan Infotech Pvt Ltd")
 * ($EMAIL = "dilip.bakotiya@gmail.com || info@gmail.com")
 * on (#DATE).
 */

public class Message {
    public SenderMessage sender_detail;
    public SenderMessage receiver_detail;
    public String msg,type,s;

    public SenderMessage getSender_detail() {
        return sender_detail;
    }

    public void setSender_detail(SenderMessage sender_detail) {
        this.sender_detail = sender_detail;
    }

    public SenderMessage getReceiver_detail() {
        return receiver_detail;
    }

    public void setReceiver_detail(SenderMessage receiver_detail) {
        this.receiver_detail = receiver_detail;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
