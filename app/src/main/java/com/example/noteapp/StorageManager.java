package com.example.noteapp;

import android.content.Context;
import android.net.Uri;
import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class StorageManager {

    private static final String INTERNAL_FILE =
            "catatan_backup.txt";

    // =========================================
    // CEK EXTERNAL STORAGE
    // =========================================

    public static boolean isExternalStorageAvailable() {

        String state =
                Environment.getExternalStorageState();

        return Environment.MEDIA_MOUNTED
                .equals(state);
    }

    // =========================================
    // INTERNAL STORAGE
    // =========================================

    public static boolean saveInternal(
            Context context,
            String text
    ) {

        File file =
                new File(
                        context.getFilesDir(),
                        INTERNAL_FILE
                );

        try (FileOutputStream fos =
                     new FileOutputStream(file)) {

            fos.write(
                    text.getBytes(
                            StandardCharsets.UTF_8
                    )
            );

            return true;

        } catch (Exception e) {

            e.printStackTrace();

            return false;
        }
    }

    // =========================================
    // CACHE
    // =========================================

    public static boolean saveCache(
            Context context,
            String text
    ) {

        File file =
                new File(
                        context.getCacheDir(),
                        "catatan_temp.txt"
                );

        try (FileOutputStream fos =
                     new FileOutputStream(file)) {

            fos.write(
                    text.getBytes(
                            StandardCharsets.UTF_8
                    )
            );

            return true;

        } catch (Exception e) {

            e.printStackTrace();

            return false;
        }
    }

    // =========================================
    // STORAGE INFO
    // =========================================

    public static String getStorageInfo(
            Context context
    ) {

        File storage =
                context.getFilesDir();

        long free =
                storage.getFreeSpace();

        long total =
                storage.getTotalSpace();

        return "Free: " +
                formatBytes(free) +
                "\nTotal: " +
                formatBytes(total);
    }

    // =========================================
    // EXPORT KE SHARED STORAGE
    // =========================================

    public static boolean writeNoteToUri(
            Context context,
            Uri uri,
            MyNote note
    ) {

        String content =
                "Judul: " +
                        note.getJudul() +
                        "\n\n" +
                        "Isi:\n" +
                        note.getIsi() +
                        "\n\n" +
                        "Tanggal:\n" +
                        note.getTanggal();

        try {

            OutputStream outputStream =
                    context.getContentResolver()
                            .openOutputStream(uri);

            if (outputStream == null) {
                return false;
            }

            outputStream.write(
                    content.getBytes(
                            StandardCharsets.UTF_8
                    )
            );

            outputStream.close();

            return true;

        } catch (Exception e) {

            e.printStackTrace();

            return false;
        }
    }

    // =========================================
    // DELETE INTERNAL
    // =========================================

    public static boolean deleteInternal(
            Context context
    ) {

        return context.deleteFile(
                INTERNAL_FILE
        );
    }

    // =========================================
    // FORMAT BYTES
    // =========================================

    private static String formatBytes(
            long bytes
    ) {

        if (bytes < 1024) {
            return bytes + " B";
        }

        if (bytes < 1024 * 1024) {
            return bytes / 1024 + " KB";
        }

        if (bytes < 1024 * 1024 * 1024) {
            return bytes /
                    (1024 * 1024) +
                    " MB";
        }

        return bytes /
                (1024L * 1024L * 1024L) +
                " GB";
    }
}