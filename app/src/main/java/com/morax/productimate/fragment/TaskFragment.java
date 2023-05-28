package com.morax.productimate.fragment;


import android.app.DatePickerDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.TextView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.morax.productimate.R;
import com.morax.productimate.adapter.TaskAdapter;
import com.morax.productimate.database.AppDatabase;
import com.morax.productimate.database.dao.TaskDao;
import com.morax.productimate.database.dao.UserDao;
import com.morax.productimate.database.entity.Note;
import com.morax.productimate.database.entity.Task;
import com.morax.productimate.util.DateFormatter;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TaskFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TaskFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public TaskFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TaskFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TaskFragment newInstance(String param1, String param2) {
        TaskFragment fragment = new TaskFragment();
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
        return inflater.inflate(R.layout.fragment_task, container, false);
    }

    private Date date = Calendar.getInstance().getTime();

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SharedPreferences userPrefs = requireContext().getSharedPreferences("userPrefs", Context.MODE_PRIVATE);
        long user_id = userPrefs.getLong("user_id", 0);

        TaskDao taskDao = AppDatabase.getInstance(requireContext()).taskDao();
        List<Task> taskList = new ArrayList<>(taskDao.getTaskByUserId(user_id));

        RecyclerView rvTask = view.findViewById(R.id.rv_task);
        TaskAdapter taskAdapter = new TaskAdapter(requireContext(), taskList);
        rvTask.setAdapter(taskAdapter);

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                Task task = taskAdapter.getTaskByPosition(position);
                AlertDialog.Builder builder = new AlertDialog.Builder(requireContext(), R.style.AlertDialogCustom);
                builder.setCancelable(true);
                builder.setTitle("Are you sure?");
                builder.setMessage("You want to remove this post?");
                builder.setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                taskDao.delete(task);
                                taskList.remove(position);
                                taskAdapter.notifyItemRemoved(position);
                            }
                        });
                builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // retrieve the after if cancel
                        taskAdapter.notifyDataSetChanged();
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        }).attachToRecyclerView(rvTask);

        FloatingActionButton fabTask = view.findViewById(R.id.fab_task);
        fabTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(requireContext());
                bottomSheetDialog.setContentView(R.layout.bottom_sheet_add_task);

                Button btnChooseDate = bottomSheetDialog.findViewById(R.id.btn_choose_date);
                Calendar calendar = Calendar.getInstance();
                TextView tvDate = bottomSheetDialog.findViewById(R.id.tv_task_date);
                String strDate = DateFormatter.formatDate(calendar.getTime());
                tvDate.setText(strDate);
                btnChooseDate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        openDateDialog(tvDate);
                    }
                });

                Button btnAddTask = bottomSheetDialog.findViewById(R.id.btn_add_task);

                TextInputEditText etTitle = bottomSheetDialog.findViewById(R.id.et_title_task);
                TextInputEditText etDescription = bottomSheetDialog.findViewById(R.id.et_description_task);

                btnAddTask.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String title = Objects.requireNonNull(etTitle.getText()).toString();
                        String description = Objects.requireNonNull(etDescription.getText()).toString();
                        Task task = new Task(title, date, description, user_id);
                        taskDao.insert(task);
                        taskList.add(0, task);
                        taskAdapter.notifyItemInserted(0);
                    }
                });

                bottomSheetDialog.show();
            }
        });

        CheckBox cbFilterTask = view.findViewById(R.id.cb_task_done_filter);
        cbFilterTask.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                taskAdapter.setTaskList(taskDao.getTaskByUserIdAndDone(user_id, b));


            }
        });
        Button btnDisplayAll = view.findViewById(R.id.btn_display_all);
        btnDisplayAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                taskAdapter.setTaskList(taskDao.getTaskByUserId(user_id));
            }
        });
    }



    public void openDateDialog(TextView view) {
        Calendar calendar = Calendar.getInstance();
        int initialYear = calendar.get(Calendar.YEAR);
        int initialMonth = calendar.get(Calendar.MONTH);
        int initialDay = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(requireContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                calendar.set(i, i1, i2);
                date = calendar.getTime();
                view.setText(DateFormatter.formatDate(date));
            }
        }, initialYear, initialMonth, initialDay);
        datePickerDialog.show();
    }
}