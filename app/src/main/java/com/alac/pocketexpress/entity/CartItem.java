package com.alac.pocketexpress.entity;

public class CartItem {
    private byte[] itemImageBlob;
    private String itemName;
    private int cartItemQuantity;
    private double cartItemPrice;
    //private int fkOrderID;
    private int fkItemCode;


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

    public int getCartItemQuantity() {
        return cartItemQuantity;
    }

    public void setCartItemQuantity(int cartItemQuantity) {
        this.cartItemQuantity = cartItemQuantity;
    }

    public double getCartItemPrice() {
        return cartItemPrice;
    }

    public void setCartItemPrice(double cartItemPrice) {
        this.cartItemPrice = cartItemPrice;
    }


    public int getFkItemCode() {
        return fkItemCode;
    }

    public void setFkItemCode(int fkItemCode) {
        this.fkItemCode = fkItemCode;
    }


    //Reading bought cart items
    public CartItem(byte[] itemImageBlob, String itemName, int cartItemQuantity, double cartItemPrice) {
        this.itemImageBlob = itemImageBlob;
        this.itemName = itemName;
        this.cartItemQuantity = cartItemQuantity;
        this.cartItemPrice = cartItemPrice;
    }

    //For Reading Cart Items
    public CartItem(byte[] itemImageBlob, String itemName, int cartItemQuantity, double cartItemPrice, int fkItemCode) {
        this.itemImageBlob = itemImageBlob;
        this.itemName = itemName;
        this.cartItemQuantity = cartItemQuantity;
        this.cartItemPrice = cartItemPrice;
        this.fkItemCode = fkItemCode;
    }
}
