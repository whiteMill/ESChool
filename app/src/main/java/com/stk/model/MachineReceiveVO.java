package com.stk.model;

/**
 * Created by wangl on 2016/11/21.
 */

public class MachineReceiveVO {
    private String MACHINE_ADDRESS;
    private String LATITUDE;
    private String LONGITUDE;
    private String BEGIN_TIME;
    private String END_TIME;

    public String getMACHINE_ADDRESS() {
        return MACHINE_ADDRESS;
    }

    public void setMACHINE_ADDRESS(String MACHINE_ADDRESS) {
        this.MACHINE_ADDRESS = MACHINE_ADDRESS;
    }

    public String getLATITUDE() {
        return LATITUDE;
    }

    public void setLATITUDE(String LATITUDE) {
        this.LATITUDE = LATITUDE;
    }

    public String getLONGITUDE() {
        return LONGITUDE;
    }

    public void setLONGITUDE(String LONGITUDE) {
        this.LONGITUDE = LONGITUDE;
    }

    public String getBEGIN_TIME() {
        return BEGIN_TIME;
    }

    public void setBEGIN_TIME(String BEGIN_TIME) {
        this.BEGIN_TIME = BEGIN_TIME;
    }

    public String getEND_TIME() {
        return END_TIME;
    }

    public void setEND_TIME(String END_TIME) {
        this.END_TIME = END_TIME;
    }
}
