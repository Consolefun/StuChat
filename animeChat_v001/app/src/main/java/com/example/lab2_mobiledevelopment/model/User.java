package com.example.lab2_mobiledevelopment.model;

public class User {
    private String stu_id;
    private String stu_Firstname;
    private String stu_Lastname;
    private String stu_imageURL;
    private String stu_phonenumber;
    private String stu_search;
    private String stu_status;
    private String stu_dateofBirth;

    public User(String stu_id, String stu_search, String stu_Firstname, String stu_Lastname, String stu_imageURL, String stu_phonenumber, String stu_status, String stu_dateofBirth) {
        this.stu_id = stu_id;
        this.stu_Firstname = stu_Firstname;
        this.stu_Lastname = stu_Lastname;
        this.stu_imageURL = stu_imageURL;
        this.stu_phonenumber = stu_phonenumber;
        this.stu_dateofBirth = stu_dateofBirth;
        this.stu_search = stu_search;
        this.stu_status = stu_status;

    }

    public User() {
    }

    public String getDateofBirth() {
        return stu_dateofBirth;
    }

    public void setDateofBirth(String stu_dateofBirth) {
        this.stu_dateofBirth = stu_dateofBirth;
    }

    public String getPhonenumber(){return stu_phonenumber;}
    public void setPhonenumber(String stu_phonenumber){
        this.stu_phonenumber = stu_phonenumber;
    }
    public String getId() {
        return stu_id;
    }

    public void setId(String stu_id) {
        this.stu_id = stu_id;
    }

    public String getFirstname() {
        return stu_Firstname;
    }

    public void setFirstname(String Firstname) {
        this.stu_Firstname = Firstname;
    }

    public String getLastname() {
        return stu_Lastname;
    }

    public void setLastname(String stu_Lastname) {
        this.stu_Lastname = stu_Lastname;
    }

    public String getImageURL() {
        return stu_imageURL;
    }

    public void setImageURL(String stu_imageURL) {
        this.stu_imageURL = stu_imageURL;
    }

    public String getStu_search() {
        return stu_search;
    }

    public void setStu_search(String stu_search) {
        this.stu_search = stu_search;
    }

    public String getStu_status() {
        return stu_status;
    }

    public void setStu_status(String stu_status) {
        this.stu_status = stu_status;
    }
}
