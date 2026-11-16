package com.example.w09_sms_monitor;

public class MySMS {
    private int _id;
    private String address;
    private String subject;
    private String body;
    private String date;

    public MySMS() {
    }

    public MySMS(int _id, String address, String subject, String body, String date) {
        this._id = _id;
        this.address = address;
        this.subject = subject;
        this.body = body;
        this.date = date;
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
