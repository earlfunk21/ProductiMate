package com.morax.productimate.adapter;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;
import com.morax.productimate.R;
import com.morax.productimate.database.AppDatabase;
import com.morax.productimate.database.dao.TaskDao;
import com.morax.productimate.database.entity.Task;
import com.morax.productimate.util.DateFormatter;

import java.util.List;
import java.util.Objects;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.ViewHolder> {

    private Context context;
    private List<Task> taskList;

    public TaskAdapter(Context context, List<Task> taskList) {
        this.context = context;
        this.taskList = taskList;
    }

    public void setTaskList(List<Task> taskList) {
        this.taskList = taskList;
        notifyDataSetChanged();
    }

    public Task getTaskByPosition(int position) {
        return taskList.get(position);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.task_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Task task = taskList.get(position);
        holder.tvTitle.setText(task.title);
        holder.tvDescription.setText(task.description);
        holder.tvDate.setText(DateFormatter.formatDate(task.date));
        holder.cbDone.setChecked(task.done);
        Log.d("task", "Title: " + task.title);
        holder.cbDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TaskDao taskDao = AppDatabase.getInstance(context).taskDao();
                task.done = !task.done;
                taskDao.update(task);
                if (task.done)
                    createNotification();
            }
        });
        holder.cvTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context, R.style.AlertDialogCustom);
                LayoutInflater inflater = LayoutInflater.from(context);
                View dialogView = inflater.inflate(R.layout.popup_task_item, null);
                dialogBuilder.setView(dialogView);
                TextView tvTitle = dialogView.findViewById(R.id.tv_task_title);
                TextView tvDescription = dialogView.findViewById(R.id.tv_task_description);
                tvTitle.setText(task.title);
                tvDescription.setText(task.description);
                dialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
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

    private void createNotification() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("My Notification", "Task Notification", NotificationManager.IMPORTANCE_HIGH);
            NotificationManager manager = context.getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "My Notification");
        builder.setContentTitle("Successfully Task done!");
        builder.setContentText("Your task finally done!");
        builder.setSmallIcon(R.drawable.icon_task);
        builder.setAutoCancel(true);

        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(context);
        managerCompat.notify(1, builder.build());
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tvTitle, tvDescription, tvDate;
        public CheckBox cbDone;
        public CardView cvTask;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_title_task);
            cvTask = itemView.findViewById(R.id.cv_task);
            tvDescription = itemView.findViewById(R.id.tv_description_task);
            tvDate = itemView.findViewById(R.id.tv_date_task);
            cbDone = itemView.findViewById(R.id.cb_task_done);
        }
    }

}
