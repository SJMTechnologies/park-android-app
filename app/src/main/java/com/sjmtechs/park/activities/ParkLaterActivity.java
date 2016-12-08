package com.sjmtechs.park.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.sjmtechs.park.R;
import com.sjmtechs.park.fragment.ParkLaterFragment;

public class ParkLaterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_park_later);

        ParkLaterFragment parkLaterFragment = new ParkLaterFragment();
        replaceFragment(parkLaterFragment,ParkLaterFragment.class.getSimpleName());
    }

    public void replaceFragment(Fragment fragment, String tag) {
        Log.e("TAG", "replaceFragment: " + tag);
        FragmentManager fm = getSupportFragmentManager();
        boolean isPopped = fm.popBackStackImmediate(tag, 0);
        if (!isPopped && fm.findFragmentByTag(tag) == null) {
            Fragment currentFragment = fm.findFragmentById(R.id.content_view);
            if (fragment != currentFragment) {
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.content_view, fragment, tag);
                /*ft.addToBackStack(tag);*/
                ft.commit();
            }
        }
    }
}
