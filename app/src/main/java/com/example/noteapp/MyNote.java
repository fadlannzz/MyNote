package com.example.noteapp;

public class MyNote {

    private int id;
    private String judul;
    private String isi;
    private String tanggal;

    public MyNote(int id, String judul, String isi, String tanggal) {
        this.id = id;
        this.judul = judul;
        this.isi = isi;
        this.tanggal = tanggal;
    }

    public MyNote(String judul, String isi, String tanggal) {
        this.judul = judul;
        this.isi = isi;
        this.tanggal = tanggal;
    }

    public int getId() {
        return id;
    }

    public String getJudul() {
        return judul;
    }

    public String getIsi() {
        return isi;
    }

    public String getTanggal() {
        return tanggal;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setJudul
            (String judul) {
        this.judul = judul;
    }

    public void setIsi(String isi) {
        this.isi = isi;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }
}