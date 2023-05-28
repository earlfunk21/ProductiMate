package com.morax.productimate.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.morax.productimate.fragment.NoteFragment;
import com.morax.productimate.fragment.ProfileFragment;
import com.morax.productimate.fragment.TaskFragment;

public class HomeLayoutAdapter extends FragmentStateAdapter {
    public HomeLayoutAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (position == 1) {
            return new NoteFragment();
        } else if (position == 2) {
            return new TaskFragment();
        }
        return new ProfileFragment();
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
