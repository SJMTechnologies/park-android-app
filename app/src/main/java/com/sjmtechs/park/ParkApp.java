package com.sjmtechs.park;

import android.app.Application;

import com.sjmtechs.park.preferences.Preferences;

public class ParkApp extends Application {

    public static Preferences preferences;

    public ParkApp() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        preferences = new Preferences(getApplicationContext());
    }
}
