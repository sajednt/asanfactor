package com.sajednt.html2pdf.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import com.sajednt.html2pdf.constant.Constant;
import com.sajednt.html2pdf.item.itemFactor;
import com.sajednt.html2pdf.item.itemListFactor;
import com.sajednt.html2pdf.item.itemSeller;

import java.util.ArrayList;
import java.util.List;

public class Database extends SQLiteOpenHelper {


    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "report";
    public Context context;
    public static final String TABLE_items = "factoritems";
    public static final String TABLE_factors = "factors";
    public static final String TABLE_sellers = "sellers";
    public static final String TABLE_customers = "customers";

    public static final String TABLE_products = "products";

    public static final String CREATE_TABLE_PRODUCTS = "CREATE TABLE " + TABLE_products + " ( "
            + Constant.id +         " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + Constant.description +" TEXT, "
            + Constant.price +      " INTEGER, "
            + Constant.info +       " TEXT "
            + ")";


    public static final String CREATE_TABLE_ITEMS = "CREATE TABLE " + TABLE_items + " ( "
            + Constant.id +         " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + Constant.description +" TEXT, "
            + Constant.factorId +   " INTEGER, "
            + Constant.count +      " INTEGER, "
            + Constant.price +      " INTEGER, "
            + Constant.total +      " INTEGER, "
            + Constant.info +       " TEXT "
            + ")";

    public static final String CREATE_TABLE_FACTORS55 = "CREATE TABLE " + TABLE_factors + " ( "
            + Constant.id +         " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + Constant.price +      " TEXT , "
            + Constant.title +      " TEXT, "
            + Constant.subtitle +   " TEXT, "
            + Constant.number +     " INTEGER, "
            + Constant.date +       " TEXT, "
            + Constant.sellerid +   " INTEGER, "
            + Constant.customerid + " INTEGER, "
            + Constant.discount +   " INTEGER, "
            + Constant.discountPercent +    " BOOLEAN ,"
            + Constant.tax +        " INTEGER, "
            + Constant.taxPercent + " BOOLEAN ,"
            + Constant.info +       " TEXT "
            + ")";

    public static final String CREATE_TABLE_SELLERS = "CREATE TABLE " + TABLE_sellers + " ( "
            + Constant.id +     " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + Constant.name +   " TEXT, "
            + Constant.phone +  " TEXT, "
            + Constant.code +   " TEXT, "
            + Constant.zip +    " TEXT, "
            + Constant.address +" TEXT, "
            + Constant.storeName +" TEXT "
            + ")";

    public static final String CREATE_TABLE_CUSTOMERS = "CREATE TABLE " + TABLE_customers + " ( "
            + Constant.id +     " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + Constant.name +   " TEXT, "
            + Constant.phone +  " TEXT, "
            + Constant.code +   " TEXT, "
            + Constant.zip +    " TEXT, "
            + Constant.address +" TEXT "
            + ")";

    public Database(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;

    }

