/*
 * Copyright 2014 University Of Wisconsin Parkside
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package uwp.cs.edu.parkingtracker.parking;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Joseph on 4/21/2015.
 * <p/>
 * This is for handling our sql lite database to store the saved parking spot
 */
public class DatabaseHandler extends SQLiteOpenHelper {

    //this is the version for the database
    private static final int DATABASE_VERSION = 1;

    //this is the name of the database
    private static final String DATABASE_NAME = "parkingSpotSaverDB.db";

    //table name
    public static final String TABLE_GPSPOINT = "gpspoint";

    //key for the database
    public static final String KEY_ID = "id";
    //column for the latitude
    public static final String COLUMN_LATITUDE = "latitude";
    //column for the longitude
    public static final String COLUMN_LONGITUDE = "longitude";

    private SQLiteDatabase sqliteDBInstance = null;

    private static DatabaseHandler sInstance;

    public static synchronized DatabaseHandler getInstance(Context context) {

        // Use the application context, which will ensure that you
        // don't accidentally leak an Activity's context.
        // See this article for more information: http://bit.ly/6LRzfx
        if (sInstance == null) {
            sInstance = new DatabaseHandler(context.getApplicationContext());
        }
        return sInstance;
    }

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.sqliteDBInstance = getWritableDatabase();
    }

    //this creates the database
    @Override
    public void onCreate(SQLiteDatabase sqliteDBInstance) {
        Log.i("onCreate", "Creating the database...");
        String DB_CREATE_SCRIPT = "create table " + TABLE_GPSPOINT +
                " ("+KEY_ID+" integer primary key, "+COLUMN_LATITUDE +
                " real, "+COLUMN_LONGITUDE+" real);)";


        sqliteDBInstance.execSQL(DB_CREATE_SCRIPT);
        ContentValues values = new ContentValues();
        values.put(KEY_ID, 1);
        values.put(COLUMN_LATITUDE, 0);
        values.put(COLUMN_LONGITUDE, 0);
        sqliteDBInstance.insert(TABLE_GPSPOINT, null, values);
    }

    public void openDB() throws SQLException
    {
        Log.i("openDB", "Checking sqliteDBInstance...");
        if(this.sqliteDBInstance == null)
        {
            Log.i("openDB", "Creating sqliteDBInstance...");
            this.sqliteDBInstance = this.getWritableDatabase();
        }
    }

    public void closeDB()
    {
        if(this.sqliteDBInstance != null)
        {
            if(this.sqliteDBInstance.isOpen())
                this.sqliteDBInstance.close();
        }
    }

    //this is the method to add the gps points, it overrides the old one everytime
    public void addGpsPoint(LatLng gpspoints) {
        ContentValues values = new ContentValues();

        values.put(COLUMN_LATITUDE, gpspoints.latitude);
        values.put(COLUMN_LONGITUDE, gpspoints.longitude);
        this.sqliteDBInstance.update(TABLE_GPSPOINT, values, KEY_ID + " = ?", new String[]{String.valueOf(1)});


    }

    public void clearPoint() {
        ContentValues values = new ContentValues();

        values.put(COLUMN_LATITUDE, 0);
        values.put(COLUMN_LONGITUDE, 0);
        this.sqliteDBInstance.update(TABLE_GPSPOINT, values, KEY_ID + " = ?", new String[]{String.valueOf(1)});
    }

    //this gets the gps point
    public LatLng getGpsPoint(int id) {
        String[] tableColumns = new String[] {
                COLUMN_LATITUDE, COLUMN_LONGITUDE};
        String whereClause = KEY_ID +" = ?";
        String[] whereArgs = new String[] {
                "1",
        };
        Cursor cursor = sqliteDBInstance.query(TABLE_GPSPOINT, tableColumns, whereClause,
                whereArgs, null, null, null);

        if (cursor.moveToFirst()) {
            Float lat = Float.parseFloat(cursor.getString(0));
            Float lon = Float.parseFloat(cursor.getString(1));
            cursor.close();
            if (lat != 0 || lon != 0) {
                return new LatLng(lat, lon);
            }
        }
        cursor.close();
        return null;
    }


    //this upgrades the database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_GPSPOINT);
        onCreate(db);
    }

}
