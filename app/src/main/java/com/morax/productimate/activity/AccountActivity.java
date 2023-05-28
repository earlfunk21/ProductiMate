package com.morax.productimate.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.textfield.TextInputEditText;
import com.morax.productimate.R;
import com.morax.productimate.adapter.AccountLayoutAdapter;
import com.morax.productimate.database.AppDatabase;
import com.morax.productimate.database.dao.UserDao;
import com.morax.productimate.database.entity.User;

import java.util.Objects;

public class AccountActivity extends AppCompatActivity {
    private UserDao userDao;
    private SharedPreferences userPrefs;
    private ViewPager2 viewPager2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        userPrefs = getSharedPreferences("userPrefs", MODE_PRIVATE);
        userDao = AppDatabase.getInstance(this).userDao();
        viewPager2 = findViewById(R.id.vp2_account);
        TabLayout tabLayout = findViewById(R.id.tl_account);
        viewPager2.setUserInputEnabled(false);
        FragmentManager fragmentManager = getSupportFragmentManager();
        AccountLayoutAdapter homeLayoutAdapter = new AccountLayoutAdapter(fragmentManager, getLifecycle());
        viewPager2.setAdapter(homeLayoutAdapter);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager2.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                tabLayout.selectTab(tabLayout.getTabAt(position));
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        userPrefs = getSharedPreferences("userPrefs", MODE_PRIVATE);
        userDao = AppDatabase.getInstance(this).userDao();
        long user_id = userPrefs.getLong("user_id", 0);
        User user = userDao.getUserById(user_id);
        if (user != null && user_id != 0) {
            Intent intent = new Intent(this, HomeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }
    }

    public void registerUser(View view) {
        TextInputEditText etUsername = findViewById(R.id.et_username_register);
        TextInputEditText etPassword = findViewById(R.id.et_password_register);
        TextInputEditText etPassword1 = findViewById(R.id.et_password1);
        TextInputEditText etFirstName = findViewById(R.id.et_firstname);
        TextInputEditText etLastName = findViewById(R.id.et_lastname);
        String username = Objects.requireNonNull(etUsername.getText()).toString();
        String password = Objects.requireNonNull(etPassword.getText()).toString();
        String confirm_password = Objects.requireNonNull(etPassword1.getText()).toString();
        String firstname = Objects.requireNonNull(etFirstName.getText()).toString();
        String lastname = Objects.requireNonNull(etLastName.getText()).toString();


        if (password.equals("")
                || confirm_password.equals("")
                || username.equals("")
                || firstname.equals("")
                || lastname.equals("")) {
            Toast.makeText(this, "Fields are required!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!password.equals(confirm_password)) {
            Toast.makeText(this, "Password must match!", Toast.LENGTH_SHORT).show();
            return;
        }
        User user = userDao.getUserByUsername(username);
        if (user != null) {
            Toast.makeText(this, "Username is already exists!", Toast.LENGTH_SHORT).show();
            return;
        }
        user = new User(
                username, password, firstname, lastname
        );
        userDao.insert(user);
        Toast.makeText(this, "Successfully Created!", Toast.LENGTH_SHORT).show();
        viewPager2.setCurrentItem(0);
    }


    public void loginUser(View view) {
        TextInputEditText etUsername = findViewById(R.id.et_username);
        TextInputEditText etPassword = findViewById(R.id.et_password);
        String username = Objects.requireNonNull(etUsername.getText()).toString();
        String password = Objects.requireNonNull(etPassword.getText()).toString();

        User user = userDao.checkUser(username, password);
        if (user == null) {
            Toast.makeText(this, "Invalid Credentials!", Toast.LENGTH_SHORT).show();
            return;
        }

        SharedPreferences.Editor editor = userPrefs.edit();
        editor.putLong("user_id", user.id);
        editor.apply();
        Intent intent = new Intent(AccountActivity.this, HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}