    @Override
    public void onCreate(SQLiteDatabase db) {


        db.execSQL(CREATE_TABLE_FACTORS55);
        db.execSQL(CREATE_TABLE_PRODUCTS);
        db.execSQL(CREATE_TABLE_ITEMS);
        db.execSQL(CREATE_TABLE_SELLERS);
        db.execSQL(CREATE_TABLE_CUSTOMERS);

        Log.e("dbHelper" , "Tables was Created");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public boolean sellerExist(String name){

        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT * FROM " + TABLE_sellers;

        Cursor c = db.rawQuery(selectQuery, null);
        c.moveToFirst();

        if(c.getCount() > 0) {
            return true;
        }
        return false;
    }

    public boolean productExist(String name){

        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT * FROM " + TABLE_products + " WHERE description = '" + name + "'";

        Cursor c = db.rawQuery(selectQuery, null);
        c.moveToFirst();

        if(c.getCount() > 0) {
            return true;
        }
        return false;

    }

    public itemSeller getSeller(){

        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT * FROM " + TABLE_sellers +" where id = 1";

        Cursor c = db.rawQuery(selectQuery, null);
        c.moveToFirst();

        itemSeller is= new itemSeller();

        do {
            is.setName(c.getString(c.getColumnIndex(Constant.name)));
            is.setPhone(c.getString(c.getColumnIndex(Constant.phone)));
            is.setCode(c.getString(c.getColumnIndex(Constant.code)));
            is.setZip(c.getString(c.getColumnIndex(Constant.zip)));
            is.setAddress(c.getString(c.getColumnIndex(Constant.address)));
            is.setStoreName(c.getString(c.getColumnIndex(Constant.storeName)));

        }while (false);

        return is;
    }

    public void addSeller(itemSeller seller){

        SQLiteDatabase db;
        db = this.getReadableDatabase();

        ContentValues values = new ContentValues();
        values.put(Constant.name , seller.getName());
        values.put(Constant.phone , seller.getPhone());
        values.put(Constant.code , seller.getCode());
        values.put(Constant.zip , seller.getZip());
        values.put(Constant.address , seller.getAddress());
        values.put(Constant.storeName , seller.getStoreName());

        if(sellerExist(seller.getName())){
            db.update(TABLE_sellers, values , Constant.id + " = ?", new String[]{"1"});
            Log.e("dbHelper", "seller updated");
        }
        else{
            db.insert(TABLE_sellers, null, values);
            Log.e("dbHelper", "seller new inserted");
        }

    }



    public Boolean addProduct(itemListFactor ilf){

        SQLiteDatabase db;
        db = this.getReadableDatabase();

        ContentValues values = new ContentValues();
        values.put(Constant.description , ilf.getDescription());
        values.put(Constant.price , ilf.getPrice());
        values.put(Constant.info , ilf.getInfo());


        if(productExist(ilf.getDescription())){
//            db.update(TABLE_sellers, values , Constant.id + " = ?", new String[]{"1"});
//            Log.e("dbHelper", "seller updated");
            return false;
        }
        else{
            db.insert(TABLE_products, null, values);
            Log.e("dbHelper", "product new inserted");
            return  true;
        }

    }

    public List<itemListFactor> getAllProduct(){

        List<itemListFactor> item = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT  * FROM " + TABLE_products +" order by id desc";

        Cursor c = db.rawQuery(selectQuery, null);
        c.moveToFirst();

        if(c.getCount()>0) {
            do {
                itemListFactor i = new itemListFactor();

                i.setId(c.getString(0));
                i.setDescription(c.getString(1));
                i.setPrice(c.getString(2));
                i.setInfo(c.getString(3));

                item.add(i);


            } while (c.moveToNext());
        }

        return item;
    }

    public String addCustomer(itemSeller is){

        SQLiteDatabase db;
        db = this.getReadableDatabase();

        ContentValues values = new ContentValues();
        values.put(Constant.name , is.getName());
        values.put(Constant.phone , is.getPhone());
        values.put(Constant.code , is.getCode());
        values.put(Constant.zip , is.getZip());
        values.put(Constant.address , is.getAddress());
        long  id = db.insert(TABLE_customers, null, values);

        return Long.toString(id);
    }

    public int factorExist(String number){

        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT * FROM " + TABLE_factors + " WHERE number = '" + number + "'";

        Cursor c = db.rawQuery(selectQuery, null);
        c.moveToFirst();

        if(c.getCount() > 0) {
            int x = c.getInt(c.getColumnIndex("id"));
            return x;
        }
        return 0;

    }

    public String addFactor(itemFactor ifa){

        SQLiteDatabase db;
        db = this.getReadableDatabase();

        ContentValues values = new ContentValues();
        values.put(Constant.title , ifa.getTitle());
        values.put(Constant.subtitle , ifa.getSubtitle());
        values.put(Constant.number , ifa.getNumber());
        values.put(Constant.date , ifa.getDate());
        values.put(Constant.sellerid , ifa.getSellerId());
        values.put(Constant.customerid , ifa.getCustomerId());
        values.put(Constant.discount , ifa.getDiscount());
        values.put(Constant.discountPercent , ifa.getDiscountPercent());
        values.put(Constant.tax , ifa.getTax());
        values.put(Constant.taxPercent , ifa.getTaxPercent());
        values.put(Constant.price , ifa.getPrice());
        values.put(Constant.info , ifa.getInfo());

        if(factorExist(ifa.getNumber()) != 0){
            db.update(TABLE_factors, values , Constant.number + " = ?", new String[]{ifa.getNumber()});
            Log.e("dbHelper", "factor updated");
            return Integer.toString(factorExist(ifa.getNumber()));
        }
        else{
             long cs = db.insert(TABLE_factors, null, values);
            Log.e("dbHelper", "factor new inserted");
            return Long.toString(cs);
        }
    }

    public void deleteAllFactorItems(String factorId){

        SQLiteDatabase db;
        db = this.getReadableDatabase();

        db.delete(TABLE_items, "factorid = "+ factorId, null);
    }

    public Boolean addFoctorItems(List<itemListFactor> listItem , String factorid){

        SQLiteDatabase db;
        db = this.getReadableDatabase();


        try {
            for (itemListFactor ilf: listItem) {
                ContentValues values = new ContentValues();
                values.put(Constant.description , ilf.getDescription());
                values.put(Constant.factorId , factorid);
                values.put(Constant.count , ilf.getCount());
                values.put(Constant.price , ilf.getPrice());
                values.put(Constant.info , ilf.getInfo());
                db.insert(TABLE_items, null, values);
            }
            return true;
        }
        catch (Exception e){
            return false;
        }
    }

    public List<itemFactor> getAllFactores(){

        SQLiteDatabase db = this.getReadableDatabase();
        List<itemFactor> listItem = new ArrayList<>();

        String selectQuery = "select count(factors.id) as factorcount , factors.id , factors.title , factors.subtitle , factors.number , factors.date , factors.price , sellers.name as sellername , customers.name as customername from factors, factoritems, sellers, customers where factors.id = factoritems.factorid and factors.sellerid = sellers.id and factors.customerid = customers.id GROUP by factors.id order by factors.id DESC";

        Cursor c = db.rawQuery(selectQuery, null);
        if(c.moveToFirst()) {
            do {
                itemFactor ifa = new itemFactor();
                ifa.setCount(c.getString(c.getColumnIndex("factorcount")));
                ifa.setId(c.getString(c.getColumnIndex(Constant.id)));
                ifa.setTitle(c.getString(c.getColumnIndex(Constant.title)));
                ifa.setSubtitle(c.getString(c.getColumnIndex(Constant.subtitle)));
                ifa.setNumber(c.getString(c.getColumnIndex(Constant.number)));
                ifa.setDate(c.getString(c.getColumnIndex(Constant.date)));
                ifa.setPrice(c.getString(c.getColumnIndex(Constant.price)));
                ifa.setSellerId(c.getString(c.getColumnIndex("sellername")));
                ifa.setCustomerId(c.getString(c.getColumnIndex("customername")));

                listItem.add(ifa);
            } while (c.moveToNext());
        }
        else{

        }
        return listItem;
    }

    public int getLastNumber(){

        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT * FROM " + TABLE_factors;

        Cursor c = db.rawQuery(selectQuery, null);
        c.moveToFirst();

        if(c.getCount() > 0)
            return 1000 + c.getCount() + 1;


        return 1001;
    }
}
