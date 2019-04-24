package com.example.assignment7;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "markers.db";
    public static final String DATABASE_TABLE = "markers";
    public static final String ID = "_id";
    public static final String NAME = "_name";
    public static final String DES = "_des";
    public static final String LAT = "_lat";
    public static final String LNG = "_lng";
    public DBHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table "+DATABASE_TABLE+
                        "("+ID+" integer primary key autoincrement, "+
                NAME+" text not null, "+DES+" text not null, "+LAT+" text not null, "+LNG+" text not null);"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists "+DATABASE_TABLE);
        onCreate(db);
    }
    public void inserMarkers(Marker marker){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(NAME, marker.getLocName());
        cv.put(DES, marker.getLocDes());
        cv.put(LAT, marker.getLat());
        cv.put(LNG, marker.getLng());
        db.insert(DATABASE_TABLE, null,cv);
    }



    public ArrayList<Marker> getMarkers() {
        ArrayList<Marker> markers = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();

        String all_markerss_query = "select * from "+DATABASE_TABLE;

        Cursor c = db.rawQuery(all_markerss_query, null);
        for(c.moveToFirst();!c.isAfterLast();c.moveToNext()){
            markers.add(new Marker(c.getString(c.getColumnIndex(NAME)),
                    c.getString(c.getColumnIndex(DES)),
                    c.getString(c.getColumnIndex(LAT)),
                    c.getString(c.getColumnIndex(LNG))));
        }
        return markers;

    }


       /*public boolean deleteTitle(String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(DATABASE_TABLE, NOTE + "=?", new String[]{name}) > 0;
    }*/


   /* public ArrayList<Double> getLatLan(){
        ArrayList<Double> latlan_list = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();

        String lat_lan_query = "select "+ LAT+","+LNG+" from "+DATABASE_TABLE;

        Cursor c = db.rawQuery(lat_lan_query, null);


    }*/
}
