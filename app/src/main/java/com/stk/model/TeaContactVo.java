package com.stk.model;

/**
 * Created by admin on 2016/11/3.
 */

public class TeaContactVo {

    /**
     * PHONE : 131167109979
     * USER_ID : cf451805bf484d8999ea3763483da82d
     * USERNAME : peipei
     * NAME : 佩佩
     */

    private String PHONE;
    private String USER_ID;
    private String USERNAME;
    private String NAME;

    public String getPHONE() {
        return PHONE;
    }

    public void setPHONE(String PHONE) {
        this.PHONE = PHONE;
    }

    public String getUSER_ID() {
        return USER_ID;
    }

    public void setUSER_ID(String USER_ID) {
        this.USER_ID = USER_ID;
    }

    public String getUSERNAME() {
        return USERNAME;
    }

    public void setUSERNAME(String USERNAME) {
        this.USERNAME = USERNAME;
    }

    public String getNAME() {
        return NAME;
    }

    public void setNAME(String NAME) {
        this.NAME = NAME;
    }

    @Override
    public String toString() {
        return "TeaContactVo{" +
                "PHONE='" + PHONE + '\'' +
                ", USER_ID='" + USER_ID + '\'' +
                ", USERNAME='" + USERNAME + '\'' +
                ", NAME='" + NAME + '\'' +
                '}';
    }
}
