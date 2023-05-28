package com.morax.productimate.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import com.morax.productimate.R;
import com.morax.productimate.database.AppDatabase;
import com.morax.productimate.database.dao.UserDao;
import com.morax.productimate.database.entity.User;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                /* Create an Intent that will start the Menu-Activity. */
                Intent intent = new Intent(MainActivity.this , AccountActivity.class);
                startActivity(intent);
                finish();
            }
        }, 2000);

    }
}