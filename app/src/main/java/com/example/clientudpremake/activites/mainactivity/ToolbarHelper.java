package com.example.clientudpremake.activites.mainactivity;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.clientudpremake.R;
import com.google.android.material.navigation.NavigationView;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ToolbarHelper {
    private final MainActivity activity;

    public void init() {
        Toolbar toolbar = activity.findViewById(R.id.toolbar);
        activity.setSupportActionBar(toolbar);
        initActionBarDrawerToggle(toolbar);
        initNavigationView();
    }

    private void initActionBarDrawerToggle(Toolbar toolbar) {
        DrawerLayout drawer = activity.findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                activity, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
    }

    private void initNavigationView() {
        NavigationView navigationView = activity.findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(activity);
    }
}
