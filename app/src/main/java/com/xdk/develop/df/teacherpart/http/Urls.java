package com.xdk.develop.df.teacherpart.http;

/**
 * Created by Administrator on 2016/10/24.
 */
public class Urls {
    public static final String BASEURL = "http://106.14.68.153:809/xdkWeb/app";
    public static String loginUrlNoCode(){
        return BASEURL+"/teacher/login/noCode";
    }
    public static String sendCode(){
        return BASEURL+"/phone/sendcode";
    }
    public static String loginUrl(){
        return BASEURL+"/teacher/login";
    }
    public static String studentsList(){
        return BASEURL+"/teacher/studentsList";
    }
    public static String studentsListLike(){
        return BASEURL+"/teacher/serchStudents";
    }
    public static String teacherClass(){
        return BASEURL+"/teacher/teacherClass";
    }
    public static String sendStudentsMessage(){
        return BASEURL+"/teacher/sendStudentsMessage";
    }
    public static String userInfo(){
        return BASEURL+"/student/infomation";
    }
    public static String sendClassMessage(){
        return BASEURL+"/teacher/sendClassMessage";
    }
    public static String askLeaveStudents(){
        return BASEURL+"/teacher/studentsAskleave";
    }
    public static String askLeaveTeacher(){
        return BASEURL+"/teacher/teacherAskLeave";
    }
    public static String changePass(){
        return BASEURL+"/user/changePass";
    }
}
