package it.unimib.camminatori.mysherpa.ui.activity;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.view.Menu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.osmdroid.config.Configuration;

import java.util.ArrayList;
import java.util.Arrays;

import it.unimib.camminatori.mysherpa.R;
import mil.nga.geopackage.BuildConfig;

public class MainActivity extends AppCompatActivity {
    private final int REQUEST_PERMISSIONS_REQUEST_CODE = 1;

    public MainActivity() {
        super(R.layout.activity_main);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences sharedPreferences = this.getSharedPreferences("MODE", Context.MODE_PRIVATE);
        System.out.println(sharedPreferences.getBoolean("night", false));
        if(!sharedPreferences.getBoolean("night", false)){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
        else{
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }

        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().
                findFragmentById(R.id.nav_host_fragment);
        NavController navController = navHostFragment.getNavController();
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        NavigationUI.setupWithNavController(bottomNav, navController);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        String[] permissions = new String[3];
        permissions[0] = Manifest.permission.WRITE_EXTERNAL_STORAGE;
        permissions[1] = Manifest.permission.ACCESS_COARSE_LOCATION;
        permissions[2] = Manifest.permission.ACCESS_FINE_LOCATION;
        requestPermissionsIfNecessary(permissions);

        //load/initialize the osmdroid configuration
        Context context = getApplicationContext();
        Configuration.getInstance().load(context, PreferenceManager.getDefaultSharedPreferences(context));
        Configuration.getInstance().setUserAgentValue(BuildConfig.APPLICATION_ID);

    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        ArrayList<String> permissionsToRequest =
                new ArrayList<>(Arrays.asList(permissions).subList(0, grantResults.length));

        if (permissionsToRequest.size() > 0) {
            ActivityCompat.requestPermissions
                    (this, permissionsToRequest.toArray(new String[0]), REQUEST_PERMISSIONS_REQUEST_CODE);
        }
    }

    private void requestPermissionsIfNecessary(String[] permissions) {
        ArrayList<String> permissionsToRequest = new ArrayList<>();
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission)
                    != PackageManager.PERMISSION_GRANTED) {
                // Permission is not granted
                permissionsToRequest.add(permission);
            }
        }

        if (permissionsToRequest.size() > 0) {
            ActivityCompat.requestPermissions(
                    this, permissionsToRequest.toArray(
                            new String[0]), REQUEST_PERMISSIONS_REQUEST_CODE);
        }
    }

}