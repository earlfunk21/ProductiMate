package com.morax.productimate.fragment;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.morax.productimate.R;
import com.morax.productimate.activity.HomeActivity;
import com.morax.productimate.adapter.NoteAdapter;
import com.morax.productimate.database.AppDatabase;
import com.morax.productimate.database.dao.NoteDao;
import com.morax.productimate.database.entity.Note;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NoteFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NoteFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public NoteFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NoteFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NoteFragment newInstance(String param1, String param2) {
        NoteFragment fragment = new NoteFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_note, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        NoteDao noteDao = AppDatabase.getInstance(requireContext()).noteDao();
        List<Note> noteList = new ArrayList<>(noteDao.getNotes());
        RecyclerView rvNote = view.findViewById(R.id.rv_note);
        NoteAdapter noteAdapter = new NoteAdapter(requireContext(), noteList);
        rvNote.setAdapter(noteAdapter);

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                Note note = noteAdapter.getNoteByPosition(position);
                AlertDialog.Builder builder = new AlertDialog.Builder(requireContext(), R.style.AlertDialogCustom);
                builder.setCancelable(true);
                builder.setTitle("Are you sure?");
                builder.setMessage("You want to remove this post?");
                builder.setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                noteDao.delete(note);
                                noteList.remove(position);
                                noteAdapter.notifyItemRemoved(position);
                            }
                        });
                builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // retrieve the after if cancel
                        noteAdapter.notifyDataSetChanged();
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        }).attachToRecyclerView(rvNote);

        Button btnAddNote = view.findViewById(R.id.btn_add_note);
        btnAddNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(requireContext(), R.style.AlertDialogCustom);
                LayoutInflater inflater = LayoutInflater.from(requireContext());
                View dialogView = inflater.inflate(R.layout.popup_note_item, null);
                dialogBuilder.setView(dialogView);
                TextInputEditText etTitle = dialogView.findViewById(R.id.et_title_note);
                TextInputEditText etContent = dialogView.findViewById(R.id.et_content_note);
                dialogBuilder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Retrieve the text from the EditText

                        String title = Objects.requireNonNull(etTitle.getText()).toString();
                        String content = Objects.requireNonNull(etContent.getText()).toString();
                        if (title.equals("")
                                || content.equals("")) {
                            Toast.makeText(requireContext(), "Fields are required!", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        NoteDao noteDao = AppDatabase.getInstance(requireContext()).noteDao();
                        Note note = new Note(title, content);
                        noteDao.insert(note);
                        noteList.add(0, note);
                        noteAdapter.notifyItemInserted(0);
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
}