package com.example.noteapp;

import android.content.Context;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FirebaseManager {

    // =================================================
    // ROOT DATABASE
    // =================================================

    private static final String ROOT_NOTES = "catatan";


    // =================================================
    // UPLOAD SEMUA CATATAN DARI SQLITE KE FIREBASE
    // =================================================

    public static void uploadAllNotes(
            List<MyNote> daftar,
            Context context
    ) {

        DatabaseReference reference =
                FirebaseDatabase
                        .getInstance()
                        .getReference(ROOT_NOTES);

        // Hapus data cloud lama terlebih dahulu
        reference.removeValue()
                .addOnCompleteListener(task -> {

                    if (!task.isSuccessful()) {

                        Toast.makeText(
                                context,
                                "Gagal menghapus backup cloud lama",
                                Toast.LENGTH_SHORT
                        ).show();

                        return;
                    }

                    // Upload setiap catatan
                    for (MyNote note : daftar) {

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

                        reference
                                .child(
                                        String.valueOf(
                                                note.getId()
                                        )
                                )
                                .setValue(data);
                    }

                    Toast.makeText(
                            context,
                            "Backup cloud berhasil",
                            Toast.LENGTH_SHORT
                    ).show();
                });
    }
}