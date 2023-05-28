package com.morax.productimate.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.morax.productimate.R;
import com.morax.productimate.adapter.HomeLayoutAdapter;

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

    public void openEditProfile(View view){
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
}