package com.yoho.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbHandler extends SQLiteOpenHelper {

    public  static Context mcontext;
    private static final String DATABASE_NAME="yohela";
    private static final String TABLE_NAME="user_address";
    private static final String CART_TABLE_NAME="cart_table";
    private static final String WISHLIST_TABLE_NAME="wishlist_table";

    private static final int DATABASE_VERSION=1;
    private static  final String address_type="address_type";
    private static  final String name="name";
    private static  final String mobile="phone_number";
    private static  final String pincode="pincode";
    private static  final String city="city";
    private static  final String state="state";
    private static  final String area="area";
    private static  final String address="address";
    private static  final String landmark="landmark";
    private static  final String alternate_no="alternate_no";

    private static  final String ID_CART="id";
    private static  final String COUNT_CART="count";
    private static  final String COUNT_DATE="datetime";
    private static  final String SIZE="size";

    private static  final String WISHLIST_ID="whishlist_id";


    public DbHandler( Context context) {
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
        mcontext=context;
    }
//this method is used to create table
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("Create table "+TABLE_NAME+"(id INTEGER PRIMARY KEY AUTOINCREMENT,address_type VARCHAR,name VARCHAR,pincode VARCHAR,city VARCHAR,state VARCHAR,area VARCHAR,address VARCHAR,landmark VARCHAR,"+mobile+" VARCHAR,alternate_no VARCHAR);");
        db.execSQL(" Create table "+CART_TABLE_NAME+"(id INTEGER,count INTEGER,datetime VARCHAR,size VARCHAR)");
        db.execSQL(" Create table "+WISHLIST_TABLE_NAME+"(whishlist_id INTEGER)");

    }
//if table already exists ..this method is used to drop
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS "+CART_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS "+WISHLIST_TABLE_NAME);

        onCreate(db);

    }


   //updating cart count
public Boolean updateCount( int cart_id,int cart_count)
{
    SQLiteDatabase database=this.getWritableDatabase();
    int cartCount=cart_count;
  int cartId=cart_id;

     database.execSQL("UPDATE cart_table SET count="+cart_count+" WHERE id="+cartId+"");

   return true;
}
    //inserting wishlist item
    public  Boolean insert_wishlistitem(int cart_id)
    {

        SQLiteDatabase database=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put(WISHLIST_ID,cart_id);

        //checking table is empty or not
        String count = "SELECT count(*) FROM "+WISHLIST_TABLE_NAME;
        Cursor mcursor = database.rawQuery(count, null);
        if(mcursor !=null) {
            mcursor.moveToFirst();
            int icount = mcursor.getInt(0);
            if (icount > 0) {

                Cursor check = database.rawQuery("SELECT * FROM " + WISHLIST_TABLE_NAME + " WHERE whishlist_id= " + cart_id, null);
                if (check.moveToNext()) {

                   return true;

                } else {
                    long result = database.insert(WISHLIST_TABLE_NAME, null, contentValues);
                    if (result == 1) {
                        return false;
                    } else {
                        return true;
                    }
                }
            } else {

                long result = database.insert(WISHLIST_TABLE_NAME, null, contentValues);
                if (result == 1) {
                    return false;
                } else {
                    return true;
                }
            }
        }else
        {
            return false;
        }



    }

    //getting wishlist item...
    public Cursor getWishlistItem()
    {
        SQLiteDatabase database=this.getWritableDatabase();
        Cursor cursor=database.rawQuery("SELECT * FROM "+WISHLIST_TABLE_NAME ,null);
        return cursor;
    }

    //inserting cartitem
    public  Boolean insert_cartitem(int cart_id,int cart_count,String date,String sizedb)
    {
        SQLiteDatabase database=this.getWritableDatabase();
        int id=cart_id;
        String sizedb_var=sizedb;

        //checking table is empty or not
        String count = "SELECT count(*) FROM "+CART_TABLE_NAME;
        Cursor mcursor = database.rawQuery(count, null);
        if(mcursor !=null) {


            mcursor.moveToFirst();
            int icount = mcursor.getInt(0);

            /*if (icount > 0) {*/

                Cursor check = database.rawQuery("SELECT * FROM " + CART_TABLE_NAME + " WHERE id = " + id +" AND size = " + sizedb_var, null);

                if (check.moveToNext()) {
                    int sample = check.getInt(1);
                    sample=sample+cart_count;
                    String datetimedata=date;
                    database.execSQL("UPDATE cart_table SET count="+sample+" WHERE id="+id+"");
                    return true;

                }
                else {
                    ContentValues contentValues=new ContentValues();
                    contentValues.put(ID_CART,cart_id);
                    contentValues.put(COUNT_CART,cart_count);
                    contentValues.put(COUNT_DATE,date);
contentValues.put(SIZE,sizedb);
                    long result = database.insert(CART_TABLE_NAME, null, contentValues);
                    if (result == -1) {
                        return false;
                    } else {
                        return true;
                    }
                }


          /*  } else {
                ContentValues contentValues=new ContentValues();
                contentValues.put(ID_CART,cart_id);
                contentValues.put(COUNT_CART,cart_count);
                contentValues.put(COUNT_DATE,date);


                long result = database.insert(CART_TABLE_NAME, null, contentValues);
                if (result == 1) {
                    return false;
                } else {
                    return true;
                }

            }*/
        }
        else
        {

            ContentValues contentValues=new ContentValues();
            contentValues.put(ID_CART,cart_id);
            contentValues.put(COUNT_CART,cart_count);
            contentValues.put(COUNT_DATE,date);
            contentValues.put(SIZE,sizedb);

            long result = database.insert(CART_TABLE_NAME, null, contentValues);
            if (result == -1) {
                return false;
            } else {
                return true;
            }

        }
//populate table



      /*  long result=database.insert(CART_TABLE_NAME,null,contentValues);
        if(result==1)
        {
            return false;
        }
        else
        {
            return true;
        }*/


    }


    //getting cart item...
    public Cursor getCartItem()
    {
        SQLiteDatabase database=this.getWritableDatabase();
        Cursor cursor=database.rawQuery("SELECT * FROM "+CART_TABLE_NAME ,null);
        return cursor;
    }


    //this method is used for insert
    public Boolean insertData(String address_type1,String name1,String Phone_number1,String pincode1,String city1,String state1,String area1,String address1,String landmark1,String alternate_no1)
    {
        SQLiteDatabase database=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put(address_type,address_type1);
        contentValues.put(name,name1);
        contentValues.put(pincode,pincode1);

        contentValues.put(mobile,Phone_number1);
        contentValues.put(city,city1);
        contentValues.put(state,state1);
        contentValues.put(area,area1);
        contentValues.put(address,address1);
        contentValues.put(landmark,landmark1);
        contentValues.put(alternate_no,alternate_no1);


        long result=database.insert(TABLE_NAME,null,contentValues);
        if(result==-1)
        {
            return false;

        }
        else
        {
            return true;
        }


    }

    //getting register item
    public Cursor getallData()
    {
        SQLiteDatabase database=this.getWritableDatabase();
        Cursor res=database.rawQuery("SELECT * FROM " + TABLE_NAME +" WHERE id=3",null );
        return res;
    }

    //deleting cart item
    public Boolean deleteCartItem(int id)
    {
        SQLiteDatabase database=this.getWritableDatabase();
        int cartId=id;
        database.execSQL("DELETE FROM "+CART_TABLE_NAME+" WHERE id="+cartId);
        return true;
    }

public Boolean delete_db()
{

    boolean b=mcontext.deleteDatabase(DATABASE_NAME);
    return b;
}
}
