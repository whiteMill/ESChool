package com.stk.model;

/**
 * Created by admin on 2016/11/2.
 */

public class ContactUser {
    private String username;
    private String firstCharacter;
    private String phone;

    public ContactUser(String username, String firstCharacter,String phone) {
        this.username = username;
        this.firstCharacter = firstCharacter;
        this.phone = phone;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstCharacter() {
        return firstCharacter;
    }

    public void setFirstCharacter(String firstCharacter) {
        this.firstCharacter = firstCharacter;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
