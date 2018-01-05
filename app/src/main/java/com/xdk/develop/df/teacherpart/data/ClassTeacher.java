package com.xdk.develop.df.teacherpart.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2016/9/27.
 */
public class ClassTeacher implements Parcelable{
    private String schoolCode;
    private String school;
    private String professional;
    private String grade;
    private String sClass;
    private String teacherid;
    private String teacher;
    private String course;
    private String manager;
    private String teachertitle;
    private String telephone;
    private boolean check;

    public boolean isCheck() {
        return check;
    }
    public ClassTeacher(){

    }

    public void setCheck(boolean check) {
        this.check = check;
    }

    protected ClassTeacher(Parcel in) {
        schoolCode = in.readString();
        school = in.readString();
        professional = in.readString();
        grade = in.readString();
        sClass = in.readString();
        teacherid = in.readString();
        teacher = in.readString();
        course = in.readString();
        manager = in.readString();
        teachertitle = in.readString();
        telephone = in.readString();
    }

    public static final Creator<ClassTeacher> CREATOR = new Creator<ClassTeacher>() {
        @Override
        public ClassTeacher createFromParcel(Parcel in) {
            return new ClassTeacher(in);
        }

        @Override
        public ClassTeacher[] newArray(int size) {
            return new ClassTeacher[size];
        }
    };

    public String getSchoolCode() {
        return schoolCode;
    }

    public void setSchoolCode(String schoolCode) {
        this.schoolCode = schoolCode;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public String getProfessional() {
        return professional;
    }

    public void setProfessional(String professional) {
        this.professional = professional;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getsClass() {
        return sClass;
    }

    public void setsClass(String sClass) {
        this.sClass = sClass;
    }

    public String getTeacherid() {
        return teacherid;
    }

    public void setTeacherid(String teacherid) {
        this.teacherid = teacherid;
    }

    public String getTeacher() {
        return teacher;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public String getManager() {
        return manager;
    }

    public void setManager(String manager) {
        this.manager = manager;
    }

    public String getTeachertitle() {
        return teachertitle;
    }

    public void setTeachertitle(String teachertitle) {
        this.teachertitle = teachertitle;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(schoolCode);
        dest.writeString(school);
        dest.writeString(professional);
        dest.writeString(grade);
        dest.writeString(sClass);
        dest.writeString(teacherid);
        dest.writeString(teacher);
        dest.writeString(course);
        dest.writeString(manager);
        dest.writeString(teachertitle);
        dest.writeString(telephone);
    }
}
