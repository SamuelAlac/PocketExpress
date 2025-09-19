package com.alac.pocketexpress.entity;

public class Cart {
    private int cartID, orderID, itemCode, cartItemQuantity;
    private double cartItemPrice;

    public int getCartID() {
        return cartID;
    }

    public void setCartID(int cartID) {
        this.cartID = cartID;
    }

    public int getOrderID() {
        return orderID;
    }

    public void setOrderID(int orderID) {
        this.orderID = orderID;
    }

    public int getItemCode() {
        return itemCode;
    }

    public void setItemCode(int itemCode) {
        this.itemCode = itemCode;
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


    //For Reading
    public Cart(int orderID, int itemCode) {
        this.orderID = orderID;
        this.itemCode = itemCode;
    }

    //For Writing
    public Cart(int orderID, int itemCode, int cartItemQuantity, double cartItemPrice) {
        this.orderID = orderID;
        this.itemCode = itemCode;
        this.cartItemQuantity = cartItemQuantity;
        this.cartItemPrice = cartItemPrice;
    }

    //For Reading
    public Cart(int cartID, int orderID, int itemCode, int cartItemQuantity, double cartItemPrice) {
        this.cartID = cartID;
        this.orderID = orderID;
        this.itemCode = itemCode;
        this.cartItemQuantity = cartItemQuantity;
        this.cartItemPrice = cartItemPrice;
    }
}
