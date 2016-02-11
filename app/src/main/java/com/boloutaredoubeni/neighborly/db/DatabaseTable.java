package com.boloutaredoubeni.neighborly.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Copyright 2016 Boloutare Doubeni
 */
public class DatabaseTable {

  private static final String TAG = DatabaseTable.class.getCanonicalName();
  private static final String DATABASE_NAME = "locations.db";

  /* package */ static final String LOCATION_TABLE = "locations";
  private static final String COL_ID = "_id";
  private static final String COL_LOCATION = "location_name";
  private static final String COL_LAT = "latitude";
  private static final String COL_LON = "longitude";
  private static final String COL_AMENITY = "amenity";
  private static final String COL_OPEN = "open_at";
  private static final String COL_CLOSED = "closed_at";
  private static final String COL_URL = "url";
  private static final int VERSION = 1;

  private static DatabaseTable mINSTANCE;

  private final DatabaseHelper mDatabaseHelper;

  private DatabaseTable(Context context) {
    mDatabaseHelper = new DatabaseHelper(context);
  }

  public static DatabaseTable getInstance(Context context) {
    if (mINSTANCE == null) {
      mINSTANCE = new DatabaseTable(context);
    }
    return mINSTANCE;
  }

  private static class DatabaseHelper extends SQLiteOpenHelper {

    private final Context mContext;
    private SQLiteDatabase mDatabase;

    private static final String CREATE_TABLE =
        String.format("CREATE TABLE %s IF NOT EXISTS "
                          + "(%s INT AUTOINCREMENT NOT NULL,"
                          + " %s TEXT NOT NULL,"
                          + " %s REAL NOT NULL,"
                          + " %s REAL NOT NULL,"
                          + " %s TEXT,"
                          + " %s TEXT,"
                          + " %s TEXT,"
                          + " %s TEXT);",
                      LOCATION_TABLE, COL_ID, COL_LOCATION, COL_LAT, COL_LON,
                      COL_AMENITY, COL_OPEN, COL_CLOSED, COL_URL);

    DatabaseHelper(Context context) {
      super(context, DATABASE_NAME, null, VERSION);
      mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
      mDatabase = db;
      mDatabase.execSQL(CREATE_TABLE);
      Log.d(TAG, "Created table " + LOCATION_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
      mDatabase = db;
      mDatabase.execSQL("DROP TABLE IF EXISTS " + LOCATION_TABLE);
      onCreate(db);
    }
  }
}
