package com.example.lab2_mobiledevelopment.CloudNotifications;

import android.widget.Toast;

public class Token {

    private String token;

    public Token(String token){
        this.token = token;

    }

    public Token(){

    }

    public String getToken(){
        return token;
    }

    public void setToken(String token){
        this.token = token;
    }
}
