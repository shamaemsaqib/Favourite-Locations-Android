package com.mad.madquiz03;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.location.Address;
import android.location.Geocoder;
import android.util.ArrayMap;
import android.view.View;

import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SQLLiteDB extends SQLiteOpenHelper {

    private Context view;
    private final static String DB_NAME = "FavourtieLocationsDB";

    SQLLiteDB(Context context){
        super(context, DB_NAME, null, 1);
        view = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String query = "CREATE TABLE FVRT_LOC(" +
                "ID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                "NAME VARCHAR(50) NOT NULL)";
        sqLiteDatabase.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE FVRT_LOC");
        onCreate(sqLiteDatabase);
    }

    public ArrayList<String> readRecords(){
        ArrayList<String> loc = new ArrayList<>();

        SQLiteDatabase read = this.getReadableDatabase();
        Cursor cursor = read.rawQuery("SELECT * FROM FVRT_LOC", null);


        int size = cursor.getCount();
        cursor.moveToFirst();

        int i = 0;
        while(!cursor.isAfterLast()){
            loc.add(cursor.getString(1));
            cursor.moveToNext();
        }
        read.close();
        return loc;
    }

    public ArrayList<LatLng> readLatLngs(){
        ArrayList<LatLng> latLngs;
        SQLLiteDB db = new SQLLiteDB(view);
        ArrayList<String> data = db.readRecords();

        Geocoder geocoder = new Geocoder(view);

        latLngs = new ArrayList<LatLng>(data.size());
        for (int i = 0; i < data.size(); i++) {
            try {
                List<Address> addresses = geocoder.getFromLocationName(data.get(i), 1);
                Address address = addresses.get(0);

                LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
                latLngs.add(latLng);
            }catch (IOException e){
                e.printStackTrace();
            }
        }

        return latLngs;
    }

    public void insert(String name){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues loc = new ContentValues();
        loc.put("NAME", name);

        db.insert("FVRT_LOC", null, loc);
        db.close();
    }
}
