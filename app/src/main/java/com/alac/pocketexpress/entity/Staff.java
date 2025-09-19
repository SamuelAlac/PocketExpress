package com.alac.pocketexpress.entity;

public class Staff {
    private int staffID;
    private String staffUsername, staffPassword, loginDate;

    public int getStaffID() {
        return staffID;
    }

    public void setStaffID(int staffID) {
        this.staffID = staffID;
    }

    public String getStaffUsername() {
        return staffUsername;
    }

    public void setStaffUsername(String staffUsername) {
        this.staffUsername = staffUsername;
    }

    public String getStaffPassword() {
        return staffPassword;
    }

    public void setStaffPassword(String staffPassword) {
        this.staffPassword = staffPassword;
    }

    public String getLoginDate() {
        return loginDate;
    }

    public void setLoginDate(String loginDate) {
        this.loginDate = loginDate;
    }

    //For Reading
    public Staff(String staffUsername, String staffPassword) {
        this.staffUsername = staffUsername;
        this.staffPassword = staffPassword;
    }

    //For Writing
    public Staff(String staffUsername, String staffPassword, String loginDate) {
        this.staffUsername = staffUsername;
        this.staffPassword = staffPassword;
        this.loginDate = loginDate;
    }

    //For Reading
    public Staff(int staffID, String staffUsername, String staffPassword, String loginDate) {
        this.staffID = staffID;
        this.staffUsername = staffUsername;
        this.staffPassword = staffPassword;
        this.loginDate = loginDate;
    }
}
