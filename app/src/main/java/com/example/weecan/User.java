package com.example.weecan;

import java.io.Serializable;

public class User implements Serializable {
    private String name;
    private String email;
    private String password;
    private String address;
    private String more;
    private String phoneN;
    private String Housephone;

    public User(){}
    public User(String name,String email, String phoneN) {
        this.name = name;
        this.email = email;
        this.phoneN = phoneN;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPhoneN() {
        return phoneN;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhoneN(String phoneN) {
        this.phoneN = phoneN;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getMore() {
        return more;
    }

    public void setMore(String more) {
        this.more = more;
    }

    public String getHousephone() {
        return Housephone;
    }

    public void setHousephone(String housephone) {
        Housephone = housephone;
    }
}