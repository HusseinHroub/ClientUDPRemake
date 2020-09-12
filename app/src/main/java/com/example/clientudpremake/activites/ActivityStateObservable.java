package com.example.clientudpremake.activites;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.clientudpremake.activites.mainactivity.ActivityStateObserver;

import java.util.ArrayList;
import java.util.List;

public class ActivityStateObservable extends AppCompatActivity {
    private List<ActivityStateObserver> activityStateObservers;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityStateObservers = new ArrayList<>();
    }

    public void attachObserver(ActivityStateObserver activityStateObserver) {
        activityStateObservers.add(activityStateObserver);
    }

    @Override
    protected void onResume() {
        super.onResume();
        notify(true);
    }

    @Override
    protected void onPause() {
        super.onPause();
        notify(false);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        notify(false);
    }

    private void notify(boolean state) {
        for (ActivityStateObserver activityStateObserver : activityStateObservers) {
            activityStateObserver.isActivityResumed(state);
        }
    }

}
