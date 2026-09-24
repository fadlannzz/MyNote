package com.example.noteapp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.materialswitch.MaterialSwitch;

public class SettingsActivity extends AppCompatActivity {

    EditText editNama;
    MaterialSwitch switchAutoSave;

    Button btnSimpan;
    Button btnReset;

    MaterialToolbar toolbarSettings;

    // Nama file SharedPreferences
    private static final String PREF_NAME =
            "MyNotePreferences";

    // Key
    private static final String KEY_NAMA =
            "namaPengguna";

    private static final String KEY_AUTO_SAVE =
            "autoSave";

    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        // Hubungkan XML dengan Java
        toolbarSettings = findViewById(R.id.toolbarSettings);

        editNama = findViewById(R.id.editNama);

        switchAutoSave = findViewById(
                R.id.switchAutoSave
        );

        btnSimpan = findViewById(R.id.btnSimpan);
        btnReset = findViewById(R.id.btnReset);

        // Membuka SharedPreferences
        preferences = getSharedPreferences(
                PREF_NAME,
                MODE_PRIVATE
        );

        // Tombol kembali
        toolbarSettings.setNavigationOnClickListener(v -> {
            finish();
        });

        // Membaca data yang sudah pernah disimpan
        tampilkanPengaturan();

        // Tombol simpan
        btnSimpan.setOnClickListener(v -> {
            simpanPengaturan();
        });

        // Tombol reset
        btnReset.setOnClickListener(v -> {
            resetPengaturan();
        });
    }

    private void tampilkanPengaturan() {

        String nama =
                preferences.getString(
                        KEY_NAMA,
                        ""
                );

        boolean autoSave =
                preferences.getBoolean(
                        KEY_AUTO_SAVE,
                        true
                );

        editNama.setText(nama);

        switchAutoSave.setChecked(
                autoSave
        );
    }

    private void simpanPengaturan() {

        String nama =
                editNama.getText()
                        .toString()
                        .trim();

        boolean autoSave =
                switchAutoSave.isChecked();

        SharedPreferences.Editor editor =
                preferences.edit();

        editor.putString(
                KEY_NAMA,
                nama
        );

        editor.putBoolean(
                KEY_AUTO_SAVE,
                autoSave
        );

        editor.apply();

        Toast.makeText(
                this,
                "Pengaturan berhasil disimpan",
                Toast.LENGTH_SHORT
        ).show();
    }

    private void resetPengaturan() {

        SharedPreferences.Editor editor =
                preferences.edit();

        editor.clear();
        editor.apply();

        editNama.setText("");

        switchAutoSave.setChecked(
                true
        );

        Toast.makeText(
                this,
                "Pengaturan telah direset",
                Toast.LENGTH_SHORT
        ).show();
    }
}