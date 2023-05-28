package com.morax.productimate.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.morax.productimate.R;
import com.morax.productimate.database.AppDatabase;
import com.morax.productimate.database.dao.UserDao;
import com.morax.productimate.database.entity.User;

import java.util.Objects;

public class UpdateProfileActivity extends AppCompatActivity {

    private UserDao userDao;
    private User user;
    private SharedPreferences userPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);
        userDao = AppDatabase.getInstance(this).userDao();
        userPrefs = getSharedPreferences("userPrefs", MODE_PRIVATE);
        user = userDao.getUserById(userPrefs.getLong("user_id", 0));
    }

    public void updateProfile(View view) {
        TextInputEditText etFirstName = findViewById(R.id.et_firstname_edit);
        TextInputEditText etLastName = findViewById(R.id.et_lastname_edit);
        String firstname = Objects.requireNonNull(etFirstName.getText()).toString();
        String lastname = Objects.requireNonNull(etLastName.getText()).toString();

        if (firstname.equals("")
                || lastname.equals("")) {
            Toast.makeText(this, "Fields are required!", Toast.LENGTH_SHORT).show();
            return;
        }
        user.firstname = firstname;
        user.lastname = lastname;
        userDao.update(user);
        Toast.makeText(this, "Successfully Created!", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(UpdateProfileActivity.this, HomeActivity.class);
        startActivity(intent);
        finish();
    }
}