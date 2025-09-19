package com.alac.pocketexpress.entity;

import android.graphics.Bitmap;

public class Item {
    private int itemCode;
    private byte[] itemImageBlob;
    private String itemName, itemDesc;
    private int itemStock;
    private double itemPrice;


    public int getItemCode() {
        return itemCode;
    }

    public void setItemCode(int itemCode) {
        this.itemCode = itemCode;
    }

    public byte[] getItemImageBlob() {
        return itemImageBlob;
    }

    public void setItemImageBlob(byte[] itemImageBlob) {
        this.itemImageBlob = itemImageBlob;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemDesc() {
        return itemDesc;
    }

    public void setItemDesc(String itemDesc) {
        this.itemDesc = itemDesc;
    }

    public int getItemStock() {
        return itemStock;
    }

    public void setItemStock(int itemStock) {
        this.itemStock = itemStock;
    }

    public double getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(double itemPrice) {
        this.itemPrice = itemPrice;
    }


    public Item(int itemCode) {
        this.itemCode = itemCode;
    }


    public Item(int itemCode, int itemStock) {
        this.itemCode = itemCode;
        this.itemStock = itemStock;
    }


    public Item(byte[] itemImageBlob, String itemName, String itemDesc, int itemStock, double itemPrice) {
        this.itemImageBlob = itemImageBlob;
        this.itemName = itemName;
        this.itemDesc = itemDesc;
        this.itemStock = itemStock;
        this.itemPrice = itemPrice;
    }

    // Constructor for reading (using byte[])
    public Item(int itemCode, byte[] itemImageBlob, String itemName, String itemDesc, int itemStock, double itemPrice) {
        this.itemCode = itemCode;
        this.itemImageBlob = itemImageBlob;
        this.itemName = itemName;
        this.itemDesc = itemDesc;
        this.itemStock = itemStock;
        this.itemPrice = itemPrice;
    }
}
