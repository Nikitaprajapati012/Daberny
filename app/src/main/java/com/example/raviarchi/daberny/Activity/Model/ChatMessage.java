package com.example.raviarchi.daberny.Activity.Model;

/**
 * Created by
 * (Ravi archi = "Archirayan Infotech")
 * ($COMPANY = "Archirayan Infotech Pvt Ltd")
 * ($EMAIL = "dilip.bakotiya@gmail.com || info@gmail.com")
 * on (#DATE).
 */

public class ChatMessage {
    public  Message inserted_data;
    public String status,msg;

    public Message getInserted_data() {
        return inserted_data;
    }

    public void setInserted_data(Message inserted_data) {
        this.inserted_data = inserted_data;
    }

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
}
