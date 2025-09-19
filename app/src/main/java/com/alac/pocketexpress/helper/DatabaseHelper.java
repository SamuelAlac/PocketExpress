package com.alac.pocketexpress.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.alac.pocketexpress.entity.Cart;
import com.alac.pocketexpress.entity.CartItem;
import com.alac.pocketexpress.entity.Item;
import com.alac.pocketexpress.entity.Order;
import com.alac.pocketexpress.entity.Staff;

public class DatabaseHelper extends SQLiteOpenHelper {

    private Context context;
    private static final String DATABASE_NAME = "PoketExpress.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_ITEM = "tblItem";
    private static final String ITEM_CODE = "ItemCode";
    private static final String ITEM_IMAGE = "ItemImage";
    private static final String ITEM_NAME = "ItemName";
    private static final String ITEM_DESC = "ItemDesc";
    private static final String ITEM_STOCK = "ItemStock";
    private static final String ITEM_PRICE = "ItemPrice";

    private static final String TABLE_CART = "tblCart";
    private static final String CART_ID = "CartID";
    private static final String FKORDER_ID = "OrderID";
    private static final String FKITEM_CODE = "FKItemCode";
    private static final String CART_ITEM_QUANTITY = "Cart_Item_Quantity";
    private static final String CART_ITEM_PRICE = "Cart_Item_Price";

    private static final String TABLE_ORDER = "tblOrder";
    private static final String ORDER_ID = "OrderID";
    private static final String FKSTAFF_ID = "FKStaffID";
    private static final String ORDER_PRICE = "OrderPrice";
    private static final String ORDER_DATE = "OrderDate";

    private static final String TABLE_STAFF = "tblStaff";
    private static final String STAFF_ID = "StaffID";
    private static final String STAFF_USERNAME = "StaffUsername";
    private static final String STAFF_PASSWORD = "StaffPassword";
    private static final String STAFF_LOGINDATE = "LoginDate";

    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String tableItem =
                " CREATE TABLE " + TABLE_ITEM +
                        " (" +
                        ITEM_CODE + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        ITEM_IMAGE + " BLOB, " +
                        ITEM_NAME + " TEXT, " +
                        ITEM_DESC + " TEXT, " +
                        ITEM_STOCK + " INTEGER, " +
                        ITEM_PRICE + " REAL);";

        String tableStaff =
                " CREATE TABLE " + TABLE_STAFF +
                        " (" +
                        STAFF_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        STAFF_USERNAME + " TEXT, " +
                        STAFF_PASSWORD + " TEXT, " +
                        STAFF_LOGINDATE + " TEXT NOT NULL DEFAULT (datetime(CURRENT_TIMESTAMP, 'localtime')));";

        String tableOrder =
                " CREATE TABLE " + TABLE_ORDER +
                        " (" +
                        ORDER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        FKSTAFF_ID + " INTEGER, " +
                        ORDER_PRICE + " REAL, " +
                        ORDER_DATE + " TEXT, " +

                        "FOREIGN KEY(" + FKSTAFF_ID + ") REFERENCES " + TABLE_STAFF + "(" + STAFF_ID + "));";

        String tableCart =
                " CREATE TABLE " + TABLE_CART +
                        " (" +
                        CART_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        FKORDER_ID + " INTEGER, " +
                        FKITEM_CODE + " INTEGER, " +
                        CART_ITEM_QUANTITY + " INTEGER, " +
                        CART_ITEM_PRICE + " REAL, " +

                        "FOREIGN KEY(" + FKORDER_ID + ") REFERENCES " + TABLE_ORDER + "(" + ORDER_ID + ")," +
                        "FOREIGN KEY(" + FKITEM_CODE + ") REFERENCES " + TABLE_ITEM + "(" + ITEM_CODE + "));";

