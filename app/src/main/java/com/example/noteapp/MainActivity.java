package com.example.noteapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.material.appbar.MaterialToolbar;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    // =========================
    // VIEW
    // =========================

    RecyclerView recyclerCatatan;
    TextView tvSectionTitle;

    // =========================
    // DATABASE
    // =========================

    Database database;

    // =========================
    // ADAPTER & DATA
    // =========================

    MyNoteAdapter adapter;
    ArrayList<MyNote> daftarMyNote;

    // =========================
    // EXPORT VARIABLE
    // =========================

    private static final int REQUEST_EXPORT_FILE = 100;

    private MyNote noteYangAkanDiExport;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // =========================
        // INISIALISASI DATABASE
        // =========================

        database = new Database(this);
        // =========================
        // HUBUNGKAN VIEW
        // =========================

        recyclerCatatan = findViewById(R.id.recyclerCatatan);
        tvSectionTitle = findViewById(R.id.tvSectionTitle);

        // =========================
        // RECYCLER VIEW
        // =========================

        recyclerCatatan.setLayoutManager(
                new LinearLayoutManager(this)
        );

        // =========================
        // TOMBOL TAMBAH
        // =========================

        findViewById(R.id.btnTambah).setOnClickListener(v -> {

            Intent intent = new Intent(
                    MainActivity.this,
                    AddNote.class
            );

            startActivity(intent);
        });
    }

    // ==================================================
    // DIPANGGIL SETIAP KALI KEMBALI KE MAIN ACTIVITY
    // ==================================================

    @Override
    protected void onResume() {
        super.onResume();

        tampilkanSemuaMyNote();
    }

    // ==================================================
    // MENAMPILKAN SEMUA CATATAN
    // ==================================================

    private void tampilkanSemuaMyNote() {

        // Ambil semua data dari SQLite
        daftarMyNote = database.ambilSemuaMyNote();

        // Update jumlah catatan
        tvSectionTitle.setText(
                "All Notes (" + daftarMyNote.size() + ")"
        );

        // Buat adapter
        adapter = new MyNoteAdapter(
                daftarMyNote,
                note -> tampilkanMenuCatatan(note)
        );

        // Hubungkan adapter dengan RecyclerView
        recyclerCatatan.setAdapter(adapter);
    }

    // ==================================================
    // MENU TITIK TIGA
    // ==================================================

    private void tampilkanMenuCatatan(MyNote note) {

        String[] pilihan = {
                "Edit",
                "Hapus",
                "Export ke File"
        };

        new AlertDialog.Builder(this)
                .setTitle(note.getJudul())
                .setItems(
                        pilihan,
                        (dialog, which) -> {

                            if (which == 0) {

                                // Edit
                                editCatatan(note);

                            } else if (which == 1) {

                                // Hapus
                                konfirmasiHapus(note);

                            } else if (which == 2) {

                                // Export
                                exportCatatan(note);
                            }
                        }
                )
                .show();
    }

    // ==================================================
    // EDIT CATATAN
    // ==================================================

    private void editCatatan(MyNote note) {

        Intent intent = new Intent(
                MainActivity.this,
                AddNote.class
        );

        // Kirim ID catatan yang dipilih
        intent.putExtra(
                "note_id",
                note.getId()
        );

        startActivity(intent);
    }

    // ==================================================
    // HAPUS CATATAN
    // ==================================================

    private void konfirmasiHapus(MyNote note) {

        new AlertDialog.Builder(this)
                .setTitle("Hapus Catatan?")
                .setMessage(
                        "Apakah kamu yakin ingin menghapus \""
                                + note.getJudul()
                                + "\"?"
                )
                .setNegativeButton(
                        "Batal",
                        null
                )
                .setPositiveButton(
                        "Hapus",
                        (dialog, which) -> {

                            int hasil =
                                    database.hapusMyNote(
                                            note.getId()
                                    );

                            if (hasil > 0) {

                                Toast.makeText(
                                        this,
                                        "Catatan berhasil dihapus",
                                        Toast.LENGTH_SHORT
                                ).show();

                                // Refresh daftar
                                tampilkanSemuaMyNote();

                            } else {

                                Toast.makeText(
                                        this,
                                        "Gagal menghapus catatan",
                                        Toast.LENGTH_SHORT
                                ).show();
                            }
                        }
                )
                .show();
    }

    // ==================================================
    // EXPORT CATATAN
    // ==================================================

    private void exportCatatan(MyNote note) {

        // Pastikan external storage tersedia
        if (!StorageManager.isExternalStorageAvailable()) {

            Toast.makeText(
                    this,
                    "External storage tidak tersedia",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }

        Intent intent =
                new Intent(Intent.ACTION_CREATE_DOCUMENT);

        intent.addCategory(
                Intent.CATEGORY_OPENABLE
        );

        intent.setType("text/plain");

        intent.putExtra(
                Intent.EXTRA_TITLE,
                note.getJudul() + ".txt"
        );

        // Simpan note sementara
        noteYangAkanDiExport = note;

        startActivityForResult(
                intent,
                REQUEST_EXPORT_FILE
        );
    }

    // ==================================================
    // HASIL FILE PICKER
    // ==================================================

    @Override
    protected void onActivityResult(
            int requestCode,
            int resultCode,
            Intent data
    ) {

        super.onActivityResult(
                requestCode,
                resultCode,
                data
        );

        if (requestCode == REQUEST_EXPORT_FILE
                && resultCode == RESULT_OK
                && data != null) {

            android.net.Uri uri = data.getData();

            if (uri != null
                    && noteYangAkanDiExport != null) {

                boolean berhasil =
                        StorageManager.writeNoteToUri(
                                this,
                                uri,
                                noteYangAkanDiExport
                        );

                Toast.makeText(
                        this,
                        berhasil
                                ? "Catatan berhasil diekspor"
                                : "Gagal mengekspor catatan",
                        Toast.LENGTH_SHORT
                ).show();

                noteYangAkanDiExport = null;
            }
        }
    }

    // ==================================================
    // MEMANGGIL TOOLBAR MENU
    // ==================================================

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Memanggil res/menu/main_menu.xml
        getMenuInflater().inflate(
                R.menu.main_menu,
                menu
        );

        return true;
    }

    // ==================================================
    // TOOLBAR MENU CLICK
    // ==================================================

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        // =========================
        // SETTINGS
        // =========================

        if (id == R.id.action_settings) {

            Intent intent = new Intent(
                    MainActivity.this,
                    SettingsActivity.class
            );

            startActivity(intent);

            return true;
        }

        // =========================
        // STORAGE INFO
        // =========================

        if (id == R.id.action_storage) {

            Toast.makeText(
                    this,
                    StorageManager.getStorageInfo(this),
                    Toast.LENGTH_LONG
            ).show();

            return true;
        }

        // =========================
        // BACKUP INTERNAL
        // =========================

        if (id == R.id.action_internal) {

            ArrayList<MyNote> notes =
                    database.ambilSemuaMyNote();

            StringBuilder text =
                    new StringBuilder();

            for (MyNote note : notes) {

                text.append("Judul: ");
                text.append(note.getJudul());
                text.append("\n");

                text.append("Isi: ");
                text.append(note.getIsi());
                text.append("\n");

                text.append("Tanggal: ");
                text.append(note.getTanggal());
                text.append("\n\n");
            }

            boolean berhasil =
                    StorageManager.saveInternal(
                            this,
                            text.toString()
                    );

            Toast.makeText(
                    this,
                    berhasil
                            ? "Backup internal berhasil"
                            : "Backup internal gagal",
                    Toast.LENGTH_SHORT
            ).show();

            return true;
        }

        // =========================
        // CACHE
        // =========================

        if (id == R.id.action_cache) {

            boolean berhasil =
                    StorageManager.saveCache(
                            this,
                            "Backup sementara MyNote"
                    );

            Toast.makeText(
                    this,
                    berhasil
                            ? "Cache berhasil dibuat"
                            : "Gagal membuat cache",
                    Toast.LENGTH_SHORT
            ).show();

            return true;
        }

        // =========================
        // FIREBASE
        // =========================

        if (id == R.id.action_firebase) {

            FirebaseManager.uploadAllNotes(
                    database.ambilSemuaMyNote(),
                    this
            );

            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

