package com.stk.utils;

/**
 * Created by wangl on 2016/11/1.
 */

public class URL {
    //登陆
    public static final String LOGIN="http://114.55.117.10:8080/ES_APP/appUserController/login";
    //学生信息查询
    public static final String STUDENT_INFO="http://114.55.117.10:8080/ES_APP/appUserController/studentInfo";
    //学生通讯查询
    public static final String STUDENT_CONT= "http://114.55.117.10:8080/ES_APP/appUserController/getStudentContacts";
    //学生通讯录模糊查询
    public static final  String STUDENT_TEL="http://114.55.117.10:8080/ES_APP/appUserController/getStudentTel";
    //老师通讯录查询
    public static final  String TEACHER_CONT="http://114.55.117.10:8080/ES_APP/appUserController/getTeacherCont";
    //老师通讯录模糊查询
    public static final  String TEACHER_TEL="http://114.55.117.10:8080/ES_APP/appUserController/getTeacherTel";
    //考勤记录查询
    public static final String ATTENCE_RECODE="http://114.55.117.10:8080/ES_APP/appUserController/attenceRecord";
    //用户密码修改
    public static final String EDIT_PASSWORD="http://114.55.117.10:8080/ES_APP/appUserController/updatePass";
    //用户电话号码修改
    public static final String EDIT_PHONE="http://114.55.117.10:8080/ES_APP/appUserController/updatePhone";
    //软件升级
    public static final String APP_DOWNLOAD="http://114.55.117.10:8080/ES_APP/update/update.xml";
    //班级查询
    public static final String CLASS_LIST="http://114.55.117.10:8080/ES_APP/appUserController/getClassByTea";
    //班级学生查询
    public static final String STUDENT_LIST="http://114.55.117.10:8080/ES_APP/appUserController/getStudentByClass";
    //学生估计查询
    public static final String STUDENT_MAP="http://114.55.117.10:8080/ES_APP/appUserController/queryBikeTrajectoryInfo";
}
