package com.xdk.develop.df.teacherpart.data;

import java.io.Serializable;

public class StudentLogin implements Serializable {
    private byte[] headPortrait;
    private int grade;
    private int sclass;
    private String name;
	public byte[] getHeadPortrait() {
		return headPortrait;
	}
	public void setHeadPortrait(byte[] headPortrait) {
		this.headPortrait = headPortrait;
	}
	public int getGrade() {
		return grade;
	}
	public void setGrade(int grade) {
		this.grade = grade;
	}
	public int getSclass() {
		return sclass;
	}
	public void setSclass(int sclass) {
		this.sclass = sclass;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
    
}