        db.execSQL(tableItem);
        db.execSQL(tableStaff);
        db.execSQL(tableOrder);
        db.execSQL(tableCart);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ITEM);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_STAFF);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ORDER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CART);
        onCreate(db);
    }

    //For Creating Staff
    public void createStaff(Staff staff) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(STAFF_USERNAME, staff.getStaffUsername());
        cv.put(STAFF_PASSWORD, staff.getStaffPassword());
        cv.put(STAFF_LOGINDATE, staff.getLoginDate());

        long result = db.insert(TABLE_STAFF, null, cv);
        if (result == -1) {
            Toast.makeText(context, "FAILED TO ADD", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "SUCCESSFULLY ADDED", Toast.LENGTH_SHORT).show();
        }
    }

    //For Reading staff account
    public Cursor readStaffAccount(Staff staff){
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_STAFF + " WHERE " + STAFF_USERNAME + "=?" + " AND " + STAFF_PASSWORD + "=?";

        Cursor cursor = null;
        if(db == null){
            Toast.makeText(context, "ACCOUNT NOT FOUND", Toast.LENGTH_SHORT).show();
        }else{
            cursor = db.rawQuery(query, new String[]{staff.getStaffUsername(), staff.getStaffPassword()});
        }

        return cursor;
    }

    //For Updating login date
    public long updateLoginDate(Staff staff){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(STAFF_LOGINDATE,staff.getLoginDate());

        return db.update(TABLE_STAFF, cv, "StaffUsername=? AND StaffPassword=?", new String[]{staff.getStaffUsername(), staff.getStaffPassword()});
    }

    //For Creating items
    public void createItem(Item item) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();


        cv.put(ITEM_IMAGE, item.getItemImageBlob());
        cv.put(ITEM_NAME, item.getItemName());
        cv.put(ITEM_DESC, item.getItemDesc());
        cv.put(ITEM_STOCK, item.getItemStock());
        cv.put(ITEM_PRICE, item.getItemPrice());

        long result = db.insert(TABLE_ITEM, null, cv);
        if (result == -1) {
            Toast.makeText(context, "FAILED TO ADD", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "SUCCESSFULLY ADDED", Toast.LENGTH_SHORT).show();
        }
    }

    //For Reading items
    public Cursor readItems() {
        String query = "SELECT * FROM " + TABLE_ITEM;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if (db != null) {
            cursor = db.rawQuery(query, null);
        }

        return cursor;
    }

    //For Reading item by Code
    public Cursor readItemByCode(String id){
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_ITEM + " WHERE ItemCode = " + id;

        Cursor cursor = null;
        if(db == null){
            Toast.makeText(context, "NO ITEM FOUND", Toast.LENGTH_SHORT).show();
        }else{
            cursor = db.rawQuery(query, null);
        }

        return cursor;
    }

    //For Updating item
    public long updateItem(Item item) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(ITEM_IMAGE, item.getItemImageBlob());
        cv.put(ITEM_NAME, item.getItemName());
        cv.put(ITEM_DESC, item.getItemDesc());
        cv.put(ITEM_STOCK, item.getItemStock());
        cv.put(ITEM_PRICE, item.getItemPrice());

        return db.update(TABLE_ITEM, cv, "ItemCode=?", new String[]{String.valueOf(item.getItemCode())});

    }


    //For deleting all items
    public void deleteAllItems() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_ITEM);
    }

    //For Reading Current Staff
    public Cursor readStaffLoggedIn(){
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT " + STAFF_ID +  " FROM " + TABLE_STAFF + " WHERE " + STAFF_LOGINDATE + " = " + "(SELECT MAX(LoginDate) FROM " + TABLE_STAFF+")";

        Cursor cursor = null;
        if(db == null){
            Toast.makeText(context, "ACCOUNT NOT FOUND", Toast.LENGTH_SHORT).show();
        }else{
            cursor = db.rawQuery(query, null);
        }

        return cursor;
    }

    //For Creating order
    public void createOrder(Order order){
        SQLiteDatabase db = this.getWritableDatabase();


        ContentValues cv = new ContentValues();
        cv.put(FKSTAFF_ID, order.getStaffID());

        long result = db.insert(TABLE_ORDER, null, cv);
        if(result == -1){
            Toast.makeText(context, "FAILED TO CREATE ORDER", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(context, "HAPPY SHOPPING", Toast.LENGTH_SHORT).show();
        }
    }

    public void deleteNullOrder(){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM " + TABLE_ORDER + " WHERE " + ORDER_DATE + " IS NULL ";
        db.execSQL(query);
    }

    //For Reading current OrderID
    public int readCurrentOrderID(){
        SQLiteDatabase db = this.getReadableDatabase();
        String query ="SELECT MAX (" + ORDER_ID + ") FROM " + TABLE_ORDER;

        Cursor cursor;
        int currentOrderID = -1; //Just incase this result failed, i hate return type xd

        if(db != null){
            cursor = db.rawQuery(query, null);
            if(cursor.moveToFirst()){
                if(cursor.isNull(0)){
                    Toast.makeText(context, "FAILED TO READ", Toast.LENGTH_SHORT).show();
                }else{
                    currentOrderID = cursor.getInt(0);
                    cursor.close();
                }
            }else{
                Toast.makeText(context, "NO DATA FOUND", Toast.LENGTH_SHORT).show();
            }

        }
        return currentOrderID;
    }

    //For checking stock
    public int readStock(Item item){
        SQLiteDatabase db = this.getReadableDatabase();

        String checkStock = "SELECT " + ITEM_STOCK + " FROM " + TABLE_ITEM + " WHERE " + ITEM_CODE + "=?";

        int currentQuantity = 0;

        Cursor cursor = null;
        if(db != null){
            cursor = db.rawQuery(checkStock, new String[]{String.valueOf(item.getItemCode())});
            if(cursor.moveToFirst()){
                currentQuantity = cursor.getInt(0);
            }
        }

        return currentQuantity;
    }

    //For updating stock whenever the user adjust item quantity to cart
    public long updateStock(Item item){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(ITEM_STOCK, item.getItemStock());

        return db.update(TABLE_ITEM, cv, ITEM_CODE+"=?", new String[]{String.valueOf(item.getItemCode())});
    }

    //For adding items in cart
    public void createCart(Cart cart){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues cv = new ContentValues();

        cv.put(FKORDER_ID, cart.getOrderID());
        cv.put(FKITEM_CODE, cart.getItemCode());
        cv.put(CART_ITEM_QUANTITY, cart.getCartItemQuantity());
        cv.put(CART_ITEM_PRICE, cart.getCartItemPrice());
        long result = db.insert(TABLE_CART, null, cv);
        if(result == -1){
            Toast.makeText(context, "FAILED TO ORDER", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(context, "SUCCESSFULLY ADDED TO CART", Toast.LENGTH_SHORT).show();
        }
    }

    //For checking if the item already exist in the shopping cart
    public boolean readAddedCartItem(String itemCode) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT  OrderID, FKItemCode FROM tblCart WHERE OrderID = (SELECT MAX(OrderID) FROM tblOrder WHERE FKItemCode =?) ";

        Cursor cursor = null;
        if(db != null){
            cursor = db.rawQuery(query,new String[]{itemCode});
            if(cursor.getCount()> 0){
                return true;
            }
        }else{
            return false;
        }

        return false;
    }

    //For Reading Cart Items
    public Cursor readCartItems(){
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT i.ItemImage, i.ItemName, c.Cart_Item_Quantity, c.Cart_Item_Price, c.FKItemCode " +
                " FROM tblCart c JOIN tblItem i ON c.FKItemCode = i.ItemCode JOIN tblOrder o ON c.OrderID = o.OrderID "+
                " WHERE o.OrderID = (SELECT MAX(OrderID) FROM tblOrder )";

        Cursor cursor = null;
        if(db != null){
            cursor = db.rawQuery(query, null);
        }

        return cursor;
    }


    //For updating Cart Item quantity and price
    public long updateCart(Cart cart){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(CART_ITEM_QUANTITY, cart.getCartItemQuantity());
        cv.put(CART_ITEM_PRICE, cart.getCartItemPrice());
        return db.update(TABLE_CART, cv ,"OrderID=? AND  FKItemCode=?", new String[]{String.valueOf(cart.getOrderID()), String.valueOf(cart.getItemCode())} );
    }

    public void deleteCartItems(){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM " + TABLE_CART + " WHERE OrderID = (SELECT MAX(OrderID) FROM tblCart)";
        db.execSQL(query);
    }

    //For Purchasing Item
    public long purchaseOrder(Order order){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(ORDER_PRICE, order.getOrderPrice());
        cv.put(ORDER_DATE, order.getOrderDateAndTime());

        return db.update(TABLE_ORDER, cv, "OrderID=?", new String[]{String.valueOf(order.getOrderID())});

    }


    //For Getting the Total Price
//    public double readTotalPrice(){
//        SQLiteDatabase db = this.getReadableDatabase();
//        String query = "SELECT " + " SUM("+CART_ITEM_PRICE+") FROM " + TABLE_CART + " WHERE " + ORDER_ID + " = " + " (SELECT MAX(" + ORDER_ID + ") FROM " + TABLE_CART+")";
//
//        double totalPrice = 0;
//
//        Cursor cursor = null;
//        if(db != null){
//            cursor = db.rawQuery(query, null);
//            if(cursor.moveToFirst()){
//                totalPrice = cursor.getDouble(0);
//                return totalPrice;
//            }
//        }else{
//            return totalPrice;
//        }
//
//        return totalPrice;
//    }
//
    //For Reading the Order Total Price
    public Cursor readTotalPrice(){
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT " + " SUM("+CART_ITEM_PRICE+") FROM " + TABLE_CART + " WHERE " + ORDER_ID + " = " + " (SELECT MAX(" + ORDER_ID + ") FROM " + TABLE_CART+")";

        Cursor cursor = null;
        cursor = db.rawQuery(query, null);

        return cursor;
    }

    public Cursor readOrderHistory(){
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT o.OrderID, o.FKStaffID, s.StaffUsername, o.OrderPrice, o.OrderDate\n" +
                "FROM tblOrder o JOIN tblStaff s ON o.FKStaffID = s.StaffID";

        Cursor cursor = null;
        if(db != null){
            cursor = db.rawQuery(query, null);
        }

        return cursor;
    }

    //For Reading Ordered Items in Order history
    public Cursor readOrderedItems(String orderID){
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT i.ItemImage, i.ItemName, c.Cart_Item_Quantity, c.Cart_Item_Price " +
                " FROM tblCart c JOIN tblItem i ON c.FKItemCode = i.ItemCode JOIN tblOrder o ON c.OrderID = o.OrderID "+
                " WHERE o.OrderID = "+ orderID;

        Cursor cursor = null;
        if(db != null){
            cursor = db.rawQuery(query, null);
        }

        return cursor;
    }
}
