package com.stk.model;

import java.io.Serializable;

/**
 * Created by wl on 2016/10/26.
 */
public class User implements Serializable{
    private String USER_ID;
    private String USERNAME;
    private String PASSWORD;
    private String NAME;
    private String RIGHTS;
    private String ROLE_ID;
    private String LAST_LOGIN;
    private String IP;
    private String STATUS;
    private String BZ;
    private String PHONE;
    private String SFID;
    private String START_TIME;
    private String END_TIME;
    private String YEARS;
    private String NUMBERNO;
    private String EMAIL;
    private String TYPE;
    private String SCHOOL_ID;
    private String STUDENT_ID;

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

    public String getPASSWORD() {
        return PASSWORD;
    }

    public void setPASSWORD(String PASSWORD) {
        this.PASSWORD = PASSWORD;
    }

    public String getNAME() {
        return NAME;
    }

    public void setNAME(String NAME) {
        this.NAME = NAME;
    }

    public String getRIGHTS() {
        return RIGHTS;
    }

    public void setRIGHTS(String RIGHTS) {
        this.RIGHTS = RIGHTS;
    }

    public String getROLE_ID() {
        return ROLE_ID;
    }

    public void setROLE_ID(String ROLE_ID) {
        this.ROLE_ID = ROLE_ID;
    }

    public String getLAST_LOGIN() {
        return LAST_LOGIN;
    }

    public void setLAST_LOGIN(String LAST_LOGIN) {
        this.LAST_LOGIN = LAST_LOGIN;
    }

    public String getIP() {
        return IP;
    }

    public void setIP(String IP) {
        this.IP = IP;
    }

    public String getSTATUS() {
        return STATUS;
    }

    public void setSTATUS(String STATUS) {
        this.STATUS = STATUS;
    }

    public String getBZ() {
        return BZ;
    }

    public void setBZ(String BZ) {
        this.BZ = BZ;
    }

    public String getPHONE() {
        return PHONE;
    }

    public void setPHONE(String PHONE) {
        this.PHONE = PHONE;
    }

    public String getSFID() {
        return SFID;
    }

    public void setSFID(String SFID) {
        this.SFID = SFID;
    }

    public String getSTART_TIME() {
        return START_TIME;
    }

    public void setSTART_TIME(String START_TIME) {
        this.START_TIME = START_TIME;
    }

    public String getEND_TIME() {
        return END_TIME;
    }

    public void setEND_TIME(String END_TIME) {
        this.END_TIME = END_TIME;
    }

    public String getYEARS() {
        return YEARS;
    }

    public void setYEARS(String YEARS) {
        this.YEARS = YEARS;
    }

    public String getNUMBERNO() {
        return NUMBERNO;
    }

    public void setNUMBERNO(String NUMBERNO) {
        this.NUMBERNO = NUMBERNO;
    }

    public String getEMAIL() {
        return EMAIL;
    }

    public void setEMAIL(String EMAIL) {
        this.EMAIL = EMAIL;
    }

    public String getTYPE() {
        return TYPE;
    }

    public void setTYPE(String TYPE) {
        this.TYPE = TYPE;
    }

    public String getSCHOOL_ID() {
        return SCHOOL_ID;
    }

    public void setSCHOOL_ID(String SCHOOL_ID) {
        this.SCHOOL_ID = SCHOOL_ID;
    }

    public String getSTUDENT_ID() {
        return STUDENT_ID;
    }

    public void setSTUDENT_ID(String STUDENT_ID) {
        this.STUDENT_ID = STUDENT_ID;
    }

    @Override
    public String toString() {
        return "User{" +
                "USER_ID='" + USER_ID + '\'' +
                ", USERNAME='" + USERNAME + '\'' +
                ", PASSWORD='" + PASSWORD + '\'' +
                ", NAME='" + NAME + '\'' +
                ", RIGHTS='" + RIGHTS + '\'' +
                ", ROLE_ID='" + ROLE_ID + '\'' +
                ", LAST_LOGIN='" + LAST_LOGIN + '\'' +
                ", IP='" + IP + '\'' +
                ", STATUS='" + STATUS + '\'' +
                ", BZ='" + BZ + '\'' +
                ", PHONE='" + PHONE + '\'' +
                ", SFID='" + SFID + '\'' +
                ", START_TIME='" + START_TIME + '\'' +
                ", END_TIME='" + END_TIME + '\'' +
                ", YEARS='" + YEARS + '\'' +
                ", NUMBERNO='" + NUMBERNO + '\'' +
                ", EMAIL='" + EMAIL + '\'' +
                ", TYPE='" + TYPE + '\'' +
                ", SCHOOL_ID='" + SCHOOL_ID + '\'' +
                ", STUDENT_ID='" + STUDENT_ID + '\'' +
                '}';
    }
}
