package com.morax.productimate.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.textfield.TextInputEditText;
import com.morax.productimate.R;
import com.morax.productimate.adapter.HomeLayoutAdapter;
import com.morax.productimate.database.AppDatabase;
import com.morax.productimate.database.dao.NoteDao;
import com.morax.productimate.database.entity.Note;

import java.util.Objects;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        ViewPager2 vp2Home = findViewById(R.id.vp2_home);
        HomeLayoutAdapter adapter = new HomeLayoutAdapter(getSupportFragmentManager(), getLifecycle());
        vp2Home.setAdapter(adapter);
        BottomNavigationView bnvHome = findViewById(R.id.bnv_home);
        vp2Home.setCurrentItem(0);
        bnvHome.setSelectedItemId(R.id.bnv_profile);
        bnvHome.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                int position = 0;
                if (itemId == R.id.bnv_note) {
                    position = 1;
                } else if (itemId == R.id.bnv_task) {
                    position = 2;
                }
                vp2Home.setCurrentItem(position);
                return true;
            }
        });
    }

    public void openEditProfile(View view) {
        Intent intent = new Intent(HomeActivity.this, UpdateProfileActivity.class);
        startActivity(intent);
    }

    public void logoutUser(View view) {
        Intent intent = new Intent(HomeActivity.this, AccountActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        SharedPreferences userPrefs = getSharedPreferences("userPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = userPrefs.edit();
        editor.clear();
        editor.apply();
    }


    public void addNote(View view) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(HomeActivity.this, R.style.AlertDialogCustom);
        LayoutInflater inflater = LayoutInflater.from(this);
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
                    Toast.makeText(HomeActivity.this, "Fields are required!", Toast.LENGTH_SHORT).show();
                    return;
                }
                NoteDao noteDao = AppDatabase.getInstance(HomeActivity.this).noteDao();
                Note note = new Note(title, content);
                noteDao.insert(note);
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
}