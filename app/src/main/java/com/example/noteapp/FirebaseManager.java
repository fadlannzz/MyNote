package com.example.noteapp;

import android.content.Context;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FirebaseManager {

    private static final String DATABASE_URL =
            "https://my-note-7f92c-default-rtdb.asia-southeast1.firebasedatabase.app/";

    private static final String ROOT_NOTES = "catatan";


    // ==================================================
    // MENGAMBIL DATABASE FIREBASE
    // ==================================================

    private static DatabaseReference getNotesReference() {

        return FirebaseDatabase
                .getInstance(DATABASE_URL)
                .getReference(ROOT_NOTES);
    }


    // ==================================================
    // BACKUP SEMUA CATATAN SQLITE KE FIREBASE
    // ==================================================

    public static void uploadAllNotes(
            List<MyNote> daftar,
            Context context
    ) {

        if (daftar == null) {

            Toast.makeText(
                    context,
                    "Data catatan tidak ditemukan",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }

        DatabaseReference reference =
                getNotesReference();

        /*
         * Semua perubahan kita kumpulkan terlebih dahulu.
         * Setelah itu dikirim menggunakan updateChildren().
         */
        Map<String, Object> updates =
                new HashMap<>();

        for (MyNote note : daftar) {

            String path =
                    String.valueOf(note.getId());

            Map<String, Object> data =
                    new HashMap<>();

            data.put(
                    "id",
                    note.getId()
            );

            data.put(
                    "judul",
                    note.getJudul()
            );

            data.put(
                    "isi",
                    note.getIsi()
            );

            data.put(
                    "tanggal",
                    note.getTanggal()
            );

            updates.put(
                    path,
                    data
            );
        }

        /*
         * Kalau tidak ada catatan,
         * hapus semua data catatan di Firebase.
         */
        if (updates.isEmpty()) {

            reference.removeValue()
                    .addOnSuccessListener(unused -> {

                        Toast.makeText(
                                context,
                                "Firebase sudah dikosongkan",
                                Toast.LENGTH_SHORT
                        ).show();

                    })
                    .addOnFailureListener(e -> {

                        Toast.makeText(
                                context,
                                "Gagal menghapus data cloud: "
                                        + e.getMessage(),
                                Toast.LENGTH_LONG
                        ).show();

                    });

            return;
        }

        /*
         * Upload sekaligus.
         */
        reference.updateChildren(updates)
                .addOnSuccessListener(unused -> {

                    Toast.makeText(
                            context,
                            "Backup cloud berhasil",
                            Toast.LENGTH_SHORT
                    ).show();

                })
                .addOnFailureListener(e -> {

                    Toast.makeText(
                            context,
                            "Backup gagal: "
                                    + e.getMessage(),
                            Toast.LENGTH_LONG
                    ).show();
                });
    }
}