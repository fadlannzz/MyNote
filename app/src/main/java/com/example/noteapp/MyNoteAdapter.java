package com.example.noteapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MyNoteAdapter
        extends RecyclerView.Adapter<MyNoteAdapter.NoteViewHolder> {

    public interface OnNoteMenuClickListener {
        void onMenuClick(MyNote note);
    }

    private final ArrayList<MyNote> daftarMyNote;
    private final OnNoteMenuClickListener listener;

    public MyNoteAdapter(
            ArrayList<MyNote> daftarMyNote,
            OnNoteMenuClickListener listener
    ) {
        this.daftarMyNote = daftarMyNote;
        this.listener = listener;
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType
    ) {

        View view =
                LayoutInflater.from(parent.getContext())
                        .inflate(
                                R.layout.item_note,
                                parent,
                                false
                        );

        return new NoteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(
            @NonNull NoteViewHolder holder,
            int position
    ) {

        MyNote note =
                daftarMyNote.get(position);

        holder.textJudul.setText(
                note.getJudul()
        );

        holder.textIsi.setText(
                note.getIsi()
        );

        holder.textTanggal.setText(
                note.getTanggal()
        );

        // Tombol titik tiga
        holder.btnMenu.setOnClickListener(v ->
                listener.onMenuClick(note)
        );
    }

    @Override
    public int getItemCount() {
        return daftarMyNote.size();
    }

    public static class NoteViewHolder
            extends RecyclerView.ViewHolder {

        TextView textJudul;
        TextView textIsi;
        TextView textTanggal;

        ImageButton btnMenu;

        public NoteViewHolder(
                @NonNull View itemView
        ) {
            super(itemView);

            textJudul =
                    itemView.findViewById(
                            R.id.textJudul
                    );

            textIsi =
                    itemView.findViewById(
                            R.id.textIsi
                    );

            textTanggal =
                    itemView.findViewById(
                            R.id.textTanggal
                    );

            btnMenu =
                    itemView.findViewById(
                            R.id.btnMenu
                    );
        }
    }
}