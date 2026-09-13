package com.example.noteapp;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class AddNote extends AppCompatActivity {

    TextInputEditText editJudul;
    TextInputEditText editIsi;

    MaterialButton btnSimpan;
    MaterialToolbar toolbar;

    Database Database;

    int noteId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_note);

        toolbar = findViewById(R.id.toolbar);
        editJudul = findViewById(R.id.editJudul);
        editIsi = findViewById(R.id.editIsi);
        btnSimpan = findViewById(R.id.btnSimpan);

        Database = new Database(this);

        // Cek apakah ini mode edit
        noteId = getIntent().getIntExtra("note_id", -1);

        if (noteId != -1) {
            toolbar.setTitle("Edit Catatan");
            btnSimpan.setText("UPDATE");

            MyNote note =
                    Database.ambilMyNote(noteId);

            if (note != null) {
                editJudul.setText(note.getJudul());
                editIsi.setText(note.getIsi());
            }
        }

        toolbar.setNavigationOnClickListener(v ->
                finish()
        );

        btnSimpan.setOnClickListener(v ->
                simpanMyNote()
        );
    }

    private void simpanMyNote() {

        String judul =
                editJudul.getText() != null
                        ? editJudul.getText().toString().trim()
                        : "";

        String isi =
                editIsi.getText() != null
                        ? editIsi.getText().toString().trim()
                        : "";

        if (judul.isEmpty()) {
            editJudul.setError(
                    "Judul tidak boleh kosong"
            );
            editJudul.requestFocus();
            return;
        }

        if (isi.isEmpty()) {
            editIsi.setError(
                    "Isi catatan tidak boleh kosong"
            );
            editIsi.requestFocus();
            return;
        }

        String tanggal =
                new SimpleDateFormat(
                        "dd-MM-yyyy HH:mm",
                        Locale.getDefault()
                ).format(new Date());

        long hasil;

        if (noteId == -1) {

            // Tambah
            hasil = Database.tambahMyNote(
                    judul,
                    isi,
                    tanggal
            );

        } else {

            // Edit
            hasil = Database.editMyNote(
                    noteId,
                    judul,
                    isi,
                    tanggal
            );
        }

        if (hasil != -1) {

            Toast.makeText(
                    this,
                    noteId == -1
                            ? "Catatan berhasil disimpan"
                            : "Catatan berhasil diperbarui",
                    Toast.LENGTH_SHORT
            ).show();

            finish();

        } else {

            Toast.makeText(
                    this,
                    "Gagal menyimpan catatan",
                    Toast.LENGTH_SHORT
            ).show();
        }
    }
}