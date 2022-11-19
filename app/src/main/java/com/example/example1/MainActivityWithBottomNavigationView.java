package com.example.example1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivityWithBottomNavigationView extends AppCompatActivity {

    private static final String TAG = MainActivityWithBottomNavigationView.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_with_bottom_navigation_view);

        Toolbar toolbar = findViewById(R.id.top_appbar);
        setSupportActionBar(toolbar);

        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().
                findFragmentById(R.id.nav_host_fragment);
        NavController navController = navHostFragment.getNavController();
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);

        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.fragment_bookmark, R.id.fragment_route,
                R.id.fragment_explore, R.id.fragment_record, R.id.fragment_profile).build();

        // For the Toolbar
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

        // For the BottomNavigationView
        NavigationUI.setupWithNavController(bottomNav, navController);

    }
}