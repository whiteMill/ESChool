package com.stk.model;

import java.io.Serializable;

/**
 * Created by admin on 2016/11/2.
 */

public class StudentVo implements Serializable {

    private String STUDENT_NO;
    private String STUDENT_NAME;
    private String STUDENT_CARD_NO;
    private String STUDENT_GENDER;
    private int STUDENT_AGE;
    private String STUDENT_IMG;
    private String PHONE;
    private String CLASS_ID;
    private String CLASS_NAME ;
    private String SCHOOL_ID;
    private String SCHOOL_NAME;
    private String CREATE_TIME;
    private String UPDATE_TIME;
    private String STUDENT_ID;

    public String getCLASS_ID() {
        return CLASS_ID;
    }

    public void setCLASS_ID(String CLASS_ID) {
        this.CLASS_ID = CLASS_ID;
    }

    public String getSCHOOL_ID() {
        return SCHOOL_ID;
    }

    public void setSCHOOL_ID(String SCHOOL_ID) {
        this.SCHOOL_ID = SCHOOL_ID;
    }

    public String getSTUDENT_NO() {
        return STUDENT_NO;
    }

    public void setSTUDENT_NO(String STUDENT_NO) {
        this.STUDENT_NO = STUDENT_NO;
    }

    public String getSTUDENT_NAME() {
        return STUDENT_NAME;
    }

    public void setSTUDENT_NAME(String STUDENT_NAME) {
        this.STUDENT_NAME = STUDENT_NAME;
    }

    public String getSTUDENT_CARD_NO() {
        return STUDENT_CARD_NO;
    }

    public void setSTUDENT_CARD_NO(String STUDENT_CARD_NO) {
        this.STUDENT_CARD_NO = STUDENT_CARD_NO;
    }

    public String getSTUDENT_GENDER() {
        return STUDENT_GENDER;
    }

    public void setSTUDENT_GENDER(String STUDENT_GENDER) {
        this.STUDENT_GENDER = STUDENT_GENDER;
    }

    public int getSTUDENT_AGE() {
        return STUDENT_AGE;
    }

    public void setSTUDENT_AGE(int STUDENT_AGE) {
        this.STUDENT_AGE = STUDENT_AGE;
    }

    public String getSTUDENT_IMG() {
        return STUDENT_IMG;
    }

    public void setSTUDENT_IMG(String STUDENT_IMG) {
        this.STUDENT_IMG = STUDENT_IMG;
    }

    public String getPHONE() {
        return PHONE;
    }

    public void setPHONE(String PHONE) {
        this.PHONE = PHONE;
    }

    public String getCLASS_NAME() {
        return CLASS_NAME;
    }

    public void setCLASS_NAME(String CLASS_NAME) {
        this.CLASS_NAME = CLASS_NAME;
    }

    public String getSCHOOL_NAME() {
        return SCHOOL_NAME;
    }

    public void setSCHOOL_NAME(String SCHOOL_NAME) {
        this.SCHOOL_NAME = SCHOOL_NAME;
    }

    public String getCREATE_TIME() {
        return CREATE_TIME;
    }

    public void setCREATE_TIME(String CREATE_TIME) {
        this.CREATE_TIME = CREATE_TIME;
    }

    public String getUPDATE_TIME() {
        return UPDATE_TIME;
    }

    public void setUPDATE_TIME(String UPDATE_TIME) {
        this.UPDATE_TIME = UPDATE_TIME;
    }

    public String getSTUDENT_ID() {
        return STUDENT_ID;
    }

    public void setSTUDENT_ID(String STUDENT_ID) {
        this.STUDENT_ID = STUDENT_ID;
    }

    @Override
    public String toString() {
        return "StudentVo{" +
                "STUDENT_NO='" + STUDENT_NO + '\'' +
                ", STUDENT_NAME='" + STUDENT_NAME + '\'' +
                ", STUDENT_CARD_NO='" + STUDENT_CARD_NO + '\'' +
                ", STUDENT_GENDER='" + STUDENT_GENDER + '\'' +
                ", STUDENT_AGE=" + STUDENT_AGE +
                ", STUDENT_IMG='" + STUDENT_IMG + '\'' +
                ", PHONE='" + PHONE + '\'' +
                ", CLASS_ID='" + CLASS_ID + '\'' +
                ", CLASS_NAME='" + CLASS_NAME + '\'' +
                ", SCHOOL_ID='" + SCHOOL_ID + '\'' +
                ", SCHOOL_NAME='" + SCHOOL_NAME + '\'' +
                ", CREATE_TIME='" + CREATE_TIME + '\'' +
                ", UPDATE_TIME='" + UPDATE_TIME + '\'' +
                ", STUDENT_ID='" + STUDENT_ID + '\'' +
                '}';
    }
}
