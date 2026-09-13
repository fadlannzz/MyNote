package com.example.noteapp;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

public class NoteContentProvider extends ContentProvider {

    public static final String AUTHORITY =
            "com.example.noteapp.provider";

    public static final Uri CONTENT_URI =
            Uri.parse(
                    "content://" +
                            AUTHORITY +
                            "/catatan"
            );

    private Database database;

    @Override
    public boolean onCreate() {

        database =
                new Database(getContext());

        return true;
    }

    @Override
    public Cursor query(
            Uri uri,
            String[] projection,
            String selection,
            String[] selectionArgs,
            String sortOrder
    ) {

        return database
                .getReadableDatabase()
                .query(
                        "catatan",
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
    }

    @Override
    public String getType(Uri uri) {

        return "vnd.android.cursor.dir/vnd.note";
    }

    @Override
    public Uri insert(
            Uri uri,
            ContentValues values
    ) {

        long id =
                database.getWritableDatabase()
                        .insert(
                                "catatan",
                                null,
                                values
                        );

        if (id == -1) {
            return null;
        }

        return Uri.withAppendedPath(
                CONTENT_URI,
                String.valueOf(id)
        );
    }

    @Override
    public int delete(
            Uri uri,
            String selection,
            String[] selectionArgs
    ) {

        return database
                .getWritableDatabase()
                .delete(
                        "catatan",
                        selection,
                        selectionArgs
                );
    }

    @Override
    public int update(
            Uri uri,
            ContentValues values,
            String selection,
            String[] selectionArgs
    ) {

        return database
                .getWritableDatabase()
                .update(
                        "catatan",
                        values,
                        selection,
                        selectionArgs
                );
    }
}