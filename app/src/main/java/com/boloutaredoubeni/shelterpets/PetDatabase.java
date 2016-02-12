package com.boloutaredoubeni.shelterpets;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.List;

/**
 * Copyright 2016 Boloutare Doubeni
 */
public class PetDatabase extends SQLiteOpenHelper {

  private static final String DATABASE_NAME = "shelteredpets.db";
  private static final int DATABASE_VERSION = 1;
  private static final String TABLE_NAME = "pets";

  private static final String COL_ID = "_id";
  public static final String COL_SPECIES = "species";

  private static PetDatabase INSTANCE = null;

  private PetDatabase(Context context) {
    super(context, DATABASE_NAME, null, DATABASE_VERSION);
  }

  public static PetDatabase getInstance(Context context) {
    if (INSTANCE == null) {
      INSTANCE = new PetDatabase(context);
    }
    return INSTANCE;
  }

  private final String CREATE_TABLE =
      "CREATE TABLE " + TABLE_NAME + "( " + COL_ID +
      " INTEGER PRIMARY KEY AUTOINCREMENT, " + COL_SPECIES + " TEXT)";

  @Override
  public void onCreate(SQLiteDatabase db) {
    db.execSQL(CREATE_TABLE);
  }

  @Override
  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {}

  public void insert(List<String> pets) {
    SQLiteDatabase db = INSTANCE.getWritableDatabase();
    ContentValues values = new ContentValues();
    for (String pet : pets) {
      values.put(COL_SPECIES, pet);
      db.insert(TABLE_NAME, null, values);
    }
  }

  public Cursor getPets() {
    return INSTANCE.getReadableDatabase().query(
        TABLE_NAME, new String[] {COL_ID, COL_SPECIES}, null, null, null, null,
        null);
  }

  public Cursor searchPets(String term) {
    String whereParam = COL_SPECIES + " LIKE '%" + term + "%'";
    return getReadableDatabase().query(TABLE_NAME, new String[]{COL_ID, COL_SPECIES}, whereParam, null, null, null, null);
  }
}
