package com.example.lab2_mobiledevelopment.CloudNotifications;

public class Data {
    private String user;

    private String body;
    private String title;
    private String sented;

    public Data(String user, String body, String title, String sented) {
        this.user = user;

        this.body = body;
        this.title = title;
        this.sented = sented;
    }

    public Data() {
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }


    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSented() {
        return sented;
    }

    public void setSented(String sented) {
        this.sented = sented;
    }
}