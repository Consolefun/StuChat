package com.example.lab2_mobiledevelopment.model;

public class Chat {
    private  String stu_sender;
    private String stu_receiver;
    private String stu_message;
//    private boolean stu_seen;

    public Chat(String stu_sender, String stu_receiver, String stu_message) {
        this.stu_sender = stu_sender;
        this.stu_receiver = stu_receiver;
        this.stu_message = stu_message;
//        this.stu_seen = stu_seen;
    }

    public Chat() {
    }

    public String getSender() {
        return stu_sender;
    }

    public void setSender(String stu_sender) {
        this.stu_sender = stu_sender;
    }

    public String getReceiver() {
        return stu_receiver;
    }

    public void setReceiver(String stu_receiver) {
        this.stu_receiver = stu_receiver;
    }

    public String getMessage() {
        return stu_message;
    }

    public void setMessage(String stu_message) {
        this.stu_message = stu_message;
    }


}
