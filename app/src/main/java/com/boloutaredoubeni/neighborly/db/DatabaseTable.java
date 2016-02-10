package com.boloutaredoubeni.neighborly.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Copyright 2016 Boloutare Doubeni
 */
public class DatabaseTable {

  private static final String TAG = DatabaseTable.class.getCanonicalName();
  private static final String DATABASE_NAME = "locations.db";

  private static final String LOCATION_TABLE = "locations";
  private static final String COL_ID = "_id";
  private static final String COL_LOCATION = "location_name";
  // Taken from the coordinates member
  private static final String COL_LAT = "latitude";
  private static final String COL_LON = "longitude";
  private static final String COL_AMENITY = "amenity";
  private static final String COL_OPEN = "open_at";
  private static final String COL_CLOSED = "closed_at";
  private static final String COL_URL = "url";
  private static final String COL_VISITED = "last_visited";
  private static final String COL_FAVORITED = "favorited";
  private static final int VERSION = 1;

  private static DatabaseTable mINSTANCE;

  private final DatabaseHelper mDatabaseHelper;

  private DatabaseTable(Context context) {
    mDatabaseHelper = new DatabaseHelper(context);
  }

  public DatabaseTable getInstance(Context context) {
    if (mINSTANCE == null) {
      mINSTANCE = new DatabaseTable(context);
    }
    return mINSTANCE;
  }

  private static class DatabaseHelper extends SQLiteOpenHelper {

    private final Context mContext;
    private SQLiteDatabase mDatabase;

    DatabaseHelper(Context context) {
      super(context, DATABASE_NAME, null, VERSION);
      mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {}

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {}
  }

  private static class CacheHelper extends SQLiteOpenHelper {

    public CacheHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
      super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
  }
}
