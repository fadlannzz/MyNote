package com.example.noteapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class Database extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "catatanku.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_CATATAN = "catatan";

    private static final String COL_ID = "id";
    private static final String COL_JUDUL = "judul";
    private static final String COL_ISI = "isi";
    private static final String COL_TANGGAL = "tanggal";

    public Database(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String query =
                "CREATE TABLE " + TABLE_CATATAN + " (" +
                        COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        COL_JUDUL + " TEXT NOT NULL, " +
                        COL_ISI + " TEXT NOT NULL, " +
                        COL_TANGGAL + " TEXT NOT NULL" +
                        ")";

        db.execSQL(query);
    }

    @Override
    public void onUpgrade(
            SQLiteDatabase db,
            int oldVersion,
            int newVersion
    ) {

        db.execSQL(
                "DROP TABLE IF EXISTS " + TABLE_CATATAN
        );

        onCreate(db);
    }

    // CREATE
    public long tambahMyNote(
            String judul,
            String isi,
            String tanggal
    ) {

        SQLiteDatabase db =
                getWritableDatabase();

        ContentValues values =
                new ContentValues();

        values.put(COL_JUDUL, judul);
        values.put(COL_ISI, isi);
        values.put(COL_TANGGAL, tanggal);

        return db.insert(
                TABLE_CATATAN,
                null,
                values
        );
    }

    // READ
    public ArrayList<MyNote> ambilSemuaMyNote() {

        ArrayList<MyNote> daftar =
                new ArrayList<>();

        SQLiteDatabase db =
                getReadableDatabase();

        Cursor cursor = db.query(
                TABLE_CATATAN,
                null,
                null,
                null,
                null,
                null,
                COL_ID + " DESC"
        );

        if (cursor.moveToFirst()) {

            do {

                int id = cursor.getInt(
                        cursor.getColumnIndexOrThrow(COL_ID)
                );

                String judul = cursor.getString(
                        cursor.getColumnIndexOrThrow(COL_JUDUL)
                );

                String isi = cursor.getString(
                        cursor.getColumnIndexOrThrow(COL_ISI)
                );

                String tanggal = cursor.getString(
                        cursor.getColumnIndexOrThrow(COL_TANGGAL)
                );

                daftar.add(
                        new MyNote(
                                id,
                                judul,
                                isi,
                                tanggal
                        )
                );

            } while (cursor.moveToNext());
        }

        cursor.close();

        return daftar;
    }

    // READ berdasarkan ID
    public MyNote ambilMyNote(int id) {

        SQLiteDatabase db =
                getReadableDatabase();

        Cursor cursor = db.query(
                TABLE_CATATAN,
                null,
                COL_ID + "=?",
                new String[]{String.valueOf(id)},
                null,
                null,
                null
        );

        MyNote note = null;

        if (cursor.moveToFirst()) {

            note = new MyNote(
                    cursor.getInt(
                            cursor.getColumnIndexOrThrow(COL_ID)
                    ),
                    cursor.getString(
                            cursor.getColumnIndexOrThrow(COL_JUDUL)
                    ),
                    cursor.getString(
                            cursor.getColumnIndexOrThrow(COL_ISI)
                    ),
                    cursor.getString(
                            cursor.getColumnIndexOrThrow(COL_TANGGAL)
                    )
            );
        }

        cursor.close();

        return note;
    }

    // UPDATE
    public int editMyNote(
            int id,
            String judul,
            String isi,
            String tanggal
    ) {

        SQLiteDatabase db =
                getWritableDatabase();

        ContentValues values =
                new ContentValues();

        values.put(COL_JUDUL, judul);
        values.put(COL_ISI, isi);
        values.put(COL_TANGGAL, tanggal);

        return db.update(
                TABLE_CATATAN,
                values,
                COL_ID + "=?",
                new String[]{String.valueOf(id)}
        );
    }

    // DELETE
    public int hapusMyNote(int id) {

        SQLiteDatabase db =
                getWritableDatabase();

        return db.delete(
                TABLE_CATATAN,
                COL_ID + "=?",
                new String[]{String.valueOf(id)}
        );
    }
}