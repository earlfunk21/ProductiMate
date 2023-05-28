package com.morax.productimate.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;
import com.morax.productimate.R;
import com.morax.productimate.database.AppDatabase;
import com.morax.productimate.database.dao.NoteDao;
import com.morax.productimate.database.entity.Note;

import java.util.List;
import java.util.Objects;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.ViewHolder> {

    private Context context;
    private List<Note> noteList;

    public NoteAdapter(Context context, List<Note> noteList) {
        this.context = context;
        this.noteList = noteList;
    }

    public Note getNoteByPosition(int position){
        return noteList.get(position);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.note_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Note note = noteList.get(position);
        holder.tvTitle.setText(note.title);
        holder.cvNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context, R.style.AlertDialogCustom);
                LayoutInflater inflater = LayoutInflater.from(context);
                View dialogView = inflater.inflate(R.layout.popup_note_item, null);
                dialogBuilder.setView(dialogView);
                TextInputEditText etTitle = dialogView.findViewById(R.id.et_title_note);
                TextInputEditText etContent = dialogView.findViewById(R.id.et_content_note);
                etTitle.setText(note.title);
                etContent.setText(note.content);
                dialogBuilder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Retrieve the text from the EditText

                        String title = Objects.requireNonNull(etTitle.getText()).toString();
                        String content = Objects.requireNonNull(etContent.getText()).toString();
                        if (title.equals("")
                                || content.equals("")) {
                            Toast.makeText(context, "Fields are required!", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        NoteDao noteDao = AppDatabase.getInstance(context).noteDao();
                        note.title = title;
                        note.content = content;
                        noteDao.update(note);
                        int currentPosition = holder.getAdapterPosition();
                        notifyItemChanged(currentPosition);
                    }
                });
                dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                AlertDialog dialog = dialogBuilder.create();
                dialog.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return noteList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tvTitle;
        public CardView cvNote;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_title_note);
            cvNote = itemView.findViewById(R.id.cv_note);
        }
    }

}
