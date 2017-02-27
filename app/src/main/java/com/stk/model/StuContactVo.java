package com.stk.model;

/**
 * Created by wl on 2016/11/2.
 */

public class StuContactVo {
    /**
     * PHONE : 18258202644
     * STUDENT_ID : 915844dd03ff411ab94bd6f6072f8b19
     * STUDENT_NAME : 测试1号
     */

    private String PHONE;
    private String STUDENT_ID;
    private String STUDENT_NAME;

    public String getPHONE() {
        return PHONE;
    }

    public void setPHONE(String PHONE) {
        this.PHONE = PHONE;
    }

    public String getSTUDENT_ID() {
        return STUDENT_ID;
    }

    public void setSTUDENT_ID(String STUDENT_ID) {
        this.STUDENT_ID = STUDENT_ID;
    }

    public String getSTUDENT_NAME() {
        return STUDENT_NAME;
    }

    public void setSTUDENT_NAME(String STUDENT_NAME) {
        this.STUDENT_NAME = STUDENT_NAME;
    }

    @Override
    public String toString() {
        return "StuContactVo{" +
                "PHONE='" + PHONE + '\'' +
                ", STUDENT_ID='" + STUDENT_ID + '\'' +
                ", STUDENT_NAME='" + STUDENT_NAME + '\'' +
                '}';
    }
}
