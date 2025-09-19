package com.alac.pocketexpress.entity;

public class Order {
    private int orderID, staffID;
    private String staffName;
    private double orderPrice;
    private String orderDateAndTime;

    public int getOrderID() {
        return orderID;
    }

    public void setOrderID(int orderID) {
        this.orderID = orderID;
    }

    public int getStaffID() {
        return staffID;
    }

    public String getStaffName() {
        return staffName;
    }

    public void setStaffName(String staffName) {
        this.staffName = staffName;
    }

    public void setStaffID(int staffID) {
        this.staffID = staffID;
    }

    public double getOrderPrice() {
        return orderPrice;
    }

    public void setOrderPrice(double orderPrice) {
        this.orderPrice = orderPrice;
    }

    public String getOrderDateAndTime(){ return orderDateAndTime; }

    public void setOrderDateAndTime(String orderDateAndTime){ this.orderDateAndTime = orderDateAndTime; }


    //For Reading
    public Order(int staffID) {
        this.staffID = staffID;
    }

    //For Writing
    public Order(int orderID, double orderPrice, String orderDateAndTime) {
        this.orderID = orderID;
        this.orderPrice = orderPrice;
        this.orderDateAndTime = orderDateAndTime;
    }

    //For Reading
    public Order(int orderID, int staffID, double orderPrice, String orderDateAndTime) {
        this.orderID = orderID;
        this.staffID = staffID;
        this.orderPrice = orderPrice;
        this.orderDateAndTime = orderDateAndTime;
    }

    //For Reading
    public Order(int orderID, int staffID, String staffName, double orderPrice, String orderDateAndTime) {
        this.orderID = orderID;
        this.staffID = staffID;
        this.staffName = staffName;
        this.orderPrice = orderPrice;
        this.orderDateAndTime = orderDateAndTime;
    }
}
